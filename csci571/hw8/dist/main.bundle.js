webpackJsonp(["main"],{

/***/ "../../../../../src/$$_lazy_route_resource lazy recursive":
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncatched exception popping up in devtools
	return Promise.resolve().then(function() {
		throw new Error("Cannot find module '" + req + "'.");
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "../../../../../src/$$_lazy_route_resource lazy recursive";

/***/ }),

/***/ "../../../../../src/app/app.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "form{\n    min-height: 80px;\n}\n\n.form-elem{\n    margin-top: 10px;\n}\n\n.center_div{\n    background-color: white;\n    margin-top: 10px;\n    border-radius: 10px;\n}\n.titles{\n    font-size: 95%;\n    margin-top: 10px;\n}\n.has-error{\n    border-color: red;\n}\n.has-success{\n    border-color: blue;\n}\n#title{\n    padding-top: 20px;\n    margin-bottom: 0px;\n}\n#line{\n    margin-top: 20px;\n    margin-bottom: 10px;\n    height:1px;\n    border-radius: 0px;\n}\n#bottom_container{\n    padding-top: 15px;\n}\n\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/app.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"row\">\n\t<div class=\"col-lg-2 col-md-1 col-sm-1 col-xs-1\"></div>\n\t<div class=\"container center_div col-lg-8 col-md-10 col-sm-10 col-xs-10\" id=\"search_form\">\n\t\t<div class=\"text-center\" id=\"title\" style=\"font-size: 120%;\"><b>Stock Market Search</b></div>\n\t\t<form novalidate [formGroup]=\"rForm\" (ngSubmit)=\"submit_form(rForm.value)\" class=\"form-horizontal\" id=\"submit_form\">\n\n\t\t\t<div class=\"form-group row\" [ngClass]=\"{'has-error':symbol.invalid && (symbol.dirty || symbol.touched || symbol.submitted), 'has-feedback':symbol.valid && (symbol.dirty || symbol.touched)}\">\n\t\t\t\t<label for=\"enter-symbol\" class=\"col-lg-3 col-md-3  col-form-label titles\"><b>Enter Stock Ticker Symbol:<span style=\"color: red;\">*</span></b></label>\n\t\t\t\t<div class=\"col-lg-6 col-md-4 form-elem\">\n\t\t\t\t\t<input matInput type=\"text\" [(ngModel)]=\"stockInput\" formControlName='symbol' class=\"form-control is-valid\" id=\"symbol_ticker\" name=\"symbol\" placeholder=\"e.g. AAPL\" [matAutocomplete]=\"auto\" (keyup)=\"onKey($event)\" required>\n\t\t\t\t\t\t\n\t\t\t\t\t<mat-autocomplete #auto=\"matAutocomplete\">\n\t\t\t\t\t\t<mat-option *ngFor=\"let ac of autoCompleteContent\" [value]=\"ac.Symbol\">{{ac.Symbol}} - {{ac.Name}} ({{ac.Exchange}})</mat-option>\n\t\t\t\t\t</mat-autocomplete>\n\n\t\t\t\t\t<div *ngIf=\"symbol.invalid && (symbol.dirty || symbol.touched)\" class=\"invalid-feedback\">Please enter a stock ticker symbol.</div>\n\t\t\t\t</div>\n\n\t\t\t\t<div class=\"col-lg-3 col-md-5 form-elem\">\n\t\t\t\t\t<button type=\"submit\" class=\"btn btn-primary\" [disabled]=\"symbol.invalid\"><span class=\"glyphicon glyphicon-search\"></span>&nbsp;Search</button>\n\t\t\t\t\t<button type=\"clear\" class=\"btn btn-default\" (click)=\"clear()\"><span class=\"glyphicon glyphicon-refresh\"></span>&nbsp;Clear</button>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</form>\n\t</div>\n</div>\n<div class=\"row\">\n\t<div class=\"col-lg-2 col-md-1 col-sm-1 col-xs-1\"></div>\n\t<div class=\"container col-lg-8 col-md-10 col-sm-10 col-xs-10 center_div\" id=\"line\"></div>\n</div>\n<div class=\"row\">\n\t<div class=\"col-lg-2 col-md-1 col-sm-1 col-xs-1\"></div>\n\t<div class=\"container col-lg-8 col-md-10 col-sm-10 col-xs-10 center_div\" id=\"bottom_container\">\n\t\t<div *ngIf=\"isFav\" [@enterAnimation]=\"animated\"><fav-list></fav-list></div>\n\t\t<div *ngIf=\"isDetails\" [@enterAnimationDetails]><app-stock-details></app-stock-details></div>\n\t</div>\n</div>\n"

/***/ }),

