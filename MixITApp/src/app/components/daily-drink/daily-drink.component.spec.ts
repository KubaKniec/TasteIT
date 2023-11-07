import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DailyDrinkComponent } from './daily-drink.component';

describe('DailyDrinkComponent', () => {
  let component: DailyDrinkComponent;
  let fixture: ComponentFixture<DailyDrinkComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DailyDrinkComponent]
    });
    fixture = TestBed.createComponent(DailyDrinkComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
