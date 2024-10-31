import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FoodlistGridComponent } from './foodlist-grid.component';

describe('FoodlistGridComponent', () => {
  let component: FoodlistGridComponent;
  let fixture: ComponentFixture<FoodlistGridComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FoodlistGridComponent]
    });
    fixture = TestBed.createComponent(FoodlistGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