/***/ "../../../../../src/app/app.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_forms__ = __webpack_require__("../../../forms/esm5/forms.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common_http__ = __webpack_require__("../../../common/esm5/http.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__angular_animations__ = __webpack_require__("../../../animations/esm5/animations.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var AppComponent = (function () {
    function AppComponent(http, data) {
        this.http = http;
        this.data = data;
        //stock data
        this.stockData = Array();
        this.done = new Array();
        this.news = Array();
        this.animated = 'off';
        this.stockInput = '';
        this.autoCompleteContent = [];
    }
    AppComponent.prototype.NoWhitespaceValidator = function (control) {
        var valid = control.value.trim().length === 0;
        return !valid ? null : { 'whitespace': true };
    };
    AppComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.isFav = true;
        this.isDetails = false;
        for (var i = 0; i < 12; i++) {
            this.stockData[i] = new Object;
            this.done[i] = false;
            this.data.data[i].subscribe(function (updated) { return _this.stockData[i] = updated; });
            this.data.done[i].subscribe(function (updated) { return _this.done[i] = updated; });
        }
        this.data.symbol.subscribe(function (updated) { return _this.stockSymbol = updated; });
        this.data.isFav.subscribe(function (updated) { return _this.isFav = updated; });
        this.data.isDetails.subscribe(function (updated) { return _this.isDetails = updated; });
        this.symbol = new __WEBPACK_IMPORTED_MODULE_1__angular_forms__["b" /* FormControl */]('', [__WEBPACK_IMPORTED_MODULE_1__angular_forms__["k" /* Validators */].required, this.NoWhitespaceValidator]);
        this.rForm = new __WEBPACK_IMPORTED_MODULE_1__angular_forms__["c" /* FormGroup */]({
            symbol: this.symbol,
        });
    };
    AppComponent.prototype.switchToDetails = function () {
        this.isFav = false;
        this.isDetails = true;
        this.animated = 'off';
    };
    AppComponent.prototype.switchToFavs = function () {
        this.isFav = true;
        this.isDetails = false;
        this.animated = 'on';
    };
    AppComponent.prototype.updateData = function (i) {
        this.data.update(i, this.stockData[i]);
    };
    AppComponent.prototype.get_price = function (params) {
        var _this = this;
        this.stockData[0] = null;
        this.stockData[1] = null;
        this.stockData[10] = null;
        this.http.get('/api/get_price', { params: params }).subscribe(function (data) {
            if (data['error'] == true) {
                _this.stockData[0] = data;
                _this.stockData[1] = data;
                _this.stockData[10] = data;
                _this.data.updateError(0, true);
                _this.data.updateError(1, true);
                _this.data.updateError(10, true);
            }
            else {
                _this.stockData[0] = data['table_obj'];
                _this.stockData[1] = data['hi_charts_obj'];
                _this.stockData[10] = data['hi_stock_data'];
            }
            _this.updateData(0);
            _this.updateData(1);
            _this.updateData(10);
            _this.done[0] = true;
            _this.done[1] = true;
            _this.done[10] = true;
            _this.data.doneWithDataTransfer(0, _this.done[0]);
            _this.data.doneWithDataTransfer(1, _this.done[1]);
            _this.data.doneWithDataTransfer(10, _this.done[10]);
        });
    };
    AppComponent.prototype.get_sma = function (params) {
        var _this = this;
        this.stockData[2] = null;
        this.http.get('/api/get_sma', { params: params }).subscribe(function (data) {
            _this.stockData[2] = data;
            if (data['error'] == true)
                _this.data.updateError(2, true);
            _this.updateData(2);
            _this.done[2] = true;
            _this.data.doneWithDataTransfer(2, _this.done[2]);
        });
    };
    AppComponent.prototype.get_ema = function (params) {
        var _this = this;
        this.stockData[3] = null;
        this.http.get('/api/get_ema', { params: params }).subscribe(function (data) {
            _this.stockData[3] = data;
            if (data['error'] == true)
                _this.data.updateError(3, true);
            _this.updateData(3);
            _this.done[3] = true;
            _this.data.doneWithDataTransfer(3, _this.done[3]);
        });
    };
    AppComponent.prototype.get_stoch = function (params) {
        var _this = this;
        this.stockData[4] = null;
        this.http.get('/api/get_stoch', { params: params }).subscribe(function (data) {
            _this.stockData[4] = data;
            if (data['error'] == true)
                _this.data.updateError(4, true);
            _this.updateData(4);
            _this.done[4] = true;
            _this.data.doneWithDataTransfer(4, _this.done[4]);
        });
    };
    AppComponent.prototype.get_rsi = function (params) {
        var _this = this;
        this.stockData[5] = null;
        this.http.get('/api/get_rsi', { params: params }).subscribe(function (data) {
            _this.stockData[5] = data;
            if (data['error'] == true)
                _this.data.updateError(5, true);
            _this.updateData(5);
            _this.done[5] = true;
            _this.data.doneWithDataTransfer(5, _this.done[5]);
        });
    };
    AppComponent.prototype.get_adx = function (params) {
        var _this = this;
        this.stockData[6] = null;
        this.http.get('/api/get_adx', { params: params }).subscribe(function (data) {
            _this.stockData[6] = data;
            if (data['error'] == true)
                _this.data.updateError(6, true);
            _this.updateData(6);
            _this.done[6] = true;
            _this.data.doneWithDataTransfer(6, _this.done[6]);
        });
    };
    AppComponent.prototype.get_cci = function (params) {
        var _this = this;
        this.stockData[7] = null;
        this.http.get('/api/get_cci', { params: params }).subscribe(function (data) {
            _this.stockData[7] = data;
            if (data['error'] == true)
                _this.data.updateError(7, true);
            _this.updateData(7);
            _this.done[7] = true;
            _this.data.doneWithDataTransfer(7, _this.done[7]);
        });
    };
    AppComponent.prototype.get_bbands = function (params) {
        var _this = this;
        this.stockData[8] = null;
        this.http.get('/api/get_bbands', { params: params }).subscribe(function (data) {
            _this.stockData[8] = data;
            if (data['error'] == true)
                _this.data.updateError(8, true);
            _this.updateData(8);
            _this.done[8] = true;
            _this.data.doneWithDataTransfer(8, _this.done[8]);
        });
    };
    AppComponent.prototype.get_macd = function (params) {
        var _this = this;
        this.stockData[9] = null;
        this.http.get('/api/get_macd', { params: params }).subscribe(function (data) {
            _this.stockData[9] = data;
            if (data['error'] == true)
                _this.data.updateError(9, true);
            _this.updateData(9);
            _this.done[9] = true;
            _this.data.doneWithDataTransfer(9, _this.done[9]);
        });
    };
    AppComponent.prototype.get_news = function (params) {
        var _this = this;
        this.news = null;
        this.data.updateNews(this.news);
        this.http.get('/api/get_news', { params: params }).subscribe(function (data) {
            _this.news = Object.values(data);
            if (_this.news[0] == true) {
                _this.data.updateError(11, true);
            }
            _this.data.updateNews(_this.news);
            _this.data.doneWithDataTransfer(11, true);
        });
    };
    AppComponent.prototype.clear = function () {
        this.stockInput = '';
        this.data.updateView(true, false, true);
    };
    AppComponent.prototype.submit_form = function (get) {
        if (this.rForm.valid) {
            for (var i = 0; i < 12; i++) {
                this.done[i] = false;
                this.data.doneWithDataTransfer(i, this.done[i]);
                this.data.updateError(i, false);
            }
            this.stockSymbol = get.symbol;
            var params = new __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["c" /* HttpParams */]();
            params = params.append('symbol', get.symbol);
            this.switchToDetails();
            this.animated = 'on';
            this.get_price(params);
            this.get_sma(params);
            this.get_ema(params);
            this.get_news(params);
            this.get_adx(params);
            this.get_stoch(params);
            this.get_bbands(params);
            this.get_cci(params);
            this.get_rsi(params);
            this.get_macd(params);
            this.data.updateSymbol(this.stockSymbol);
        }
        else {
            this.symbol.markAsTouched();
        }
    };
    AppComponent.prototype.onKey = function (event) {
        var _this = this;
        if (event.key == 'ArrowUp' || event.key == 'ArrowDown')
            return;
        this.autoCompleteContent = [];
        var temp = event.target.value;
        if (temp == null || temp == undefined || temp == "")
            return;
        var params = new __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["c" /* HttpParams */]();
        params = params.append('symbol', temp);
        this.http.get('/api/auto_complete', { params: params }).subscribe(function (data) {
            _this.autoCompleteContent = Object.values(data);
        });
    };
    AppComponent.prototype.get_data_for_fav = function (symbol) {
        for (var i = 0; i < 12; i++) {
            this.done[i] = false;
            this.data.doneWithDataTransfer(i, this.done[i]);
            this.data.updateError(i, false);
        }
        this.stockSymbol = symbol;
        var params = new __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["c" /* HttpParams */]();
        params = params.append('symbol', symbol);
        this.switchToDetails();
        this.animated = 'on';
        this.get_price(params);
        this.get_sma(params);
        this.get_ema(params);
        this.get_news(params);
        this.get_adx(params);
        this.get_bbands(params);
        this.get_cci(params);
        this.get_rsi(params);
        this.get_macd(params);
        this.get_stoch(params);
        this.data.updateSymbol(this.stockSymbol);
    };
    AppComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-root',
            template: __webpack_require__("../../../../../src/app/app.component.html"),
            styles: [__webpack_require__("../../../../../src/app/app.component.css")],
            animations: [
                Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["k" /* trigger */])('enterAnimation', [
                    Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["j" /* transition */])('void => on', [
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ transform: 'translateX(-100%)', opacity: 0 }),
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["e" /* animate */])('500ms', Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ transform: 'translateX(0)', opacity: 1 }))
                    ])
                ]),
                Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["k" /* trigger */])('enterAnimationDetails', [
                    Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["j" /* transition */])('void => *', [
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ transform: 'translateX(100%)', opacity: 0 }),
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["e" /* animate */])('500ms', Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ transform: 'translateX(0)', opacity: 1 }))
                    ])
                ])
            ],
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_2__angular_common_http__["a" /* HttpClient */], __WEBPACK_IMPORTED_MODULE_3__services_data_service__["a" /* DataService */]])
    ], AppComponent);
    return AppComponent;
}());



