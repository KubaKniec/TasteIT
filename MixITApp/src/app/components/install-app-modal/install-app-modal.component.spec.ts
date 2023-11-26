import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InstallAppModalComponent } from './install-app-modal.component';

describe('InstallAppModalComponent', () => {
  let component: InstallAppModalComponent;
  let fixture: ComponentFixture<InstallAppModalComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InstallAppModalComponent]
    });
    fixture = TestBed.createComponent(InstallAppModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
