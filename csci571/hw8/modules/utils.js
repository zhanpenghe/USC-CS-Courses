
var https = require('follow-redirects').https;
var parseString = require('xml2js').parseString;
var querystring = require('querystring');
var http = require('follow-redirects').http;
var moment = require('moment-timezone');
var request = require("request");

function get_data(url){
	https.get(url, query_res => {
		query_res.setEncoding("utf8");
		var body = '';
		query_res.on("data", str_data=>{
			body += str_data;
		});
		query_res.on("end", ()=>{
			try{
				return JSON.parse(body);
			}catch(ex){
				console.log('Failed on query: '+url);
				return get_data(url);
			}
		});
	});
}

function isEmpty(obj){
	for(var key in obj) return false;
	return true;
}

exports.process_query=function(url, process_function, res, symbol){
	try{	
		https.get(url, query_res => {
			query_res.setEncoding("utf8");
			var body = '';
			query_res.on("data", str_data=>{
				body += str_data;
			});
			query_res.on("end", ()=>{
				try{
					var obj = JSON.parse(body);
					if(obj['Error Message']!=null&& obj['Error Message']!=undefined){
						res.json({error: true});
					}else if(obj == undefined || obj== null||isEmpty(obj)){
						console.log(obj);
						res.json({error: true});
					}else{
						var response = process_function(obj, symbol);
						res.json(response);
					}
				}catch(ex){
					console.log(ex);
					console.log(body);
					console.log('Failed on query: '+url);
					res.json({error: true});
				}
			});
		});
	}catch(e){
		res.json({error: true});
	}
};

exports.get_ac=function(url, res){
	http.get(url, query_res => {
		query_res.setEncoding("utf8");
		var body = '';
		query_res.on("data", str_data=>{
			body += str_data;
		});
		query_res.on("end", ()=>{
			try{
				res.json(JSON.parse(body));
			}catch(ex){
				console.log(ex);
				console.log(body);
				res.json({});
			}
		});
	});
};

exports.get_news=function(res, symbol){
	try{
		var url = 'https://seekingalpha.com/api/sa/combined/'+symbol+'.xml';
		https.get(url, query_res => {
			query_res.setEncoding("utf8");
			var body = '';
			query_res.on("data", str_data=>{
				body += str_data;
			});
			query_res.on("end", ()=>{
				var response = body;

				parseString(response, function (err, result) {
					try{	
						var news_obj = result['rss']['channel'][0]['item'];
						var result = [];
						var count = 0;

						for(var i = 0; i<news_obj.length; i++){
							if(count>=5) break;
							if(news_obj[i]['link'][0].includes('article')){
								var temp = {};
								temp['title'] = news_obj[i]['title'][0];
								temp['link'] = news_obj[i]['link'][0];
								temp['pubDate'] = news_obj[i]['pubDate'][0].substring(0, 26)+" "+moment(news_obj[i]['pubDate'][0]).tz('America/New_York').format('z');
								temp['author'] = news_obj[i]['sa:author_name'][0];
								result.push(temp);
								count+=1;
							}
						}
						res.send(result);
					}catch(e){
						res.json({error: true});
					}
				});
			});
		});
	}catch(e){
		res.json({error: true});
	}
}

exports.process_meta=function(obj, symbol){
	try{
		var price_and_vol = obj['Time Series (Daily)'];
		var result = {};
		var i = 0;
		for(var key in price_and_vol){
			if(i === 0){
				result['lastDayClose'] = parseFloat(price_and_vol[key]['4. close']);
				result['lastDayVol'] = parseFloat(price_and_vol[key]['5. volume']);
			}else if(i === 1){
				result['change'] = parseFloat(result['lastDayClose']) - parseFloat(price_and_vol[key]['4. close']);
				result['changePercent'] = (parseFloat(result['lastDayClose']) - parseFloat(price_and_vol[key]['4. close']))/parseFloat(price_and_vol[key]['4. close'])*100;
			}
			else break;
			i+=1;
		}
		return result;
	}catch(e){
		return {error: true};
	}
}

