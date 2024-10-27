import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TagViewComponent } from './tag-view.component';

describe('TagViewComponent', () => {
  let component: TagViewComponent;
  let fixture: ComponentFixture<TagViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TagViewComponent]
    });
    fixture = TestBed.createComponent(TagViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
