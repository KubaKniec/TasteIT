import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarViewComponent } from './bar-view.component';

describe('BarViewComponent', () => {
  let component: BarViewComponent;
  let fixture: ComponentFixture<BarViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BarViewComponent]
    });
    fixture = TestBed.createComponent(BarViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
