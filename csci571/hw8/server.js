var express = require('express');
var logger = require('morgan');
var path = require('path');
var bodyParser = require('body-parser');

var utils = require('./modules/utils');

var app = express();
var port = process.env.PORT||3000;

app.use(logger('dev'));
app.use(express.static(path.join(__dirname, 'dist')));
app.disable('etag');
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/api/auto_complete', function(req, res){
  var symbol = req.query.symbol;
  var query_url = 'http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input='+symbol;
  utils.get_ac(query_url, res);
});

app.get('/api/get_meta', function(req, res){
  var symbol = req.query.symbol;
  var query_url = 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&symbol='+symbol+'&apikey=RVQIKC0E68W0MHBN';
  utils.process_query(query_url, utils.process_meta, res, symbol);
});

app.get('/api/get_news', function(req, res){
  var symbol = req.query.symbol;
  utils.get_news(res, symbol);
});


app.get('/api/get_price', function(req, res) {
  var symbol = req.query.symbol;
  var query_url = 'https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&outputsize=full&symbol='+symbol+'&apikey=RVQIKC0E68W0MHBN';
  utils.process_query(query_url, utils.process_price_and_volume, res, symbol);
});

app.get('/api/get_sma', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=SMA&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=RVQIKC0E68W0MHBN";
  utils.process_query(query_url, utils.process_sma, res, symbol);
});

app.get('/api/get_ema', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=EMA&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=XBQC1HRNDMKTBDGH";
  utils.process_query(query_url, utils.process_ema, res, symbol);
});

app.get('/api/get_stoch', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=STOCH&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=XBQC1HRNDMKTBDGH";
  utils.process_query(query_url, utils.process_stoch, res, symbol);
});

app.get('/api/get_rsi', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=RSI&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=I27P610KHPMVPHRX";
  utils.process_query(query_url, utils.process_rsi, res, symbol);
});

app.get('/api/get_adx', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=ADX&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=I27P610KHPMVPHRX";
  utils.process_query(query_url, utils.process_adx, res, symbol);
});

app.get('/api/get_cci', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=CCI&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=I27P610KHPMVPHRX";
  utils.process_query(query_url, utils.process_cci, res, symbol);
});


app.get('/api/get_bbands', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=BBANDS&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=P7OX2F6NS53D6YCY";
  utils.process_query(query_url, utils.process_bbands, res, symbol);
});

app.get('/api/get_macd', function(req, res){
  var symbol = req.query.symbol;
  var query_url = "https://www.alphavantage.co/query?function=MACD&symbol="+symbol+"&interval=daily&time_period=10&series_type=open&apikey=P7OX2F6NS53D6YCY";
  utils.process_query(query_url, utils.process_macd, res, symbol);
});

app.post('/api/get_img', function(req, res){
  var async = req.body.async;
  var type = req.body.type;
  var options = req.body.options;
  utils.get_export_img(res, async, type, options);
})


app.listen(port, function () {
  console.log('HW8 app listening on port 3000!');
});


