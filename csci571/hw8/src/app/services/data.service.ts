import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';


@Injectable()
export class DataService {

  private doneSource = Array();
  done = Array();

  private chartSource = new BehaviorSubject<any>(0);
  chartSelected = this.chartSource.asObservable();

  private currChartSource = new BehaviorSubject<Object>(null);
  chart = this.currChartSource.asObservable();

  private dataSources = Array();
  data = Array();

  private errorSources = Array();
  error = Array();

  private symbolSource = new BehaviorSubject<String>('');
  symbol = this.symbolSource.asObservable();

  private favSource = new BehaviorSubject<Array<any>>([]);
  favList = this.favSource.asObservable();
  private metaSource = new BehaviorSubject<Object>({});
  meta = this.metaSource.asObservable();

  private newsSource = new BehaviorSubject<Array<Object>>([]);
  news = this.newsSource.asObservable();

  private isFavSource = new BehaviorSubject<Boolean>(true);
  isFav = this.isFavSource.asObservable();

  private isDetailsSource = new BehaviorSubject<Boolean>(false);
  isDetails = this.isDetailsSource.asObservable();

  private isFirstSource = new BehaviorSubject<Boolean>(true);
  isFirst = this.isFirstSource.asObservable();

  private orderOptSource = new BehaviorSubject<String>('Default');
  orderOpt = this.orderOptSource.asObservable();
  private orderSource = new BehaviorSubject<String>('Ascending');
  order = this.orderSource.asObservable();

  private autoRefreshSource = new BehaviorSubject<Boolean>(false);
  autoRefresh = this.autoRefreshSource.asObservable();

  private isHistSource = new BehaviorSubject<Boolean>(false);
  isHist = this.isHistSource.asObservable();

  constructor() {
    for(var i = 0; i<12; i++){
      this.dataSources.push(new BehaviorSubject<Object>(null));
      this.doneSource.push(new BehaviorSubject<Boolean>(null));
      this.data.push(this.dataSources[i].asObservable());
      this.done.push(this.doneSource[i].asObservable());
      this.errorSources.push(new BehaviorSubject<Boolean>(false));
      this.error.push(this.errorSources[i].asObservable());
    }
  }

  update(i, obj: Object){
    this.dataSources[i].next(obj);
  }

  doneWithDataTransfer(i, nextDone:Boolean){
  	this.doneSource[i].next(nextDone);
  }

  switchChart(i: any){
    this.chartSource.next(i);
  }

  updateSymbol(symbol: String){
    this.symbolSource.next(symbol);
  }

  updateFavList(list: Array<any>){
    this.favSource.next(list);
    localStorage.setItem('zhanpeng-fav-stock-list', JSON.stringify(list));
  }

  updateFavList2(list:Array<any>){
    this.favSource.next(list);
  }

  updateView(isFav: Boolean, isDetail: Boolean, isFirst: Boolean){
    this.isFavSource.next(isFav);
    this.isDetailsSource.next(isDetail);
    this.isFirstSource.next(isFirst);
  }

  updateNews(news: Array<Object>){
    this.newsSource.next(news);
  }

  updateChart(chart: Object){
    this.currChartSource.next(chart);
  }

  updateMeta(meta: Object){
    this.metaSource.next(meta);
  }

  updateOrder(oo: String, order: String){
    this.orderOptSource.next(oo);
    this.orderSource.next(order);
  }

  updateError(i, err){
    this.errorSources[i].next(err);
  }

  updateAuto(auto: Boolean){
    this.autoRefreshSource.next(auto);
  }

  get_checked(){
    return this.autoRefresh;
  }

  updateIsHist(isH: Boolean){
    this.isHistSource.next(isH);
  }
}
