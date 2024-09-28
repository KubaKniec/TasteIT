import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodlistsComponent } from './foodlists.component';

describe('FoodlistsComponent', () => {
  let component: FoodlistsComponent;
  let fixture: ComponentFixture<FoodlistsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FoodlistsComponent]
    });
    fixture = TestBed.createComponent(FoodlistsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
