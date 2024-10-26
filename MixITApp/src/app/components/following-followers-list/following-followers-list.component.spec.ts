import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FollowingFollowersListComponent } from './following-followers-list.component';

describe('FollowingFollowersListComponent', () => {
  let component: FollowingFollowersListComponent;
  let fixture: ComponentFixture<FollowingFollowersListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FollowingFollowersListComponent]
    });
    fixture = TestBed.createComponent(FollowingFollowersListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
