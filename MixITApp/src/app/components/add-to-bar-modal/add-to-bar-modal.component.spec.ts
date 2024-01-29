import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddToBarModalComponent } from './add-to-bar-modal.component';

describe('AddToBarModalComponent', () => {
  let component: AddToBarModalComponent;
  let fixture: ComponentFixture<AddToBarModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddToBarModalComponent]
    });
    fixture = TestBed.createComponent(AddToBarModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
