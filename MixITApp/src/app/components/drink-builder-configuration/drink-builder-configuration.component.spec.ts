import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrinkBuilderConfigurationComponent } from './drink-builder-configuration.component';

describe('DrinkBuilderConfigurationComponent', () => {
  let component: DrinkBuilderConfigurationComponent;
  let fixture: ComponentFixture<DrinkBuilderConfigurationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DrinkBuilderConfigurationComponent]
    });
    fixture = TestBed.createComponent(DrinkBuilderConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