/***/ }),

/***/ "../../../../../src/app/app.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return AppModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__ = __webpack_require__("../../../platform-browser/esm5/platform-browser.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__ = __webpack_require__("../../../platform-browser/esm5/animations.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_material___ = __webpack_require__("../../../material/esm5/material.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_ngx_facebook__ = __webpack_require__("../../../../ngx-facebook/dist/esm/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__angular_forms__ = __webpack_require__("../../../forms/esm5/forms.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__angular_common_http__ = __webpack_require__("../../../common/esm5/http.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__components_fav_list_fav_list_component__ = __webpack_require__("../../../../../src/app/components/fav-list/fav-list.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__components_stock_details_stock_details_component__ = __webpack_require__("../../../../../src/app/components/stock-details/stock-details.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__components_stock_details_details_details_component__ = __webpack_require__("../../../../../src/app/components/stock-details/details/details.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__components_stock_details_hi_charts_hi_charts_component__ = __webpack_require__("../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_13__components_stock_details_progress_bar_progress_bar_component__ = __webpack_require__("../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_14__components_stock_details_stock_news_stock_news_component__ = __webpack_require__("../../../../../src/app/components/stock-details/stock-news/stock-news.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_15__components_stock_details_hist_hist_component__ = __webpack_require__("../../../../../src/app/components/stock-details/hist/hist.component.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
















var AppModule = (function () {
    function AppModule() {
    }
    AppModule = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_2__angular_core__["H" /* NgModule */])({
            declarations: [
                __WEBPACK_IMPORTED_MODULE_5__app_component__["a" /* AppComponent */],
                __WEBPACK_IMPORTED_MODULE_8__components_fav_list_fav_list_component__["a" /* FavListComponent */],
                __WEBPACK_IMPORTED_MODULE_9__components_stock_details_stock_details_component__["a" /* StockDetailsComponent */],
                __WEBPACK_IMPORTED_MODULE_10__components_stock_details_details_details_component__["a" /* DetailsComponent */],
                __WEBPACK_IMPORTED_MODULE_12__components_stock_details_hi_charts_hi_charts_component__["a" /* HiChartsComponent */],
                __WEBPACK_IMPORTED_MODULE_13__components_stock_details_progress_bar_progress_bar_component__["a" /* ProgressBarComponent */],
                __WEBPACK_IMPORTED_MODULE_14__components_stock_details_stock_news_stock_news_component__["a" /* StockNewsComponent */],
                __WEBPACK_IMPORTED_MODULE_15__components_stock_details_hist_hist_component__["a" /* HistComponent */],
            ],
            imports: [
                __WEBPACK_IMPORTED_MODULE_0__angular_platform_browser__["a" /* BrowserModule */],
                __WEBPACK_IMPORTED_MODULE_6__angular_forms__["e" /* FormsModule */],
                __WEBPACK_IMPORTED_MODULE_6__angular_forms__["j" /* ReactiveFormsModule */],
                __WEBPACK_IMPORTED_MODULE_7__angular_common_http__["b" /* HttpClientModule */],
                __WEBPACK_IMPORTED_MODULE_3__angular_material___["a" /* MatAutocompleteModule */],
                __WEBPACK_IMPORTED_MODULE_3__angular_material___["b" /* MatFormFieldModule */],
                __WEBPACK_IMPORTED_MODULE_3__angular_material___["c" /* MatInputModule */],
                __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_animations__["a" /* BrowserAnimationsModule */],
                __WEBPACK_IMPORTED_MODULE_4_ngx_facebook__["a" /* FacebookModule */].forRoot()
            ],
            providers: [
                __WEBPACK_IMPORTED_MODULE_11__services_data_service__["a" /* DataService */],
            ],
            bootstrap: [__WEBPACK_IMPORTED_MODULE_5__app_component__["a" /* AppComponent */]]
        })
    ], AppModule);
    return AppModule;
}());



/***/ }),

/***/ "../../../../../src/app/components/fav-list/fav-list.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".panel-heading{\n    margin-right: 0px;\n    margin-left: 0px;\n}\n.scrollable{\n    overflow-x: auto;\n}\n.align-left{\n\tfloat: left;\n}\n.align-right{\n\tfloat: right;\n}\nimg{\n\theight: 10px;\n\twidth:10px;\n}\n\n.red{\n\tcolor: red;\n}\n\n.green{\n\tcolor: green;\n}\n\na:hover{\n\tcursor: pointer;\n}\n\n.row{\n\tmargin-left: 0px;\n\tmargin-right: 0px;\n}\n\n.favl{\n\tpadding-top: 6px;\n}", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/fav-list/fav-list.component.html":
/***/ (function(module, exports) {

module.exports = "<div id=\"fav-container\" (window:resize)=\"onResize($event)\">\n\t<div class=\"panel panel-default\">\n\t\t<div class=\"panel-heading row\">\n\t\t\t<div class=\"align-bottom align-left favl\"><b>Favorite List</b></div>\n\t\t\t<div class=\"fav-well align-right\">\n\t\t\t\t{{ renderToggle() }}\n\t\t\t\t<span *ngIf=\"isW()\"><b>Automatic Refresh:&nbsp;</b></span><input type=\"checkbox\"  data-toggle=\"toggle\" id=\"toggle\">\t\t \n\t\t\t\t<button type=\"button\" class=\"btn btn-default\" (click)=\"get_all_metas()\"><span class=\"glyphicon glyphicon-refresh\"></span></button>\n\t\t\t\t<button type=\"button\" class=\"btn btn-default\" [disabled]=\"isFirst\" (click)=\"switchToDetails()\"><span class=\"glyphicon glyphicon-chevron-right\"></span></button>\n\t\t\t</div>\n\t\t</div>\n\t\t<div class=\"panel-body\">\n\t\t\t<div class=\"row\">\n\t\t\t\t<div class=\"col-lg-2 col-sm-2\" style=\"margin-top: 5px;\"><b>Sort by</b></div>\n\t\t\t\t<div class=\"col-lg-3 col-sm-3\">\n\t\t\t\t\t<select class=\"form-control\" (change)=\"updateOrder($event)\" style=\"margin-bottom: 5px;\">\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='Default'\">Default</option>\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='Symbol'\">Symbol</option>\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='Stock Price'\">Stock Price</option>\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='Change'\">Change</option>\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='ChangePercent'\">Change Percent</option>\n\t\t\t\t\t\t<option [selected]=\"orderOpt=='Volume'\">Volume</option>\n\t\t\t\t\t</select>\n\t\t\t\t</div>  \t\t\t\t\t\n\t\t\t\t<div class=\"col-lg-1 col-sm-2\"  style=\"margin-top: 5px;\"><b>Order</b></div>\n\t\t\t\t<div class=\"col-lg-3 col-sm-3\" style=\"margin-bottom: 5px;\">\n\t\t\t\t\t<select class=\"form-control\" (change)=\"updateOrder($event)\">>\n\t\t\t\t\t\t<option [selected]=\"order=='Ascending'\">Ascending</option>\n\t\t\t\t\t\t<option [selected]=\"order=='Descending'\">Descending</option>\n\t\t\t\t\t</select>\n\t\t\t\t</div>\n\t\t\t\t<div class=\"col-sm-4\"></div>\n\t\t\t</div>\n\t\t\t<div class=\"scrollable\">\n\t\t\t\t<table class=\"table table-striped\">\n\t\t\t\t\t<thead>\n\t\t\t\t\t\t<tr>\n\t\t\t\t\t\t\t<th style=\"min-width: 70px;\">Symbol</th>\n\t\t\t\t\t\t\t<th style=\"min-width: 100px;\">Stock Price</th>\n\t\t\t\t\t\t\t<th style=\"min-width: 190px;\">Change (Change Percent)</th>\n\t\t\t\t\t\t\t<th style=\"min-width: 70px;\">Volume</th>\n\t\t\t\t\t\t</tr>\n\t\t\t\t\t</thead>\n\t\t\t\t\t<tbody>\n\t\t\t\t\t\t<tr *ngFor=\"let Symbol of favListDisplay;let i=index\" >\n\t\t\t\t\t\t\t<td><a (click)=\"getDetails(i)\">{{ Symbol.symbol }}</a></td>\n\t\t\t\t\t\t\t<td>{{ Symbol.lastDayClose | number:'1.2-2'}}</td>\n\t\t\t\t\t\t\t<td [ngClass]=\"{'red':(Symbol.change<0), 'green':(Symbol.change>0)}\">{{ Symbol.change | number:'1.2-2'}} ({{Symbol.changePercent| number:'1.2-2'}}%)\n\t\t\t\t\t\t\t\t<img src=\"http://cs-server.usc.edu:45678/hw/hw8/images/Up.png\" *ngIf=\"(Symbol.change>0)\">\n\t\t\t\t\t\t\t\t<img src=\"http://cs-server.usc.edu:45678/hw/hw8/images/Down.png\" *ngIf=\"(Symbol.change<0)\">\n\t\t\t\t\t\t\t</td>\n\t\t\t\t\t\t\t<td>{{ Symbol.lastDayVol | number:'1.0-2'}}</td>\n\t\t\t\t\t\t\t<td><button type=\"button\" class=\"btn btn-default\" (click)=\"delete(i)\"><span class=\"glyphicon glyphicon-trash\n\"></span></button></td>\n\t\t\t\t\t\t</tr>\n\t\t\t\t\t</tbody>\n\t\t\t\t</table>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</div>"

/***/ }),

/***/ "../../../../../src/app/components/fav-list/fav-list.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return FavListComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_common_http__ = __webpack_require__("../../../common/esm5/http.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_Rx__ = __webpack_require__("../../../../rxjs/_esm5/Rx.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__app_component__ = __webpack_require__("../../../../../src/app/app.component.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var FavListComponent = (function () {
    function FavListComponent(data, http, ref) {
        var _this = this;
        this.data = data;
        this.http = http;
        this.ref = ref;
        this.favList = Array();
        this.meta = {};
        this.numberOfTicks = 0;
        this.app = new __WEBPACK_IMPORTED_MODULE_4__app_component__["a" /* AppComponent */](this.http, this.data);
        setInterval(function () {
            _this.numberOfTicks++;
            _this.ref.markForCheck();
        }, 500);
    }
    FavListComponent.prototype.get_meta = function (i) {
        var _this = this;
        var params = new __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["c" /* HttpParams */]();
        var symbol = this.favList[i]['symbol'];
        params = params.append('symbol', symbol);
        this.http.get('/api/get_meta', { params: params }).subscribe(function (data) {
            if (data['error'] == true) {
            }
            else if (_this.favList[i] != null && _this.favList[i] != undefined && _this.favList[i]['symbol'] == symbol) {
                _this.favList[i]['change'] = data['change'];
                _this.favList[i]['changePercent'] = data['changePercent'];
                _this.favList[i]['lastDayClose'] = data['lastDayClose'];
                _this.favList[i]['lastDayVol'] = data['lastDayVol'];
                _this.data.updateFavList(_this.favList);
            }
        });
    };
    FavListComponent.prototype.renderToggle = function () {
        var self = this;
        var isOn = this.checked ? 'on' : 'off';
        $('#toggle').bootstrapToggle(isOn);
        $('#toggle').change(function () {
            var c = $(this).prop('checked');
            self.data.updateAuto(c);
        });
    };
    FavListComponent.prototype.get_all_metas = function () {
        for (var i = 0; i < this.favList.length; i++) {
            this.get_meta(i);
        }
    };
    FavListComponent.prototype.updateOrder = function (event) {
        if (event != null) {
            if (event.target.value == "Ascending" || event.target.value == "Descending")
                this.order = event.target.value;
            else
                this.orderOpt = event.target.value;
            this.data.updateOrder(this.orderOpt, this.order);
        }
        this.favListDisplay = new Array();
        for (var i = 0; i < this.favList.length; i++)
            this.favListDisplay.push(this.favList[i]);
        if (this.order == 'Ascending') {
            if (this.orderOpt == 'Default')
                this.favListDisplay = this.favListDisplay;
            else if (this.orderOpt == 'Stock Price')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.lastDayClose == b.lasDayClose) ? 0 : ((a.lastDayClose < b.lastDayClose) ? -1 : 1); });
            else if (this.orderOpt == 'Volume')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.lastDayVol == b.lastDayVol) ? 0 : ((a.lastDayVol < b.lastDayVol) ? -1 : 1); });
            else if (this.orderOpt == 'Symbol')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.symbol == b.symbol) ? 0 : ((a.symbol < b.symbol) ? -1 : 1); });
            else if (this.orderOpt == 'Change')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.change == b.change) ? 0 : ((a.change < b.change) ? -1 : 1); });
            else if (this.orderOpt == 'Change Percent')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.changePercent == b.changePercent) ? 0 : ((a.changePercent < b.changePercent) ? -1 : 1); });
        }
        else {
            if (this.orderOpt == 'Default')
                this.favListDisplay.reverse();
            else if (this.orderOpt == 'Stock Price')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.lastDayClose == b.lasDayClose) ? 0 : ((a.lastDayClose > b.lastDayClose) ? -1 : 1); });
            else if (this.orderOpt == 'Volume')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.lastDayVol == b.lastDayVol) ? 0 : ((a.lastDayVol > b.lastDayVol) ? -1 : 1); });
            else if (this.orderOpt == 'Symbol')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.symbol == b.symbol) ? 0 : ((a.symbol > b.symbol) ? -1 : 1); });
            else if (this.orderOpt == 'Change')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.change == b.change) ? 0 : ((a.change > b.change) ? -1 : 1); });
            else if (this.orderOpt == 'Change Percent')
                this.favListDisplay = this.favListDisplay.sort(function (a, b) { return (a.changePercent == b.changePercent) ? 0 : ((a.changePercent > b.changePercent) ? -1 : 1); });
        }
    };
    FavListComponent.prototype.switchToDetails = function () {
        this.isFav = false;
        this.isDetails = true;
        this.data.updateView(this.isFav, this.isDetails, false);
    };
    FavListComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.autoRefreshData();
        this.sub.unsubscribe();
        this.innerWidth = window.innerWidth;
        this.data.isFav.subscribe(function (updated) { return _this.isFav = updated; });
        this.data.isDetails.subscribe(function (updated) { return _this.isDetails = updated; });
        this.data.isFirst.subscribe(function (updated) { return _this.isFirst = updated; });
        this.data.orderOpt.subscribe(function (updated) { return _this.orderOpt = updated; });
        this.data.order.subscribe(function (updated) { return _this.order = updated; });
        this.data.autoRefresh.subscribe(function (updated) {
            if (_this.checked != updated) {
                if (updated) {
                    _this.autoRefreshData();
                }
                else {
                    _this.sub.unsubscribe();
                }
            }
            _this.checked = updated;
        });
        var local = localStorage.getItem('zhanpeng-fav-stock-list');
        if (local != null && local != undefined && this.favList.length <= 0) {
            this.favList = JSON.parse(local);
            this.data.updateFavList(this.favList);
            this.updateOrder(null);
        }
        this.data.favList.subscribe(function (updated) {
            _this.favList = updated;
            _this.updateOrder(null);
        });
    };
    FavListComponent.prototype.isW = function () {
        return (this.innerWidth >= 560);
    };
    FavListComponent.prototype.onResize = function (event) {
        this.innerWidth = window.innerWidth;
    };
    FavListComponent.prototype.delete = function (idx) {
        var sym = this.favListDisplay[idx]['symbol'];
        for (var i = 0; i < this.favList.length; i++) {
            if (this.favList[i]['symbol'] == sym) {
                this.favList.splice(i, 1);
                break;
            }
        }
        this.data.updateFavList(this.favList);
        this.updateOrder(null);
    };
    FavListComponent.prototype.refresh = function () {
        this.data.updateAuto(!this.checked);
    };
    FavListComponent.prototype.autoRefreshData = function () {
        var _this = this;
        this.sub = __WEBPACK_IMPORTED_MODULE_3_rxjs_Rx__["a" /* Observable */].interval(5000).subscribe(function (x) {
            _this.get_all_metas();
        });
    };
    FavListComponent.prototype.getDetails = function (i) {
        var symbol = this.favListDisplay[i]['symbol'];
        this.app.get_data_for_fav(symbol);
        this.switchToDetails();
    };
    FavListComponent.prototype.get_checked = function () {
        return this.data.get_checked();
    };
    FavListComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'fav-list',
            template: __webpack_require__("../../../../../src/app/components/fav-list/fav-list.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/fav-list/fav-list.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_2__services_data_service__["a" /* DataService */], __WEBPACK_IMPORTED_MODULE_1__angular_common_http__["a" /* HttpClient */], __WEBPACK_IMPORTED_MODULE_0__angular_core__["j" /* ChangeDetectorRef */]])
    ], FavListComponent);
    return FavListComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/details/details.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "img{\n\theight: 10px;\n\twidth:10px;\n}\n.red{\n\tcolor: red;\n}\n\n.green{\n\tcolor:green;\n}", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/details/details.component.html":
