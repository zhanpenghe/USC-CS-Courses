import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import {HttpClient, HttpClientModule, HttpParams} from '@angular/common/http';
import { DataService} from '../../services/data.service';
import { Observable } from 'rxjs/Rx';
import { AppComponent } from '../../app.component';

declare var $:any;

@Component({
  selector: 'fav-list',
  templateUrl: './fav-list.component.html',
  styleUrls: ['./fav-list.component.css']
})
export class FavListComponent implements OnInit {

  isFav :Boolean;
  isDetails: Boolean;
  isFirst: Boolean;

  favList=Array<any>();
  meta={}; 
  favListDisplay: Array<any>;
  order:String;
  orderOpt:String;
  autoRefresh:any;
  innerWidth:number;
  sub: any;
  numberOfTicks = 0;
  checked: Boolean;
  app = new AppComponent(this.http, this.data);

  constructor(private data: DataService, private http: HttpClient, private ref: ChangeDetectorRef) {
      setInterval(() => {
      this.numberOfTicks++;
      this.ref.markForCheck();
    }, 500);
  }

  get_meta(i){
    let params = new HttpParams();
    var symbol = this.favList[i]['symbol'];
    params = params.append('symbol', symbol);
    this.http.get('/api/get_meta', {params: params}).subscribe(data => {
      if(data['error']==true){
      }else if(this.favList[i]!=null&&this.favList[i]!=undefined&&this.favList[i]['symbol']==symbol){
        this.favList[i]['change'] = data['change'];
        this.favList[i]['changePercent'] = data['changePercent'];
        this.favList[i]['lastDayClose'] = data['lastDayClose'];
        this.favList[i]['lastDayVol'] = data['lastDayVol'];
        this.data.updateFavList(this.favList);
      }
    });
  }

  renderToggle(){
    var self = this;
    var isOn = this.checked?'on':'off';
    $('#toggle').bootstrapToggle(isOn);
    $('#toggle').change(function() {
      var c = $(this).prop('checked');
      self.data.updateAuto(c);
    });

  }

  get_all_metas(){
    for(var i = 0; i<this.favList.length; i++){
      this.get_meta(i);
    }
  }

  updateOrder(event:any){
    if(event!=null){
      if(event.target.value=="Ascending"||event.target.value=="Descending") this.order = event.target.value;
      else this.orderOpt = event.target.value;
      this.data.updateOrder(this.orderOpt, this.order);
    }

    this.favListDisplay = new Array<any>();
    for(var i = 0; i<this.favList.length; i++) this.favListDisplay.push(this.favList[i]);
    if(this.order == 'Ascending'){
      if(this.orderOpt == 'Default') this.favListDisplay = this.favListDisplay;
      else if(this.orderOpt == 'Stock Price') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.lastDayClose == b.lasDayClose)?0:((a.lastDayClose<b.lastDayClose)?-1:1);});
      else if(this.orderOpt == 'Volume') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.lastDayVol == b.lastDayVol)?0:((a.lastDayVol<b.lastDayVol)?-1:1);});
      else if(this.orderOpt == 'Symbol') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.symbol == b.symbol)?0:((a.symbol<b.symbol)?-1:1);});
      else if(this.orderOpt == 'Change') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.change == b.change)?0:((a.change<b.change)?-1:1);});
      else if(this.orderOpt == 'Change Percent') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.changePercent == b.changePercent)?0:((a.changePercent<b.changePercent)?-1:1);});
    }else{
      if(this.orderOpt == 'Default') this.favListDisplay.reverse();
      else if(this.orderOpt == 'Stock Price') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.lastDayClose == b.lasDayClose)?0:((a.lastDayClose>b.lastDayClose)?-1:1);});
      else if(this.orderOpt == 'Volume') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.lastDayVol == b.lastDayVol)?0:((a.lastDayVol>b.lastDayVol)?-1:1);});
      else if(this.orderOpt == 'Symbol') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.symbol == b.symbol)?0:((a.symbol>b.symbol)?-1:1);});
      else if(this.orderOpt == 'Change') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.change == b.change)?0:((a.change>b.change)?-1:1);});
      else if(this.orderOpt == 'Change Percent') this.favListDisplay = this.favListDisplay.sort(function(a, b){return (a.changePercent == b.changePercent)?0:((a.changePercent>b.changePercent)?-1:1);});
    }

  }

  switchToDetails(){
    this.isFav = false;
    this.isDetails = true;
    this.data.updateView(this.isFav, this.isDetails, false);
  }

  ngOnInit() {
    this.autoRefreshData();
    this.sub.unsubscribe();
    this.innerWidth = window.innerWidth;
    this.data.isFav.subscribe(updated=>this.isFav = updated);
    this.data.isDetails.subscribe(updated=>this.isDetails = updated);
    this.data.isFirst.subscribe(updated=>this.isFirst = updated);
    this.data.orderOpt.subscribe(updated=>this.orderOpt = updated);
    this.data.order.subscribe(updated=>this.order=updated);
    this.data.autoRefresh.subscribe(updated=>{
      if(this.checked!=updated){
        if(updated){
          this.autoRefreshData();
        }else{
          this.sub.unsubscribe();
        }
      }
      this.checked = updated;
    });

  	var local = localStorage.getItem('zhanpeng-fav-stock-list');
  	if(local != null && local!=undefined && this.favList.length<=0){
  		this.favList = JSON.parse(local);
  		this.data.updateFavList(this.favList);
      this.updateOrder(null);
  	}
  	this.data.favList.subscribe(updated=>{
  		this.favList = updated;
      this.updateOrder(null);
  	});
  }

  isW(){
    return (this.innerWidth>=560);
  }

  onResize(event) {
    this.innerWidth = window.innerWidth;
  }

  delete(idx){
    var sym = this.favListDisplay[idx]['symbol'];
    for(var i = 0;i<this.favList.length;i++){
      if(this.favList[i]['symbol']==sym){
        this.favList.splice(i, 1);
        break;
      }
    }
    this.data.updateFavList(this.favList);
    this.updateOrder(null);
  }

  refresh(){
    this.data.updateAuto(!this.checked);
  }

  autoRefreshData(){
    this.sub = Observable.interval(5000).subscribe(x => {
      this.get_all_metas();
    });
  }

  getDetails(i){
    var symbol = this.favListDisplay[i]['symbol'];
    this.app.get_data_for_fav(symbol);
    this.switchToDetails();
  }

  get_checked(){
    return this.data.get_checked();
  }

}
