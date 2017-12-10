import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators, ReactiveFormsModule, FormsModule, FormBuilder} from '@angular/forms';
import {HttpClient, HttpClientModule, HttpParams} from '@angular/common/http';
import { DataService } from './services/data.service'

import { trigger, style, animate, transition } from '@angular/animations';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger(
      'enterAnimation', [
        transition('void => on', [
          style({transform: 'translateX(-100%)', opacity: 0}),
          animate('500ms', style({transform: 'translateX(0)', opacity: 1}))
        ])
      ]
    ),
    trigger(
      'enterAnimationDetails', [
        transition('void => *', [
          style({transform: 'translateX(100%)', opacity: 0}),
          animate('500ms', style({transform: 'translateX(0)', opacity: 1}))
        ])
      ]
    )
  ],
})

export class AppComponent {

  rForm: FormGroup;
  get: any;
  symbol: FormControl;

  //stock data
  stockData = Array();
  done = new Array<Boolean>();
  stockSymbol: String;
  news=Array<Boolean>();

  //controll 
  isFav: Boolean;
  isDetails: Boolean;
  animated='off';
  stockInput: string = '';

  autoCompleteContent=[];

  constructor(private http: HttpClient, private data: DataService){

  }

  public NoWhitespaceValidator(control: FormControl) {
    var valid = control.value.trim().length === 0;
    return !valid ? null : { 'whitespace': true }
  }

  ngOnInit(){
    this.isFav = true;
    this.isDetails = false;

    for(var i = 0; i<12;i++){
      this.stockData[i] = new Object;
      this.done[i] = false;
      this.data.data[i].subscribe(updated => this.stockData[i] = updated);
      this.data.done[i].subscribe(updated=>this.done[i] = updated);
    }
    
    this.data.symbol.subscribe(updated=>this.stockSymbol = updated);
    this.data.isFav.subscribe(updated=>this.isFav = updated);
    this.data.isDetails.subscribe(updated=>this.isDetails = updated);

  	this.symbol = new FormControl('', [Validators.required, this.NoWhitespaceValidator]);
  	this.rForm = new FormGroup({
  		symbol : this.symbol,
  	});
  }

  switchToDetails(){
    this.isFav = false;
    this.isDetails = true;
    this.animated = 'off';
  }

  switchToFavs(){
    this.isFav = true;
    this.isDetails = false;
    this.animated = 'on';
  }

  updateData(i){
    this.data.update(i, this.stockData[i]);
  }

  get_price(params){
    this.stockData[0] = null;
    this.stockData[1] = null;
    this.stockData[10] = null;
    this.http.get('/api/get_price', {params: params}).subscribe(data => {

        if(data['error']==true){
          this.stockData[0] = data;
          this.stockData[1] = data;
          this.stockData[10] = data;
          this.data.updateError(0, true);
          this.data.updateError(1, true);
          this.data.updateError(10, true);
        }
        else{
          this.stockData[0] = data['table_obj'];
          this.stockData[1] = data['hi_charts_obj'];
          this.stockData[10] = data['hi_stock_data'];
        }
        this.updateData(0);
        this.updateData(1);
        this.updateData(10);
        this.done[0] = true;
        this.done[1] = true;
        this.done[10] = true;
        this.data.doneWithDataTransfer(0, this.done[0]);
        this.data.doneWithDataTransfer(1, this.done[1]);
        this.data.doneWithDataTransfer(10, this.done[10]);
    });
  }

  get_sma(params){
    this.stockData[2] = null;
    this.http.get('/api/get_sma', {params: params}).subscribe(data => {

        this.stockData[2] = data;
        if(data['error']==true) this.data.updateError(2, true);
        this.updateData(2);
        this.done[2] = true; 
        this.data.doneWithDataTransfer(2, this.done[2]);
     });
  }

