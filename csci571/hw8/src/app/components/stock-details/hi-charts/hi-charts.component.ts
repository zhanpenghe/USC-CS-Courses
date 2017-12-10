import { Component, OnInit } from '@angular/core';
import { DataService } from '../../../services/data.service'
import * as Highcharts from 'Highcharts/';
import * as exporting from 'highcharts/modules/exporting';
import { trigger, transition, style, animate} from '@angular/animations';

exporting(Highcharts);

@Component({
  selector: 'app-hi-charts',
  templateUrl: './hi-charts.component.html',
  styleUrls: ['./hi-charts.component.css'],
  animations: [
    trigger('fadeInOut', [
      transition('* => *', [
        style({ opacity: 0 }),
        animate(500, style({ opacity: 1 }))
      ])
    ])
  ]
})

export class HiChartsComponent implements OnInit {

  //chart: Chart;
  stockData=Array();
  doneWithLoading = Array();
  currChart:any;

  anmationState=false;

  constructor(private data: DataService) { }
  
  renderChart(){
    if(this.doneWithLoading[this.currChart]){
      Highcharts.chart('hi-chart-container', this.stockData[this.currChart]);
      this.anmationState = !this.anmationState;
    };
  }

  subscribeData(i){
    this.data.data[i+1].subscribe(updated =>this.stockData[i] = updated);
    this.data.done[i+1].subscribe(updated => this.doneWithLoading[i] = updated);
  }

  ngOnInit() {

    for(var i = 0; i<9; i++) this.subscribeData(i);

    this.data.chartSelected.subscribe(updated=>{
      this.currChart = updated;
      this.data.updateChart(this.stockData[this.currChart]);
      this.renderChart();
    });
  }



}
