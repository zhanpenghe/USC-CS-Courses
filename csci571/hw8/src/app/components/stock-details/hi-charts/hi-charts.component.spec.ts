import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HiChartsComponent } from './hi-charts.component';

describe('HiChartsComponent', () => {
  let component: HiChartsComponent;
  let fixture: ComponentFixture<HiChartsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HiChartsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HiChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
