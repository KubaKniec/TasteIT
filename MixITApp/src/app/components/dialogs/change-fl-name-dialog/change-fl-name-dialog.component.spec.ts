import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangeFlNameDialogComponent } from './change-fl-name-dialog.component';

describe('ChangeFlNameDialogComponent', () => {
  let component: ChangeFlNameDialogComponent;
  let fixture: ComponentFixture<ChangeFlNameDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ChangeFlNameDialogComponent]
    });
    fixture = TestBed.createComponent(ChangeFlNameDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
