import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstructionsViewComponent } from './instructions-view.component';

describe('InstructionsViewComponent', () => {
  let component: InstructionsViewComponent;
  let fixture: ComponentFixture<InstructionsViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstructionsViewComponent]
    });
    fixture = TestBed.createComponent(InstructionsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