  get_ema(params){
    this.stockData[3] = null;
    this.http.get('/api/get_ema', {params: params}).subscribe(data => {
        this.stockData[3] = data;
        if(data['error']==true) this.data.updateError(3, true);
        this.updateData(3);
        this.done[3] = true; 
        this.data.doneWithDataTransfer(3, this.done[3]);
    });
  }

  get_stoch(params){
    this.stockData[4] = null;
    this.http.get('/api/get_stoch', {params: params}).subscribe(data => {
        this.stockData[4] = data;
        if(data['error']==true) this.data.updateError(4, true);
        this.updateData(4);
        this.done[4] = true; 
        this.data.doneWithDataTransfer(4, this.done[4]);
    });
  }

  get_rsi(params){
    this.stockData[5] = null;
    this.http.get('/api/get_rsi', {params: params}).subscribe(data => {
        this.stockData[5] = data;
        if(data['error']==true) this.data.updateError(5, true);
        this.updateData(5);
        this.done[5] = true; 
        this.data.doneWithDataTransfer(5, this.done[5]);
    });
  }

  get_adx(params){
    this.stockData[6] = null;
    this.http.get('/api/get_adx', {params: params}).subscribe(data => {
        this.stockData[6] = data;
        if(data['error']==true) this.data.updateError(6, true);
        this.updateData(6);
        this.done[6] = true; 
        this.data.doneWithDataTransfer(6,this.done[6]);
    });
  }

  get_cci(params){
    this.stockData[7] = null;
    this.http.get('/api/get_cci', {params: params}).subscribe(data => {
        this.stockData[7] = data;
        if(data['error']==true) this.data.updateError(7, true);
        this.updateData(7);
        this.done[7] = true; 
        this.data.doneWithDataTransfer(7, this.done[7]);
    });
  }

  get_bbands(params){
    this.stockData[8] = null;
    this.http.get('/api/get_bbands', {params: params}).subscribe(data => {
        this.stockData[8] = data;
        if(data['error']==true) this.data.updateError(8, true);
        this.updateData(8);
        this.done[8] = true; 
        this.data.doneWithDataTransfer(8, this.done[8]);
    });
  }

  get_macd(params){
    this.stockData[9] = null;
    this.http.get('/api/get_macd', {params: params}).subscribe(data=>{
        this.stockData[9] = data;
        if(data['error']==true) this.data.updateError(9, true);
        this.updateData(9);
        this.done[9] = true; 
        this.data.doneWithDataTransfer(9, this.done[9]);
    });
  }

  get_news(params){
    this.news = null;
    this.data.updateNews(this.news);
    this.http.get('/api/get_news', {params: params}).subscribe(data=>{
        this.news = Object.values(data);
        if(this.news[0] == true){
          this.data.updateError(11, true);
        }
        this.data.updateNews(this.news);
        this.data.doneWithDataTransfer(11, true);
    });
  }

  clear(){
    this.stockInput = '';
    this.data.updateView(true, false, true);
  }

  submit_form(get){
  	if(this.rForm.valid){

      for(var i = 0; i<12;i++){
        this.done[i] = false;
        this.data.doneWithDataTransfer(i, this.done[i]);
        this.data.updateError(i, false);
      }
  		this.stockSymbol = get.symbol;
      

  		let params = new HttpParams();
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

  	}else{
  		this.symbol.markAsTouched();
  	}
  }

  onKey(event: any) {
    if(event.key == 'ArrowUp'||event.key=='ArrowDown') return;
    this.autoCompleteContent = [];

    var temp = event.target.value;

    if(temp ==null || temp==undefined||temp=="") return;

    var params = new HttpParams();
    params = params.append('symbol', temp);

    this.http.get('/api/auto_complete', {params: params}).subscribe(data=>{
        this.autoCompleteContent = Object.values(data);
    });
  }

  get_data_for_fav(symbol){
      for(var i = 0; i<12;i++){
        this.done[i] = false;
        this.data.doneWithDataTransfer(i, this.done[i]);
        this.data.updateError(i, false);
      }
      this.stockSymbol = symbol;

      let params = new HttpParams();
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
  }


}
