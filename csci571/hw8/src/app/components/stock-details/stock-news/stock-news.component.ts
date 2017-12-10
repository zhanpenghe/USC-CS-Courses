import { Component, OnInit } from '@angular/core';
import { DataService } from '../../../services/data.service';

@Component({
  selector: 'app-stock-news',
  templateUrl: './stock-news.component.html',
  styleUrls: ['./stock-news.component.css']
})
export class StockNewsComponent implements OnInit {

  newsData: Array<Object>;

  constructor(private data: DataService) { }

  ngOnInit() {
  	this.data.news.subscribe(updated =>{
  		this.newsData = updated;
  	});
  }

}