/***/ (function(module, exports) {

module.exports = "<table class=\"table table-striped\">\n\t<tr>\n\t\t<td><b>Stock Ticker Symbol</b></td>\n\t\t<td>{{ table.symbol }}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Last Price</b></td>\n\t\t<td>{{ table.lastDayClose| number:'1.2-2' }}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Change (Change Percent)</b></td>\n\t\t<td [ngClass]=\"{'red':(table.change<0), 'green':(table.change>0)}\">\n\t\t\t{{ table.change| number:'1.2-2' }} ({{table.changePercent| number:'1.2-2'}}%)\n\t\t\t<img src=\"http://cs-server.usc.edu:45678/hw/hw8/images/Up.png\" *ngIf=\"(table.change>0)\">\n\t\t\t<img src=\"http://cs-server.usc.edu:45678/hw/hw8/images/Down.png\" *ngIf=\"(table.change<0)\">\n\t\t</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Timestamp</b></td>\n\t\t<td>{{ table.lastDay }}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Open</b></td>\n\t\t<td>{{ table.lastDayOpen| number:'1.2-2' }}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Previous Close</b></td>\n\t\t<td>{{ table.prevClose| number:'1.2-2' }}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Day's Range</b></td>\n\t\t<td>{{table.lo|number:'1.2-2'}}&nbsp;-&nbsp;{{ table.hi|number:'1.2-2'}}</td>\n\t</tr>\n\t<tr>\n\t\t<td><b>Volume</b></td>\n\t\t<td>{{ table.lastDayVol| number:'1.0-2' }}</td>\n\t</tr>\n</table>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/details/details.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DetailsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var DetailsComponent = (function () {
    function DetailsComponent(data) {
        this.data = data;
    }
    DetailsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.data.done[0].subscribe(function (updated) { return _this.done = updated; });
        this.data.data[0].subscribe(function (table) { return _this.table = table; });
    };
    DetailsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-details',
            template: __webpack_require__("../../../../../src/app/components/stock-details/details/details.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/details/details.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_data_service__["a" /* DataService */]])
    ], DetailsComponent);
    return DetailsComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.html":
