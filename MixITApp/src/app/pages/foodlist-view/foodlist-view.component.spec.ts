import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodlistViewComponent } from './foodlist-view.component';

describe('FoodlistViewComponent', () => {
  let component: FoodlistViewComponent;
  let fixture: ComponentFixture<FoodlistViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FoodlistViewComponent]
    });
    fixture = TestBed.createComponent(FoodlistViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
