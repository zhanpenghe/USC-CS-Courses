import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FavListComponent } from './fav-list.component';

describe('FavListComponent', () => {
  let component: FavListComponent;
  let fixture: ComponentFixture<FavListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FavListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FavListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