/***/ (function(module, exports) {

module.exports = "<div [@fadeInOut]=\"anmationState\" id=\"hi-chart-container\"></div>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HiChartsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_Highcharts___ = __webpack_require__("../../../../Highcharts/highcharts.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_Highcharts____default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_Highcharts___);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__ = __webpack_require__("../../../../highcharts/modules/exporting.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__angular_animations__ = __webpack_require__("../../../animations/esm5/animations.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





__WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__(__WEBPACK_IMPORTED_MODULE_2_Highcharts___);
var HiChartsComponent = (function () {
    function HiChartsComponent(data) {
        this.data = data;
        //chart: Chart;
        this.stockData = Array();
        this.doneWithLoading = Array();
        this.anmationState = false;
    }
    HiChartsComponent.prototype.renderChart = function () {
        if (this.doneWithLoading[this.currChart]) {
            __WEBPACK_IMPORTED_MODULE_2_Highcharts___["chart"]('hi-chart-container', this.stockData[this.currChart]);
            this.anmationState = !this.anmationState;
        }
        ;
    };
    HiChartsComponent.prototype.subscribeData = function (i) {
        var _this = this;
        this.data.data[i + 1].subscribe(function (updated) { return _this.stockData[i] = updated; });
        this.data.done[i + 1].subscribe(function (updated) { return _this.doneWithLoading[i] = updated; });
    };
    HiChartsComponent.prototype.ngOnInit = function () {
        var _this = this;
        for (var i = 0; i < 9; i++)
            this.subscribeData(i);
        this.data.chartSelected.subscribe(function (updated) {
            _this.currChart = updated;
            _this.data.updateChart(_this.stockData[_this.currChart]);
            _this.renderChart();
        });
    };
    HiChartsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-hi-charts',
            template: __webpack_require__("../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/hi-charts/hi-charts.component.css")],
            animations: [
                Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["k" /* trigger */])('fadeInOut', [
                    Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["j" /* transition */])('* => *', [
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ opacity: 0 }),
                        Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["e" /* animate */])(500, Object(__WEBPACK_IMPORTED_MODULE_4__angular_animations__["i" /* style */])({ opacity: 1 }))
                    ])
                ])
            ]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_data_service__["a" /* DataService */]])
    ], HiChartsComponent);
    return HiChartsComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/hist/hist.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/hist/hist.component.html":
