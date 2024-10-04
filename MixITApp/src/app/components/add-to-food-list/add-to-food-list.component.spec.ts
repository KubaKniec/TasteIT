import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToFoodListComponent } from './add-to-food-list.component';

describe('AddToFoodListComponent', () => {
  let component: AddToFoodListComponent;
  let fixture: ComponentFixture<AddToFoodListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddToFoodListComponent]
    });
    fixture = TestBed.createComponent(AddToFoodListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
