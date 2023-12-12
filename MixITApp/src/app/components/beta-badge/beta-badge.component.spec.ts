import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BetaBadgeComponent } from './beta-badge.component';

describe('BetaBadgeComponent', () => {
  let component: BetaBadgeComponent;
  let fixture: ComponentFixture<BetaBadgeComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BetaBadgeComponent]
    });
    fixture = TestBed.createComponent(BetaBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