/***/ (function(module, exports) {

module.exports = "<div id=\"hist-container\"></div>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/hist/hist.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return HistComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_highcharts_highstock__ = __webpack_require__("../../../../highcharts/highstock.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_highcharts_highstock___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_highcharts_highstock__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__ = __webpack_require__("../../../../highcharts/modules/exporting.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__);
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




__WEBPACK_IMPORTED_MODULE_3_highcharts_modules_exporting__(__WEBPACK_IMPORTED_MODULE_2_highcharts_highstock__);
var HistComponent = (function () {
    function HistComponent(data) {
        this.data = data;
    }
    HistComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.data.symbol.subscribe(function (updated) {
            _this.symbol = updated.toUpperCase();
        });
        this.data.isHist.subscribe(function (updated) { return _this.isHist = updated; });
        this.data.data[10].subscribe(function (updated) {
            _this.stock_data = updated;
        });
        this.data.done[10].subscribe(function (updated) {
            _this.done = updated;
            if (_this.done && _this.isHist) {
                __WEBPACK_IMPORTED_MODULE_2_highcharts_highstock__["stockChart"]("hist-container", {
                    title: {
                        text: _this.symbol + ' Stock Value'
                    },
                    subtitle: {
                        useHTML: true,
                        text: '<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
                        style: {
                            "color": "#0000FF"
                        }
                    },
                    rangeSelector: {
                        selected: 0,
                        buttons: [{
                                type: 'week',
                                count: 1,
                                text: '1w'
                            },
                            {
                                type: 'month',
                                count: 1,
                                text: '1m'
                            }, {
                                type: 'month',
                                count: 3,
                                text: '3m'
                            }, {
                                type: 'month',
                                count: 6,
                                text: '6m'
                            }, {
                                type: 'ytd',
                                text: 'YTD'
                            }, {
                                type: 'year',
                                count: 1,
                                text: '1y'
                            }, {
                                type: 'all',
                                text: 'All'
                            }]
                    },
                    series: [{
                            name: 'Stock Value',
                            data: _this.stock_data,
                            fillOpacity: 0.7,
                            color: "#9cbfe5",
                            lineColor: "#92a9c5",
                            type: 'area',
                            tooltip: {
                                valueDecimals: 2
                            }
                        }],
                    responsive: {
                        rules: [{
                                chartOptions: {
                                    rangeSelector: {
                                        inputEnabled: false
                                    }
                                },
                                condition: {
                                    maxWidth: 550
                                },
                            }]
                    },
                    tooltip: {
                        split: false,
                    },
                });
            }
        });
    };
    HistComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-hist',
            template: __webpack_require__("../../../../../src/app/components/stock-details/hist/hist.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/hist/hist.component.css")],
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_data_service__["a" /* DataService */]])
    ], HistComponent);
    return HistComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"progress\">\n  <div class=\"progress-bar progress-bar-striped active\" role=\"progressbar\"\n  aria-valuenow=\"50\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width:50%\">\n  </div>\n</div>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ProgressBarComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var ProgressBarComponent = (function () {
    function ProgressBarComponent() {
    }
    ProgressBarComponent.prototype.ngOnInit = function () {
    };
    ProgressBarComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-progress-bar',
            template: __webpack_require__("../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/progress-bar/progress-bar.component.css")]
        }),
        __metadata("design:paramtypes", [])
    ], ProgressBarComponent);
    return ProgressBarComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-details.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, ".panel-heading{\n    margin-right: 0px;\n    margin-left: 0px;\n}\n.preloaded{\n\tmargin-bottom: 10px;\n}\n#title{\n\ttext-align: center;\n}\n#line{\n    margin-top: 20px;\n    margin-bottom: 10px;\n    height:1px;\n    background-color: grey;\n    border-color: grey;\n}\n\nimg{\n\theight: 27px;\n\twidth: 27px;\n}\n\n.glyphicon-star{\n   color: #f9d556;\n   font-size: 20px;\n}\n.glyphicon-star-empty{\n    color: black;\n    font-size: 20px;\n}\n\n#backbutton{\n    position: absolute;\n    bottom: 0;\n}\n\n.align-left{\n    float: left;\n}\n.align-right{\n    float: right;\n}\n.table-progress{\n    margin-top: 70px;\n}\n.details-table{\n    margin-top: 35px;\n}\n.hi-charts-progress{\n    margin-top: 28px;\n}\n\nhand: hover{\n    cursor: pointer;\n}\n\n.error-hist{\n    margin-top: 40px;\n    margin-bottom: 40px;\n}\n\n.hi-chart-error{\n    margin-top: 20px;\n}\n", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-details.component.html":
