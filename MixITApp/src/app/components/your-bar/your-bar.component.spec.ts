import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YourBarComponent } from './your-bar.component';

describe('YourBarComponent', () => {
  let component: YourBarComponent;
  let fixture: ComponentFixture<YourBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ YourBarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(YourBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
