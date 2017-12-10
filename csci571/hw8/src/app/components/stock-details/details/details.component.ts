import { Component, OnInit } from '@angular/core';
import { DecimalPipe } from '@angular/common';
import { DataService } from '../../../services/data.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css']
})
export class DetailsComponent implements OnInit {

  table: Object;
  done: Boolean;

  constructor(private data: DataService) { }

  ngOnInit() {
  	this.data.done[0].subscribe(updated=>this.done = updated);
  	this.data.data[0].subscribe(table=> this.table = table);
  }

}