/***/ (function(module, exports) {

module.exports = "<div class=\"panel panel-default\" (window:resize)=\"onResize($event)\">\n\t<div class=\"panel-heading row\">\n\t\t<div class=\"col-sm-3 col-xs-2\"><button type=\"button\" class=\"btn btn-default\" (click)=\"switchToFav()\"><span class=\"glyphicon glyphicon-chevron-left\"></span></button></div>\n\t\t<div class=\"col-sm-6 col-xs-8\" id=\"title\"><b>Stock Details</b></div>\n\t</div>\n\t<div class=\"panel-body\">\n\t\t<ul class=\"nav nav-pills\">\n\t\t\t<li role=\"presentation\" class=\"active responsive hand\" (click)=\"switchToCurr()\"><a data-toggle=\"tab\"><span class=\"glyphicon glyphicon-dashboard\"></span>&nbsp;{{ content[0] }}</a></li>\n\t\t\t<li role=\"presentation\" class=\"responsive hand\" (click)=\"switchToHist()\"><a data-toggle=\"tab\"><span class=\"glyphicon glyphicon-stats\"></span>&nbsp;{{ content[1] }}</a></li>\n\t\t\t<li role=\"presentation\" class=\"responsive hand\" (click)=\"switchToNews()\"><a data-toggle=\"tab\"><span class=\"glyphicon glyphicon-link\"></span>&nbsp;{{ content[2] }}</a></li>\n\t\t</ul>\n\n\t\t<div id=\"line\"></div>\n\n\t\t<div *ngIf=\"isCurr\">\n\t\t\t<div class=\"col-lg-6\">\n\t\t\t\t<div class=\"row\">\n\t\t\t\t\t<div class=\"col-lg-4 col-md-4 col-sm-6 col-xs-4\"><b>Stock Details</b></div>\n\t\t\t\t\t<div class=\"align-right\">\n\t\t\t\t\t\t<button class=\"btn btn-default\" (click)=\"updateFavorite()\" [disabled]=\"!done[0]||error[10]\"><span id=\"smallStarStar\" class=\"glyphicon\" [ngClass]=\"{'glyphicon-star-empty':(!isfound(symbol)),'glyphicon-star':(isfound(symbol))}\"></span></button>\n\t\t\t\t\t\t<button class=\"btn btn-default\" (click)=\"shareToFB()\" [disabled]=\"!done[currChart]||error[10]\"><img src=\"http://cs-server.usc.edu:45678/hw/hw8/images/facebook.png\"></button>\n\t\t\t\t\t</div>\n\t\t\t\t</div>\n\t\t\t\t<div *ngIf=\"!done[0]\" class=\"table-progress\"><app-progress-bar></app-progress-bar></div>\n\t\t\t\t<div *ngIf=\"done[0]\">\n\t\t\t\t\t<div *ngIf=\"error[0]\" class=\"alert alert-danger details-table\">{{errors[0]}}</div>\n\t\t\t\t\t<div *ngIf=\"!error[0]\" class=\"details-table\"><app-details></app-details></div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t\t<div class=\"col-lg-6\">\n\t\t\t\t<ul class=\"nav nav-tabs\">\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(1)}\" (click)=\"switchChart(0)\"><a data-toggle=\"tab\">Price</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(2)}\" (click)=\"switchChart(1)\"><a data-toggle=\"tab\">SMA</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(3)}\" (click)=\"switchChart(2)\"><a data-toggle=\"tab\">EMA</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(4)}\" (click)=\"switchChart(3)\"><a data-toggle=\"tab\">STOCH</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(5)}\" (click)=\"switchChart(4)\"><a data-toggle=\"tab\">RSI</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(6)}\"(click)=\"switchChart(5)\"><a data-toggle=\"tab\">ADX</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(7)}\" (click)=\"switchChart(6)\"><a data-toggle=\"tab\">CCI</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\"[ngClass]=\"{'active':isActive(8)}\" (click)=\"switchChart(7)\"><a data-toggle=\"tab\">BBANDS</a></li>\n\t\t\t\t\t<li role=\"presentation\" class=\"hand\" [ngClass]=\"{'active':isActive(9)}\" (click)=\"switchChart(8)\"><a data-toggle=\"tab\">MACD</a></li>\n\t\t\t\t</ul>\n\t\t\t\t<div *ngIf=\"!done[currChart]\" class=\"hi-charts-progress\"><app-progress-bar></app-progress-bar></div>\n\t\t\t\t<div *ngIf=\"done[currChart]\">\n\t\t\t\t\t<div *ngIf=\"error[currChart]\" class=\"alert alert-danger hi-chart-error\">{{errors[currChart]}}</div>\n\t\t\t\t\t<div *ngIf=\"!error[currChart]\"><app-hi-charts></app-hi-charts></div>\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t\t<div *ngIf=\"isHist\">\n\t\t\t<div *ngIf=\"!done[10]\" class=\"table-progress\"><app-progress-bar></app-progress-bar></div>\n\t\t\t<div *ngIf=\"done[10]\">\n\t\t\t\t<div *ngIf=\"error[10]\" class=\"alert alert-danger error-hist\">{{errors[10]}}</div>\n\t\t\t\t<div *ngIf=\"!error[10]\"><app-hist></app-hist></div>\n\t\t\t</div>\n\t\t</div>\n\t\t<div *ngIf=\"isNews\">\n\t\t\t<div *ngIf=\"!done[11]\" class=\"table-progress\"><app-progress-bar></app-progress-bar></div>\n\t\t\t<div *ngIf=\"done[11]\">\n\t\t\t\t<div *ngIf=\"error[11]\" class=\"alert alert-danger\">{{errors[11]}}</div>\n\t\t\t\t<div *ngIf=\"!error[11]\"><app-stock-news></app-stock-news></div>\n\t\t\t</div>\n\t\t</div>\n\t</div>\n</div>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-details.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockDetailsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_common_http__ = __webpack_require__("../../../common/esm5/http.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_ngx_facebook__ = __webpack_require__("../../../../ngx-facebook/dist/esm/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var StockDetailsComponent = (function () {
    function StockDetailsComponent(data, http, fb) {
        this.data = data;
        this.http = http;
        this.fb = fb;
        this.done = Array();
        this.error = Array();
        this.favList = Array();
        this.meta = Object();
        this.content = Array();
        fb.init({
            appId: '1454463421315931',
            version: 'v2.9'
        });
    }
    StockDetailsComponent.prototype.subscribeDone = function (i) {
        var _this = this;
        this.data.done[i].subscribe(function (updated) { _this.done[i] = updated; });
        this.data.error[i].subscribe(function (updated) { _this.error[i] = updated; });
    };
    StockDetailsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.errors = [
            'Error! Failed to get current stock data',
            'Error! Failed to get Price data',
            'Error! Failed to get SMA data',
            'Error! Failed to get EMA data',
            'Error! Failed to get STOCH data',
            'Error! Failed to get RSI data',
            'Error! Failed to get ADX data',
            'Error! Failed to get CCI data',
            'Error! Failed to get BBANDS data',
            'Error! Failed to get MACD data',
            'Error! Failed to get historical charts data',
            'Error! Failed to get news feed data'
        ];
        this.innerWidth = window.innerWidth;
        this.updateContent();
        this.isCurr = true;
        this.isHist = false;
        this.isNews = false;
        for (var i = 0; i < 12; i++)
            this.subscribeDone(i);
        this.data.chartSelected.subscribe(function (updated) { return _this.currChart = updated + 1; });
        this.data.meta.subscribe(function (updated) { return _this.meta = updated; });
        this.data.data[0].subscribe(function (updated) { return _this.tableData = updated; });
        this.data.symbol.subscribe(function (updated) { return _this.symbol = updated; });
        this.data.favList.subscribe(function (updated) { return _this.favList = updated; });
        this.data.isFav.subscribe(function (updated) { return _this.isFav = updated; });
        this.data.isDetails.subscribe(function (updated) { return _this.isDetails = updated; });
        this.data.chart.subscribe(function (updated) { return _this.toBeExport = updated; });
    };
    StockDetailsComponent.prototype.switchToCurr = function () {
        this.isHist = false;
        this.isNews = false;
        this.isCurr = true;
        this.data.updateIsHist(this.isHist);
    };
    StockDetailsComponent.prototype.switchToHist = function () {
        this.isCurr = false;
        this.isNews = false;
        this.isHist = true;
        this.data.updateIsHist(this.isHist);
    };
    StockDetailsComponent.prototype.switchToNews = function () {
        this.isCurr = false;
        this.isHist = false;
        this.isNews = true;
        this.data.updateIsHist(this.isHist);
    };
    StockDetailsComponent.prototype.switchChart = function (i) {
        this.currChart = i + 1;
        this.data.switchChart(this.currChart - 1);
    };
    StockDetailsComponent.prototype.find = function (target) {
        for (var i = 0; i < this.favList.length; i++) {
            if (this.favList[i]['symbol'] == target)
                return i;
        }
        return -1;
    };
    StockDetailsComponent.prototype.isfound = function (target) {
        return (this.find(target.toUpperCase()) >= 0);
    };
    StockDetailsComponent.prototype.updateFavorite = function () {
        if (this.symbol != "") {
            var found = this.find(this.symbol.toUpperCase());
            if (found != -1)
                this.favList.splice(found, 1);
            else {
                var meta_info = {
                    symbol: this.symbol.toUpperCase(),
                    lastDayClose: parseFloat(this.tableData['lastDayClose']),
                    change: parseFloat(this.tableData['change']),
                    changePercent: this.tableData['changePercent'],
                    lastDayVol: this.tableData['lastDayVol']
                };
                this.favList.push(meta_info);
            }
            this.data.updateFavList(this.favList);
        }
    };
    StockDetailsComponent.prototype.switchToFav = function () {
        this.data.updateView(true, false, false);
    };
    StockDetailsComponent.prototype.shareToFB = function () {
        var _this = this;
        var exportUrl = '/api/get_img';
        var body = {
            async: true,
            type: 'png',
            options: this.toBeExport
        };
        this.http.post(exportUrl, body, { responseType: 'text' }).subscribe(function (res) {
            var options = {
                method: 'share',
                href: 'http://export.highcharts.com/' + res
            };
            _this.fb.ui(options).then(function (res) {
                if (res && !res.error_message) {
                    window.alert("Posted Successfully");
                }
                else {
                    window.alert("Failed to post to Facebook");
                }
            }).catch(_this.handleError);
        });
    };
    StockDetailsComponent.prototype.isActive = function (i) {
        return (i === this.currChart);
    };
    StockDetailsComponent.prototype.handleError = function (error) {
        //console.error('Error processing action', error);
    };
    StockDetailsComponent.prototype.updateContent = function () {
        var temp = Array();
        if (this.innerWidth >= 670) {
            temp.push('Current Stock');
            temp.push('Historical Charts');
            temp.push('News Feeds');
        }
        else {
            temp.push('Stock');
            temp.push('Charts');
            temp.push('Feeds');
        }
        this.content = temp;
    };
    StockDetailsComponent.prototype.onResize = function (event) {
        this.innerWidth = event.target.innerWidth;
        this.updateContent();
    };
    StockDetailsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-stock-details',
            template: __webpack_require__("../../../../../src/app/components/stock-details/stock-details.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/stock-details.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_data_service__["a" /* DataService */], __WEBPACK_IMPORTED_MODULE_2__angular_common_http__["a" /* HttpClient */], __WEBPACK_IMPORTED_MODULE_3_ngx_facebook__["b" /* FacebookService */]])
    ], StockDetailsComponent);
    return StockDetailsComponent;
}());



