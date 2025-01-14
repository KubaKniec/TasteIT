import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmPostDeletionComponent } from './confirm-post-deletion.component';

describe('ConfirmPostDeletionComponent', () => {
  let component: ConfirmPostDeletionComponent;
  let fixture: ComponentFixture<ConfirmPostDeletionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ConfirmPostDeletionComponent]
    });
    fixture = TestBed.createComponent(ConfirmPostDeletionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
