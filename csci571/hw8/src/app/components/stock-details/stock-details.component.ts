import { Component, OnInit } from '@angular/core';
import { DataService} from '../../services/data.service';
import {HttpClient, HttpClientModule, HttpParams} from '@angular/common/http';
import { FacebookService, UIResponse, UIParams, FBVideoComponent } from 'ngx-facebook';


@Component({
  selector: 'app-stock-details',
  templateUrl: './stock-details.component.html',
  styleUrls: ['./stock-details.component.css']
})
export class StockDetailsComponent implements OnInit {
 
  isCurr: Boolean;
  isHist: Boolean;
  isNews: Boolean;

  currChart:any;
  done=Array<Boolean>();
  error=Array<Boolean>();

  symbol: String;
  favList =  Array();
  toBeExport: Object;
  tableData:Object;
  meta = Object();
  innerWidth: number;
  isFav:Boolean;
  isDetails:Boolean;
  errors:Array<String>;

  content = Array<String>();

  constructor(private data: DataService, private http: HttpClient, private fb: FacebookService) {
    fb.init({
      appId: '1454463421315931',
      version: 'v2.9'
    });
  }

  subscribeDone(i){
     this.data.done[i].subscribe(updated=>{this.done[i]=updated;});
     this.data.error[i].subscribe(updated=>{this.error[i]=updated;});
  }

  ngOnInit() {

    this.errors=[
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

    for(var i = 0; i<12; i++) this.subscribeDone(i);
    this.data.chartSelected.subscribe(updated=>this.currChart=updated+1);
    this.data.meta.subscribe(updated=>this.meta = updated);
    this.data.data[0].subscribe(updated=>this.tableData = updated);
    this.data.symbol.subscribe(updated=>this.symbol = updated);
    this.data.favList.subscribe(updated=>this.favList = updated);
    this.data.isFav.subscribe(updated=>this.isFav = updated);
    this.data.isDetails.subscribe(updated=>this.isDetails = updated);
    this.data.chart.subscribe(updated=> this.toBeExport = updated);
  }

  switchToCurr(){
  	this.isHist = false;
  	this.isNews = false;
    this.isCurr = true;
    this.data.updateIsHist(this.isHist);
  }

  switchToHist(){
  	this.isCurr=false;
  	this.isNews = false;
    this.isHist = true;
    this.data.updateIsHist(this.isHist);
  }

  switchToNews(){
	  this.isCurr = false;
  	this.isHist = false;
  	this.isNews = true;
    this.data.updateIsHist(this.isHist);
  }

  switchChart(i){
    this.currChart = i+1;
    this.data.switchChart(this.currChart-1);
  }

  find(target){
    for(var i = 0; i<this.favList.length; i++){
      if(this.favList[i]['symbol']==target) return i;
    }
    return -1;
  }

  isfound(target){
    return (this.find(target.toUpperCase())>=0);
  }

  updateFavorite(){
    if(this.symbol!=""){
      var found = this.find(this.symbol.toUpperCase());
      if(found!=-1) this.favList.splice(found, 1);
      else{
        var meta_info = {
          symbol: this.symbol.toUpperCase(),
          lastDayClose: parseFloat(this.tableData['lastDayClose']),
          change: parseFloat(this.tableData['change']),
          changePercent: this.tableData['changePercent'],
          lastDayVol: this.tableData['lastDayVol']
        }
        this.favList.push(meta_info);
      }
      this.data.updateFavList(this.favList);
    }
  }

  switchToFav(){
    this.data.updateView(true, false, false);
  }

  shareToFB(){
    var exportUrl = '/api/get_img';
    var body = {
      async : true,
      type: 'png',
      options : this.toBeExport
    }
    this.http.post(exportUrl, body, {responseType: 'text'}).subscribe(res=>{
      const options: UIParams = {
        method: 'share',
        href: 'http://export.highcharts.com/'+res
      };
      this.fb.ui(options).then((res: UIResponse) => {
        if( res&& !res.error_message){
          window.alert("Posted Successfully");
        }else{
          window.alert("Failed to post to Facebook");
        }
        }).catch(this.handleError);
    });
   }

  isActive(i){
    return (i===this.currChart);
  }

  private handleError(error) {
    //console.error('Error processing action', error);
  }

  updateContent()
  {
    var temp = Array<String>();
    if(this.innerWidth>=670){
      temp.push('Current Stock');
      temp.push('Historical Charts');
      temp.push('News Feeds');
    }else{
      temp.push('Stock');
      temp.push('Charts');
      temp.push('Feeds');
    }
    this.content = temp;
  }

  onResize(event) {
    this.innerWidth = event.target.innerWidth;
    this.updateContent();
  }


}