/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-news/stock-news.component.css":
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__("../../../../css-loader/lib/css-base.js")(false);
// imports


// module
exports.push([module.i, "", ""]);

// exports


/*** EXPORTS FROM exports-loader ***/
module.exports = module.exports.toString();

/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-news/stock-news.component.html":
/***/ (function(module, exports) {

module.exports = "<div *ngIf=\"(newsData!=null)\">\n\t<div *ngFor=\"let eachNews of newsData\" class=\"jumbotron\">\n\t\t<div style=\"margin-bottom: 35px; font-size: 120%;\"><a href=\"{{ eachNews.link }}\" target=\"_blank\" >{{ eachNews.title }}</a></div>\n\t\t<div style=\"margin-bottom: 10px;\"><b>Author: {{ eachNews.author }}</b></div>\n\t\t<div><b>Date: {{ eachNews.pubDate }}</b></div>\t\n\t</div>\n</div>"

/***/ }),

/***/ "../../../../../src/app/components/stock-details/stock-news/stock-news.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return StockNewsComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__services_data_service__ = __webpack_require__("../../../../../src/app/services/data.service.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var StockNewsComponent = (function () {
    function StockNewsComponent(data) {
        this.data = data;
    }
    StockNewsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.data.news.subscribe(function (updated) {
            _this.newsData = updated;
            console.log(_this.newsData);
        });
    };
    StockNewsComponent = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["m" /* Component */])({
            selector: 'app-stock-news',
            template: __webpack_require__("../../../../../src/app/components/stock-details/stock-news/stock-news.component.html"),
            styles: [__webpack_require__("../../../../../src/app/components/stock-details/stock-news/stock-news.component.css")]
        }),
        __metadata("design:paramtypes", [__WEBPACK_IMPORTED_MODULE_1__services_data_service__["a" /* DataService */]])
    ], StockNewsComponent);
    return StockNewsComponent;
}());



/***/ }),

/***/ "../../../../../src/app/services/data.service.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataService; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__ = __webpack_require__("../../../../rxjs/_esm5/BehaviorSubject.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var DataService = (function () {
    function DataService() {
        this.doneSource = Array();
        this.done = Array();
        this.chartSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](0);
        this.chartSelected = this.chartSource.asObservable();
        this.currChartSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](null);
        this.chart = this.currChartSource.asObservable();
        this.dataSources = Array();
        this.data = Array();
        this.errorSources = Array();
        this.error = Array();
        this.symbolSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]('');
        this.symbol = this.symbolSource.asObservable();
        this.favSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]([]);
        this.favList = this.favSource.asObservable();
        this.metaSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]({});
        this.meta = this.metaSource.asObservable();
        this.newsSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]([]);
        this.news = this.newsSource.asObservable();
        this.isFavSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](true);
        this.isFav = this.isFavSource.asObservable();
        this.isDetailsSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](false);
        this.isDetails = this.isDetailsSource.asObservable();
        this.isFirstSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](true);
        this.isFirst = this.isFirstSource.asObservable();
        this.orderOptSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]('Default');
        this.orderOpt = this.orderOptSource.asObservable();
        this.orderSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */]('Ascending');
        this.order = this.orderSource.asObservable();
        this.autoRefreshSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](false);
        this.autoRefresh = this.autoRefreshSource.asObservable();
        this.isHistSource = new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](false);
        this.isHist = this.isHistSource.asObservable();
        for (var i = 0; i < 12; i++) {
            this.dataSources.push(new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](null));
            this.doneSource.push(new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](null));
            this.data.push(this.dataSources[i].asObservable());
            this.done.push(this.doneSource[i].asObservable());
            this.errorSources.push(new __WEBPACK_IMPORTED_MODULE_1_rxjs_BehaviorSubject__["a" /* BehaviorSubject */](false));
            this.error.push(this.errorSources[i].asObservable());
        }
    }
    DataService.prototype.update = function (i, obj) {
        this.dataSources[i].next(obj);
    };
    DataService.prototype.doneWithDataTransfer = function (i, nextDone) {
        this.doneSource[i].next(nextDone);
    };
    DataService.prototype.switchChart = function (i) {
        this.chartSource.next(i);
    };
    DataService.prototype.updateSymbol = function (symbol) {
        this.symbolSource.next(symbol);
    };
    DataService.prototype.updateFavList = function (list) {
        this.favSource.next(list);
        localStorage.setItem('zhanpeng-fav-stock-list', JSON.stringify(list));
    };
    DataService.prototype.updateFavList2 = function (list) {
        this.favSource.next(list);
    };
    DataService.prototype.updateView = function (isFav, isDetail, isFirst) {
        this.isFavSource.next(isFav);
        this.isDetailsSource.next(isDetail);
        this.isFirstSource.next(isFirst);
    };
    DataService.prototype.updateNews = function (news) {
        this.newsSource.next(news);
    };
    DataService.prototype.updateChart = function (chart) {
        this.currChartSource.next(chart);
    };
    DataService.prototype.updateMeta = function (meta) {
        this.metaSource.next(meta);
    };
    DataService.prototype.updateOrder = function (oo, order) {
        this.orderOptSource.next(oo);
        this.orderSource.next(order);
    };
    DataService.prototype.updateError = function (i, err) {
        this.errorSources[i].next(err);
    };
    DataService.prototype.updateAuto = function (auto) {
        this.autoRefreshSource.next(auto);
    };
    DataService.prototype.get_checked = function () {
        return this.autoRefresh;
    };
    DataService.prototype.updateIsHist = function (isH) {
        this.isHistSource.next(isH);
    };
    DataService = __decorate([
        Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["z" /* Injectable */])(),
        __metadata("design:paramtypes", [])
    ], DataService);
    return DataService;
}());



/***/ }),

/***/ "../../../../../src/environments/environment.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return environment; });
// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
var environment = {
    production: false
};


/***/ }),

/***/ "../../../../../src/main.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/esm5/core.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__ = __webpack_require__("../../../platform-browser-dynamic/esm5/platform-browser-dynamic.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__app_app_module__ = __webpack_require__("../../../../../src/app/app.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__environments_environment__ = __webpack_require__("../../../../../src/environments/environment.ts");




if (__WEBPACK_IMPORTED_MODULE_3__environments_environment__["a" /* environment */].production) {
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["_12" /* enableProdMode */])();
}
Object(__WEBPACK_IMPORTED_MODULE_1__angular_platform_browser_dynamic__["a" /* platformBrowserDynamic */])().bootstrapModule(__WEBPACK_IMPORTED_MODULE_2__app_app_module__["a" /* AppModule */])
    .catch(function (err) { return console.log(err); });


/***/ }),

/***/ 0:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("../../../../../src/main.ts");


/***/ })

},[0]);
//# sourceMappingURL=main.bundle.js.map