import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinkViewComponent } from './drink-view.component';

describe('DrinkViewComponent', () => {
  let component: DrinkViewComponent;
  let fixture: ComponentFixture<DrinkViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrinkViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrinkViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
