<!DOCTYPE html>
<html>
<head>
	<title>hw6</title>
	<meta charset="utf-8"/>
	<style type="text/css">

		*{
			font-family: "Helvetica Neue", Helvetica;
		}
		.searchForm{
			text-align: center;
			margin:0 auto;
			width: 480px;
			background-color: #F5F4F5;
			height: 200px;
			border: 1px solid #CCCCCC;
		}

		table, td, th {
			text-align: center;
			border: 1px solid #CCCCCC;
		}

		table {
			margin: auto auto;
			border-collapse: collapse;
			width: 70%;
		}

		td{height: 32px;}

		a{
			text-decoration:none !important;
			text-decoration-color: #00ff00;
			margin-left: 5px;
		}

		a:hover{
			color: #3F3D41;
		}

		.inlineSmallArrow{
			display: inline-block;
			width: 13px;
			height: 13px;
		}

		#arrowImg{
			text-align: center;
			display: block;
			margin: 0 auto;
			width: 40px;
			height: 25px;
			margin-top: 5px;
			margin-bottom: 8px;
		}

		.header{
			text-align: left;
			background-color: #F3F3F3;
		}

		#container{
			width: 70%;
			margin-left: auto;
			margin-right: auto;
			margin-top: 10px;
			margin-bottom: 5px;
		}

		#large{
			padding-top: 10px;
			padding-bottom: 7px;
			font-size: 200%;
			font-style: italic;
		}
		#middle{width: 95%; margin:0 auto;}
		#info{margin-top: 10px;}
		#left, #right{
			display: inline-block;
			vertical-align: top;
			text-align: left;
			margin-top: 10px;
		}
		#left{
			float: left;
			margin-right: 0px;
		}

		#right{
			display: block;
			margin-left: 0px;
			float: left;
		}
		#error{margin-top: 10px;}
		.buttons{
			margin-top: 4px;
			padding-top: 0px;
			padding-bottom: 0px;
		}
		#greyLine{
			background-color: #CCCCCC;
			height:1px;
		}
		#cmt{text-align: left;font-style: italic;display: block;float:left;}
		#newsContainer table{background-color: #FBFBFB;margin-bottom: 10px;}
		#newsContainer td{text-align: left;}
		#show{text-align: center;color: grey; font-size:80%;}
		.content{background-color: #FAFAFA;}

	</style>
	<script src="https://code.highcharts.com/highcharts.js"></script>
	<script src="https://code.highcharts.com/modules/exporting.js"></script>
	<script type="text/javascript">
		function clearContent(){document.getElementById("stock").value = ""; document.getElementById("globalContainer").innerHTML = "";}

		function checkField(){
			if(document.getElementById("stock").value==null||document.getElementById("stock").value==""){ 
				window.alert("Please enter a symbol");
				return false;
			}else if(document.getElementById("stock").value.includes(" ")){
				window.alert("Pleas enter a symbol without space.");
				return false;
			}
		}
	</script>
</head>
<body>
	<div class="searchForm">
		<form action="./stock.php" method="POST" onsubmit="return checkField();">
			<div id="large">Stock Search</div>
			<div id="middle">
				<div id="greyLine"></div>
				<div id="left">Enter Stock Ticker Symbol:*&nbsp</div>
				<div id="right"><div><input id="stock" type="text" name="stock"></div>
				<div id="btn"><input class="buttons" type="submit" name="submit" value="Search"> <input class="buttons" type="button" name="clear" value="Clear" onclick="clearContent();"></div></div>
				<div id="cmt">* - Mandatory fields.</div>
			</div>
		</form>
	</div>
	<?php if(isset($_POST["submit"])): ?>
		<?php
			function get_data($url)
			{
				try{
					$res =file_get_contents($url);
				}catch(Exception $err){
					return null;
				}
				$statusCode = explode(" ",$http_response_header[0])[1];
				if($statusCode!="200"){
					return null;
				}else{
					try{
						$jsonObj = json_decode($res, true);
						return $jsonObj;
					}catch(Exception $e){
						return null;
					}
				}
			}

			$lastDay;
			$close;
			$open;
			$prevClose;
			$change;
			$changePercent;
			$range;
			$volume;
			$priceData;

			$obj = get_data('https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol='.$_POST['stock'].'&outputsize=full&apikey=RVQIKC0E68W0MHBN');
			
			if($obj != null && !isset($obj['Error Message'])){
				date_default_timezone_set('America/Los_Angeles');
				//extract information for table
				foreach($obj as $key=>$value)
				{
					if($key == 'Meta Data')
					{
						$lastDay = substr($value['3. Last Refreshed'], 0, 10);
					}elseif($key == "Time Series (Daily)"){
						$lastDayInfo = $value[$lastDay];
						$open = $lastDayInfo['1. open'];
						$close = $lastDayInfo['4. close'];
						$range = $lastDayInfo['3. low']." - ".$lastDayInfo['2. high'];
						$volume = $lastDayInfo['5. volume'];

						//get previous day info
						$prevDayStr = (new DateTime($lastDay))->modify('-1 day')->format('Y-m-d');
						$prevClose = $value[$prevDayStr]['4. close'];

						$change = ((float)$close-(float)$prevClose);
						$changePercent = ((float)$close-(float)$prevClose)/$prevClose;
						$priceData = $value;
					}
				}

				//generate chart
				$data_a = Array();
				$volumeData_a = Array();
				$timeline_a =Array();
				$sixMon = (new DateTime($lastDay))->modify('-171 days');
				$count = 0;
				$minDate = null;
				foreach($priceData as $key=>$value)
				{
					$date = new DateTime($key);
					if($date >= $sixMon && $count<=130){
						$minDate = $date;
						$timeline_a[]= $date->format('m/d');
						$data_a[]=(float)$value['4. close'];
						$volumeData_a[]=(float)$value['5. volume'];
						$count+=1;
					}
				}
				$timeline = array_reverse($timeline_a);
				$data = array_reverse($data_a);
				$volumeData = array_reverse($volumeData_a);

				$sixMon = $minDate->format('Y-m-d');

				//get the news data
				$newsObj = @file_get_contents("https://seekingalpha.com/api/sa/combined/".$_POST['stock'].".xml");
				if($newsObj==false){
					$newsTable = "";
				}else{
					$xml = simplexml_load_string($newsObj);
					$news = json_decode(json_encode($xml), true)["channel"]["item"];

					$count = 0;
					$newsTable = "<table id=\"newsTable\">";
					foreach($news as $item){
						if($count >= 5) break;
						if(strpos($item["link"], "/article/")!=false){
							$count+=1;
							$newsTable.="<tr><td><a target=\"_blank\" href=\"".$item["link"]."\">".$item["title"]."</a>&nbsp&nbsp&nbsp&nbsp&nbspPublicated Time: ".substr($item["pubDate"], 0, 26)."</td></tr>";
						}
					}
					$newsTable.="</table>";
				}
			}
		?>
		<?php if($obj!=null && !isset($obj['Error Message'])): ?>
			<div id="globalContainer">
			<table id="info">
				<tr>
					<td class="header"><b>Stock Ticker Symbol</b></td>
					<td class="content"><?php echo $_POST['stock']?></td>
				</tr>
				<tr>
					<td class="header"><b>Close</b></td>
					<td class="content"><?php echo $close ?></td>
				</tr>
				<tr>
					<td class="header"><b>Open</b></td>
					<td class="content"><?php echo $open ?></td>
				</tr>
				<tr>
					<td class="header"><b>Previous Close</b></td>
					<td class="content"><?php echo $prevClose ?></td>
				</tr>
				<tr>
					<td class="header"><b>Change</b></td>
					<td class="content">
						<?php echo sprintf("%.2f", $change) ?>
						<?php if($change > 0): ?>
							<img class="inlineSmallArrow" src="http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png">
						<?php else :?>
							<img class="inlineSmallArrow" src="http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png">
						<?php endif; ?>
					</td>
				</tr>
				<tr>
					<td class="header"><b>Change Percent</b></td>
					<td class="content">
						<?php echo sprintf("%.2f%%", $changePercent*100) ?>
						<?php if($change > 0): ?>
							<img class="inlineSmallArrow" src="http://cs-server.usc.edu:45678/hw/hw6/images/Green_Arrow_Up.png">
						<?php else :?>
							<img class="inlineSmallArrow" src="http://cs-server.usc.edu:45678/hw/hw6/images/Red_Arrow_Down.png">
						<?php endif; ?>		
					</td>
				</tr>
				<tr>
					<td class="header"><b>Day's Range</b></td>
					<td class="content"><?php echo $range ?></td>
				</tr>
				<tr>
					<td class="header"><b>Volume</b></td>
					<td class="content"><?php echo number_format($volume) ?></td>
				</tr>
				<tr>
					<td class="header"><b>Timestamp</b></td>
					<td class="content"><?php echo $lastDay ?> </td>
				</tr>
				<tr>
					<td class="header"><b>Indicator</b></td>
					<td class="content">
						<a href="javascript:priceChart()">Price</a>&nbsp
						<a href="javascript:sma()">SMA</a>&nbsp
						<a href="javascript:ema()">EMA</a>&nbsp
						<a href="javascript:stoch()">STOCH</a>&nbsp
						<a href="javascript:rsi()">RSI</a>&nbsp
						<a href="javascript:adx()">ADX</a>&nbsp
						<a href="javascript:cci()">CCI</a>&nbsp
						<a href="javascript:bbands()">BBANDS</a>&nbsp
						<a href="javascript:macd()">MACD</a>
					</td>
				</tr>
			</table>
			
			<div id="container" style="height: 480px; min-width: 450px;"></div>
			<div id="show">Click to show stock news</div>
			<div class="arrow"><img src="http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Down.png" onClick="showNews()" id="arrowImg"></div>
			<div id="newsContainer"></div>
			</div>
			<script type="text/javascript">

				//global variables for data
				var symbol =  <?php echo json_encode($_POST['stock']);?>;
				document.getElementById('stock').value = symbol;
				var ifClicked = false;
				var newsTable = <?php echo json_encode($newsTable);?>;

				var lastDay = new Date(<?php echo json_encode($lastDay);?>);
				var sixMon = new Date(<?php echo json_encode($sixMon);?>);
				console.log(sixMon);
				console.log(lastDay);

				var smaHC = null;
				var emaHC = null;
				var stochHC = null;
				var rsiHC = null;
				var adxHC = null;
				var cciHC = null;
				var bbandsHC = null;
				var macdHC = null;
				function priceChart(){
					Highcharts.chart('container',{
						    chart: {
						    	borderColor: '#CCCCCC',
								borderWidth: 1,
								zoomType: 'x',
							},
							title: {
								text: 'Stock Price('+<?php echo json_encode([(new DateTime($lastDay))->format('m/d/Y')]); ?>+')'
							},
							subtitle: {
								useHTML: true,
								text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
								style:{
									"color": "#0000FF"
								}
							},
							xAxis:{
								categories: <?php echo json_encode($timeline)?>,
								tickInterval:5,
								startOnTick: true,
								endOnTick: true
							},
							yAxis:[{
								title: {
									text: symbol+' Volume',
								},
								opposite: true,
								max: 350000000,
								tickAmount: 8
							},
							{
								title: {
									text: 'Price',
								},
								tickAmount: 8,
								tickInterval: 5
							}],
							legend: {
								layout: 'vertical',
								align: 'right',
								verticalAlign: 'middle',
								floating: false,
								backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
							},
							series:[
							{
								data: <?php echo json_encode($data)?>,
								type: 'area',
								lineColor: '#E52203',
								color: '#EF7F7F',
								fillOpacity: 0.5,
								yAxis: 1,
								name: symbol,
								marker: {
									enabled: false
								},
								threshold: null
							},
							{
								data:<?php echo json_encode($volumeData)?>,
								type: 'column',
								color: '#FFFFFF',
								name: symbol+' Volume'
							}]
					});
				}

				priceChart();

				function showNews() {
					if(!ifClicked){
						document.getElementById("arrowImg").src = "http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Up.png";
						document.getElementById("newsContainer").innerHTML=newsTable;
						document.getElementById("show").innerHTML="Click to hide stock news";
						ifClicked = true;
					}else{
						document.getElementById("newsContainer").innerHTML="";
						document.getElementById("arrowImg").src = "http://cs-server.usc.edu:45678/hw/hw6/images/Gray_Arrow_Down.png";
						document.getElementById("show").innerHTML="Click to show stock news";
						ifClicked = false;
					}
				}

				function sma(){
					if(smaHC == null)
					{
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=SMA&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: SMA"];
									var smaData = [];
									var smaX = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){

											smaX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											smaData.push(parseFloat(data[key]['SMA']));
										}
									}
									smaX.reverse();
									smaData.reverse();

									smaHC = {
										chart: {
											borderColor: '#CCCCCC',
											borderWidth: 1,
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
										legend: {
											layout: 'vertical',
											align: 'right',
											verticalAlign: 'middle',
											floating: false,
											backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
										},
										series:[
										{
											data: smaData,
											name: symbol,
											lineColor: '#FF0000',
											color: '#FF0000',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2,
												symbol: 'square'
											},
											lineWidth: 1,
											threshold: null
										}]
									};
									Highcharts.chart('container', smaHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();	
					}
					else{
						Highcharts.chart('container', smaHC);
					}
				}

				function ema(){
					if(emaHC == null)
					{
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}

						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=EMA&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);
						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: EMA"];
									var emaX = [];
									var emaData = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											emaX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											emaData.push(parseFloat(data[key]['EMA']));
										}
									}
									emaX.reverse();
									emaData.reverse();

									emaHC = {
										chart: {
											borderColor: '#CCCCCC',
											borderWidth: 1,
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
										legend: {
											layout: 'vertical',
											align: 'right',
											verticalAlign: 'middle',
											floating: false,
											backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
										},
										series:[
										{
											data: emaData,
											name: symbol,
											lineColor: '#FF0000',
											color: '#FF0000',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2,
												symbol: 'square'
											},
											lineWidth: 1,
											threshold: null
										}]
									};

									Highcharts.chart('container', emaHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container',emaHC);
					}			
				}

				function stoch(){

					if(stochHC == null)
					{
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=STOCH&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);
						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: STOCH"];
									var slowD = [];
									var slowK = [];
									var stochX = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											stochX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											slowK.push(parseFloat(data[key]['SlowK']));
											slowD.push(parseFloat(data[key]['SlowD']));
										}
									}
									slowD.reverse();
									slowK.reverse();
									stochX.reverse();
									stochHC = {
											chart: {
												borderColor: '#CCCCCC',
												borderWidth: 1,
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
											legend: {
												layout: 'vertical',
												align: 'right',
												verticalAlign: 'middle',
												floating: false,
												backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
											},
											series:[{
												data: slowK,
												name: symbol+' SlowK',
												lineColor: '#FF0000',
												color: '#FF0000',
												fillOpacity: 0.5,
												marker: {
													symbol: 'square',
													enabled: true,
													radius: 2
												},
												lineWidth: 1,
												threshold: null
											},
											{
												data: slowD,
												name: symbol+' SlowD',
												lineColor: '#7FAEE1',
												color: '#7FAEE1',
												fillOpacity: 0.5,
												marker: {
													symbol: 'square',
													enabled: true,
													radius: 2
												},
												lineWidth: 1,
												threshold: null
											}]
									};

									Highcharts.chart('container', stochHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container', stochHC);
					}
				}

				function rsi(){

					if(rsiHC==null)
					{
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET",  "https://www.alphavantage.co/query?function=RSI&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: RSI"];
									
									var rsiData = [];
									var rsiX = [];
									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											rsiX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											rsiData.push(parseFloat(data[key]['RSI']));
										}
									}
									rsiData.reverse();
									rsiX.reverse();

									rsiHC = {
											chart: {
												borderColor: '#CCCCCC',
												borderWidth: 1,
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
											legend: {
												layout: 'vertical',
												align: 'right',
												verticalAlign: 'middle',
												floating: false,
												backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
											},
											series:[
											{
												data: rsiData,
												name: symbol,
												lineColor: '#FF0000',
												color: '#FF0000',
												fillOpacity: 0.5,
												marker: {
													enabled: true,
													radius: 2,
													symbol: 'square'
												},
												lineWidth: 1,
												threshold: null
											}]
									};
									Highcharts.chart('container', rsiHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container', rsiHC);
					}
				}

				function adx(){
					if(adxHC == null){
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET",  "https://www.alphavantage.co/query?function=ADX&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: ADX"];
									var adxX = [];
									var adxData = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											adxX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											adxData.push(parseFloat(data[key]['ADX']));
										}
									} 
									adxX.reverse();
									adxData.reverse();

									adxHC = {
											chart: {
												borderColor: '#CCCCCC',
												borderWidth: 1,
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
											legend: {
												layout: 'vertical',
												align: 'right',
												verticalAlign: 'middle',
												floating: false,
												backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
											},
											series:[
											{
												data: adxData,
												name: symbol,
												lineColor: '#FF0000',
												color: '#FF0000',
												fillOpacity: 0.5,
												marker: {
													enabled: true,
													radius: 2,
													symbol: 'square'
												},
												lineWidth: 1,
												threshold: null
											}]
									};

									Highcharts.chart('container', adxHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}

						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container', adxHC);
					}
				}

				function cci(){
					if(cciHC == null){
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=CCI&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: CCI"];
									var cciX = [];
									var cciData = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											cciX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											cciData.push(parseFloat(data[key]['CCI']));
										}
									} 
									cciX.reverse();
									cciData.reverse();

									cciHC = {
											chart: {
												borderColor: '#CCCCCC',
												borderWidth: 1,
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
											legend: {
												layout: 'vertical',
												align: 'right',
												verticalAlign: 'middle',
												floating: false,
												backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
											},
											series:[
											{
												data: cciData,
												name: symbol,
												lineColor: '#FF0000',
												color: '#FF0000',
												fillOpacity: 0.5,
												marker: {
													enabled: true,
													radius: 2,
													symbol: 'square'
												},
												lineWidth: 1,
												threshold: null
											}]
									};
									Highcharts.chart('container', cciHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container', cciHC);
					}
				}

				function bbands() {
					if(bbandsHC==null){
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=BBANDS&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{
									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: BBANDS"];
									var lower = [];
									var middle = [];
									var upper = [];
									var bbandsX = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											bbandsX.push(key.substring(5, 7)+"/"+key.substring(8, 10));
											lower.push(parseFloat(data[key]['Real Lower Band']));
											upper.push(parseFloat(data[key]['Real Upper Band']));
											middle.push(parseFloat(data[key]['Real Middle Band']));
										}
									} 
									bbandsX.reverse();
									lower.reverse();
									upper.reverse();
									middle.reverse();

									bbandsHC = {
										chart: {
											borderColor: '#CCCCCC',
											borderWidth: 1,
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
										legend: {
											layout: 'vertical',
											align: 'right',
											verticalAlign: 'middle',
											floating: false,
											backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
										},
										series:[
										{
											data: lower,
											name: symbol+' Real Lower Band',
											lineColor: '#6DFE86',
											color: '#6DFE86',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2
											},
											lineWidth: 1,
											threshold: null
										},
										{
											data: middle,
											name: symbol+' Real Middle Band',
											lineColor: '#FF0000',
											color: '#FF0000',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2
											},
											lineWidth: 1,
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
												radius: 2
											},
											lineWidth: 1,
											threshold: null
										}]
									};
									Highcharts.chart('container', bbandsHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};
						xmlhttp.send();
					}else{
						Highcharts.chart('container', bbandsHC);
					}
				}

				function macd() {
					if(macdHC == null)
					{
						if (window.XMLHttpRequest){
							var xmlhttp=new XMLHttpRequest();
						}else{
							var xmlhttp=new ActiveXObject("Microsoft.XMLHTTP"); 
						}
						xmlhttp.open("GET", "https://www.alphavantage.co/query?function=MACD&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN", true);

						xmlhttp.onreadystatechange = function() {
							if(xmlhttp.status==200 && xmlhttp.readyState === XMLHttpRequest.DONE)
							{
								try{

									var datajs = JSON.parse(xmlhttp.responseText);
									var data = datajs["Technical Analysis: MACD"];
									var macdX = [];
									var macdHist = [];
									var macdSignal = [];
									var macdVal = [];

									for(var key in data){
										var date = new Date(key);
										if(date < sixMon) break;
										if(date>=sixMon && date<=lastDay){
											macdX.push(key.substring(5, 7)+"/"+key.substring(8,	10));
											macdHist.push(parseFloat(data[key]['MACD_Hist']));
											macdSignal.push(parseFloat(data[key]['MACD_Signal']));
											macdVal.push(parseFloat(data[key]['MACD']));
										}
									} 
									macdX.reverse();
									macdHist.reverse();
									macdSignal.reverse();
									macdVal.reverse();

									macdHC = {
										chart: {
											borderColor: '#CCCCCC',
											borderWidth: 1,
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
										legend: {
											layout: 'vertical',
											align: 'right',
											verticalAlign: 'middle',
											floating: false,
											backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
										},
										series:[
										{
											data: macdVal,
											name: symbol+' MACD',
											lineColor: '#FF0000',
											color: '#FF0000',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2
											},
											lineWidth: 1,old: null
										},
										{
											data: macdSignal,
											name: symbol+' MACD_Signal',
											lineColor: '#FDAE70',
											color: '#FDAE70',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2
											},
											lineWidth: 1,
											threshold: null
										},
										{
											data: macdHist,
											name: symbol+' MACD_Hist',
											lineColor: '#946EEA',
											color: '#946EEA',
											fillOpacity: 0.5,
											marker: {
												enabled: true,
												radius: 2
											},
											lineWidth: 1,
											threshold: null
										}]
									};
									Highcharts.chart('container', macdHC);
								}catch(e)
								{
									window.alert(e);
									window.alert("INVALID JSON FORMAT!");
									return;
								}
							}
						};

						xmlhttp.send();
					}else{
						Highcharts.chart('container', macdHC);
					}
				}
		</script>
		<?php elseif($obj == null):?>
			<script type="text/javascript">
				window.alert('Failed to fetch stock data, please retry.');
				var symbol =  <?php echo json_encode($_POST['stock']);?>;
				document.getElementById('stock').value = symbol;
			</script>
		<?php elseif (isset($obj['Error Message'])): ?>
			<div id="globalContainer">
				<table id="error">
				<tr>
					<td class="header"><b>Error</b></td>
					<td class="content">Error: NO record has been found, please enter a valid symbol</td>
				</tr>
				</table>
			</div>
			<script type="text/javascript">
				var symbol =  <?php echo json_encode($_POST['stock']);?>;
				document.getElementById('stock').value = symbol;
			</script>
		<?php endif;?>
	<?php endif; ?>
</body>
</html>