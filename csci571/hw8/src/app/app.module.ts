import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations'
import { NgModule } from '@angular/core';
import {MatAutocompleteModule, MatFormFieldModule, MatInputModule} from '@angular/material/';
import { FacebookModule } from 'ngx-facebook';

import { AppComponent } from './app.component';

import {FormControl, FormGroup, Validators, ReactiveFormsModule, FormsModule, FormBuilder} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import { FavListComponent } from './components/fav-list/fav-list.component';
import { StockDetailsComponent } from './components/stock-details/stock-details.component';
import { DetailsComponent } from './components/stock-details/details/details.component';

import { DataService } from './services/data.service';
import { HiChartsComponent } from './components/stock-details/hi-charts/hi-charts.component';
import { ProgressBarComponent } from './components/stock-details/progress-bar/progress-bar.component';
import { StockNewsComponent } from './components/stock-details/stock-news/stock-news.component';
import { HistComponent } from './components/stock-details/hist/hist.component';

@NgModule({
  declarations: [
    AppComponent,
    FavListComponent,
    StockDetailsComponent,
    DetailsComponent,
    HiChartsComponent,
    ProgressBarComponent,
    StockNewsComponent,
    HistComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatAutocompleteModule,
    MatFormFieldModule,
    MatInputModule,
    BrowserAnimationsModule,
    FacebookModule.forRoot()
  ],
  providers: [
    DataService,
    //{provide: HIGHCHARTS_MODULES, useFactory: highchartsModules}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
