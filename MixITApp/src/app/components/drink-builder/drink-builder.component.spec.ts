import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinkBuilderComponent } from './drink-builder.component';

describe('DrinkBuilderComponent', () => {
  let component: DrinkBuilderComponent;
  let fixture: ComponentFixture<DrinkBuilderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrinkBuilderComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrinkBuilderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
