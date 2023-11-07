import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinksGridComponent } from './drinks-grid.component';

describe('DrinksGridComponent', () => {
  let component: DrinksGridComponent;
  let fixture: ComponentFixture<DrinksGridComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DrinksGridComponent]
    });
    fixture = TestBed.createComponent(DrinksGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
