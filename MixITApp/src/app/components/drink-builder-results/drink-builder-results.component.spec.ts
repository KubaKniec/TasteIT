import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinkBuilderResultsComponent } from './drink-builder-results.component';

describe('DrinkBuilderResultsComponent', () => {
  let component: DrinkBuilderResultsComponent;
  let fixture: ComponentFixture<DrinkBuilderResultsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DrinkBuilderResultsComponent]
    });
    fixture = TestBed.createComponent(DrinkBuilderResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
