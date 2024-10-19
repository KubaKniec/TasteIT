import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostGridComponent } from './post-grid.component';

describe('PostGridComponent', () => {
  let component: PostGridComponent;
  let fixture: ComponentFixture<PostGridComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PostGridComponent]
    });
    fixture = TestBed.createComponent(PostGridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
