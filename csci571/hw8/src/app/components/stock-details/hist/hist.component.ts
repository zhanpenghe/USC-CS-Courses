import { Component, OnInit } from '@angular/core';
import { DataService } from '../../../services/data.service';

declare var $: any;
import * as Highcharts from 'highcharts';
import * as Highstocks from 'highcharts/highstock';
import * as exporting from 'highcharts/modules/exporting';
exporting(Highstocks);

@Component({
  selector: 'app-hist',
  templateUrl: './hist.component.html',
  styleUrls: ['./hist.component.css'],
})
export class HistComponent implements OnInit {

  stock_data:Object;
  symbol: String;
  done: Boolean;
  isHist:Boolean;

  constructor(private data: DataService) {}

  ngOnInit() {
    this.data.symbol.subscribe(updated=>{
      this.symbol=updated.toUpperCase();
    });
    this.data.isHist.subscribe(updated=>this.isHist=updated);
    this.data.data[10].subscribe(updated=>{
      this.stock_data=updated;
    });
    this.data.done[10].subscribe(updated=>{
      this.done = updated;
      if(this.done && this.isHist){
        Highstocks.stockChart("hist-container", {
        title: {
          text: this.symbol+' Stock Value'
        },
        subtitle: {
          useHTML: true,
          text:'<a target="_blank" href="https://www.alphavantage.co/">Source: Alpha Vantage</a>',
          style:{
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
                data: this.stock_data,
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
  }

}
