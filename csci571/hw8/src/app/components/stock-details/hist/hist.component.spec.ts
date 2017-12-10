import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistComponent } from './hist.component';

describe('HistComponent', () => {
  let component: HistComponent;
  let fixture: ComponentFixture<HistComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