exports.process_price_and_volume=function(obj, symbol){
	try{
			var symbol = obj['Meta Data']['2. Symbol'];
			var price_and_vol = obj['Time Series (Daily)'];
			var table_obj = {};
			table_obj['symbol'] = symbol;
			var last = obj['Meta Data']["3. Last Refreshed"];
			if(last.length<=15){
				last+=' 16:00:00';
			}
			table_obj['lastDay'] = last+' '+moment(obj['Meta Data']["3. Last Refreshed"]).tz('America/New_York').format('z');
			//retrieve data
			var i = 0;
			dates = [];
			prices = [];
			volume = [];
			stock_data = [];
			var maxVol= -2000;
			var maxPrice = -2000;
			for(var key in price_and_vol){
				if(i ===0){
					table_obj['lastDayOpen'] = parseFloat(price_and_vol[key]['1. open']);
					table_obj['hi'] = parseFloat(price_and_vol[key]['2. high']);
					table_obj['lo'] = parseFloat(price_and_vol[key]['3. low']);
					table_obj['lastDayClose'] = price_and_vol[key]['4. close'];
					table_obj['lastDayVol'] = price_and_vol[key]['5. volume'];
				}else if(i===1){
					table_obj['change'] = parseFloat(table_obj['lastDayClose']) - parseFloat(price_and_vol[key]['4. close']);
					table_obj['changePercent'] = (parseFloat(table_obj['lastDayClose']) - parseFloat(price_and_vol[key]['4. close']))/parseFloat(price_and_vol[key]['4. close'])*100;
					table_obj['prevClose'] = parseFloat(price_and_vol[key]['4. close']);
				}
				if(i>1000) break;
				if(i<=140){
					dates.push(key.substring(5, 7)+"/"+key.substring(8, 10));
					prices.push(parseFloat(price_and_vol[key]['4. close']));
					volume.push(parseFloat(price_and_vol[key]['5. volume']));
					if(parseFloat(price_and_vol[key]['4. close'])>maxPrice) maxPrice = parseFloat(price_and_vol[key]['4. close']);
					if(parseFloat(price_and_vol[key]['5. volume'])>maxVol) maxVol= parseFloat(price_and_vol[key]['5. volume']);
				}
				stock_data.push([(new Date(key)).getTime(), parseFloat(price_and_vol[key]['4. close'])]);
				i+=1;
			}

			console.log(maxVol);
			console.log(maxPrice);

			dates.reverse();
			prices.reverse();
			volume.reverse();

			stock_data.reverse();

			var hi_charts_obj={
				chart:{
					zoomType: 'x'
				},
				title: {
					text: symbol.toUpperCase()+' Stock Price and Volume'
				},
				subtitle: {
					useHTML: true,
					text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
					style:{
						"color": "#0000FF"
					}
				},
				xAxis:{
					categories:dates,
					tickInterval:5,
					startOnTick: true,
					endOnTick: true
				},
				yAxis:[
				{
					title: {
						text: symbol+' Volume',
					},
					max: maxVol*2,
					opposite: true,
					tickAmount: 8
				},
				{
					title: {
						text: 'Price',
					},
					max: maxPrice*1.2,
					tickAmount: 8,
				}],
				series:[
				{
					data: prices,
					type: 'area',
					lineColor: '#0000ff',
					color: '#e6e7fb',
					fillOpacity: 0.5,
					yAxis: 1,
					name: symbol,
					marker: {
						enabled: false
					},
					threshold: null
				},
				{
					data: volume,
					type: 'column',
					color: '#ff0000',
					name: symbol+' Volume'
				}],
				plotOptions: {
		            series: {
		                animation: false
		            }
		        }
			};
			var result = {
				'table_obj': table_obj,
				'hi_charts_obj': hi_charts_obj,
				'hi_stock_data': stock_data
			};
			return result;
		}catch(e){
			return {error: true};
		}
};

exports.process_sma=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: SMA"];
		var smaData = [];
		var smaX = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			smaX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			smaData.push(parseFloat(data[key]['SMA']));
			count-=1;
		}
		smaX.reverse();
		smaData.reverse();
		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Simple Moving Average (SMA)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: smaX,
				tickInterval:5
			},
			yAxis:
			{
				title: {
					text: 'SMA',
				},
				labels: {
					format: '{value}'
				}
			},
			tooltip:{
				shared: true
			},
			series:[
			{
				data: smaData,
				name: symbol,
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};

exports.process_ema=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: EMA"];
		var emaData = [];
		var emaX = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			emaX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			emaData.push(parseFloat(data[key]['EMA']));
			count-=1;
		}
		emaX.reverse();
		emaData.reverse();

		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Exponential Moving Average (EMA)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: emaX,
				tickInterval:5
			},
			yAxis:
			{
				title: {
					text: 'EMA',
				},
				labels: {
					format: '{value}'
				}
			},
			tooltip:{
				shared: true
			},
			series:[
			{
				data: emaData,
				name: symbol,
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};

exports.process_stoch=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: STOCH"];

		var slowD = [];
		var slowK = [];
		var stochX = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			stochX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			slowK.push(parseFloat(data[key]['SlowK']));
			slowD.push(parseFloat(data[key]['SlowD']));
			count-=1;
		}
		slowD.reverse();
		slowK.reverse();
		stochX.reverse();

		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Stochastic Oscillator (STOCH)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: stochX,
				tickInterval:5
			},
			yAxis:
			{
				title: {
					text: 'STOCH',
				},
				labels: {
					format: '{value}'
				}
			},
			series:[{
				data: slowK,
				name: symbol+' SlowK',
				lineColor: '#000000',
				color: '#FF0000',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			},
			{
				data: slowD,
				name: symbol+' SlowD',
				lineColor: '#7FAEE1',
				color: '#7FAEE1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};


exports.process_rsi=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: RSI"];
		var rsiData = [];
		var rsiX = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			rsiX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			rsiData.push(parseFloat(data[key]['RSI']));
			count-=1;
		}
		rsiX.reverse();
		rsiData.reverse();

		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Related Strenth Index (RSI)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: rsiX,
				tickInterval:5
			},
			yAxis:
			{
				title: {
					text: 'RSI',
				},
				labels: {
					format: '{value}'
				}
			},
			tooltip:{
				shared: true
			},
			series:[
			{
				data: rsiData,
				name: symbol,
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};

exports.process_adx=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: ADX"];
		var adxX = [];
		var adxData = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			adxX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			adxData.push(parseFloat(data[key]['ADX']));
			count-=1;
		}
		adxX.reverse();
		adxData.reverse();
		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Average Directional movement indeX (ADX)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: adxX,
				tickInterval:5,
			},
			yAxis:
			{
				title: {
					text: 'ADX',
				},
				labels: {
					format: '{value}'
				}
			},
			tooltip:{
				shared: true
			},
			series:[
			{
				data: adxData,
				name: symbol,
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};


exports.process_cci=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: CCI"];
		var cciX = [];
		var cciData = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			cciX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			cciData.push(parseFloat(data[key]['CCI']));
			count-=1;
		}
		cciX.reverse();
		cciData.reverse();
		var result ={
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Commodity Channel Index (CCI)'
			},
			subtitle: {
				useHTML: true,
				text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
				style:{
					"color": "#0000FF"
				}
			},
			xAxis:{
				categories: cciX,
				tickInterval:5,
			},
			yAxis:
			{
				title: {
					text: 'CCI',
				},
				labels: {
					format: '{value}'
				}
			},
			tooltip:{
				shared: true
			},
			series:[
			{
				data: cciData,
				name: symbol,
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: false,
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error:true};
	}
};


