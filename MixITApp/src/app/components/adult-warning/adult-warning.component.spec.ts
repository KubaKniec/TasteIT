import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdultWarningComponent } from './adult-warning.component';

describe('AdultWarningComponent', () => {
  let component: AdultWarningComponent;
  let fixture: ComponentFixture<AdultWarningComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdultWarningComponent]
    });
    fixture = TestBed.createComponent(AdultWarningComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