exports.process_bbands=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: BBANDS"];

		var lower = [];
		var middle = [];
		var upper = [];
		var bbandsX = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			bbandsX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
			lower.push(parseFloat(data[key]['Real Lower Band']));
			upper.push(parseFloat(data[key]['Real Upper Band']));
			middle.push(parseFloat(data[key]['Real Middle Band']));
			count-=1;
		}
		bbandsX.reverse();
		lower.reverse();
		upper.reverse();
		middle.reverse();

		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Bollinger Bands (BBANDS)'
			},
			subtitle: {
					useHTML: true,
					text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
					style:{
						"color": "#0000FF"
					}
				},
			xAxis:{
				categories: bbandsX,
				tickInterval:5,
			},
			yAxis:
			{
				title: {
					text: 'BBANDS',
				},
				labels: {
					format: '{value}'
				}
			},
			series:[
			{
				data: lower,
				name: symbol+' Real Lower Band',
				lineColor: '#ace493',
				color: '#ace493',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			},
			{
				data: middle,
				name: symbol+' Real Middle Band',
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			},
			{
				data: upper,
				name: symbol+' Read Upper band',
				lineColor: '#000000',
				color: '#000000',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};


exports.process_macd=function(obj, symbol){
	try{
		var data = obj["Technical Analysis: MACD"];

		var macdX = [];
		var macdHist = [];
		var macdSignal = [];
		var macdVal = [];
		var count = 140;

		for(var key in data){
			if(count<0) break;
			macdX.push(key.substring(5, 7)+"/"+key.substring(8,	10));
			macdHist.push(parseFloat(data[key]['MACD_Hist']));
			macdSignal.push(parseFloat(data[key]['MACD_Signal']));
			macdVal.push(parseFloat(data[key]['MACD']));
			count-=1;
		}
		macdX.reverse();
		macdHist.reverse();
		macdSignal.reverse();
		macdVal.reverse();

		var result = {
			chart: {
				zoomType: 'x'
			},
			title: {
				text: 'Moving Average Convergence/Divergence (MACD)'
			},
			subtitle: {
				useHTML: true,
					text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
					style:{
						"color": "#0000FF"
					}
				},
			xAxis:{
				categories: macdX,
				tickInterval:5,
			},
			yAxis:
			{
				title: {
					text: 'MACD',
				},
				labels: {
					format: '{value}'
				}
			},
			series:[
			{
				data: macdVal,
				name: symbol+' MACD',
				lineColor: '#9db4d1',
				color: '#9db4d1',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			},
			{
				data: macdSignal,
				name: symbol+' MACD_Signal',
				lineColor: '#000000',
				color: '#000000',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			},
			{
				data: macdHist,
				name: symbol+' MACD_Hist',
				lineColor: '#ace493',
				color: '#ace493',
				fillOpacity: 0.5,
				marker: {
					enabled: true,
					radius: 1.2
				},
				lineWidth: 2,
				threshold: null
			}],
			plotOptions: {
	            line: {
	                animation: false
	            }
	        }
		};
		return result;
	}catch(e){
		return {error: true};
	}
};

exports.get_export_img=function(res, async, type, options){
	try{
		var obj = {};
		obj.async = 'true';
		obj.type = type;
		obj.options = JSON.stringify(options);
		request.post({
			url: "https://export.highcharts.com", 
			formData: obj
		}, function(err, response, body){
			res.send(body);
		});
	}catch(e){
		console.log(e);
		res.send('');
	}
}


