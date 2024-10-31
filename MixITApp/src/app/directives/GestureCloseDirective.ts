import 'hammerjs';
import { HAMMER_GESTURE_CONFIG, HammerGestureConfig } from "@angular/platform-browser";
import { Directive, ElementRef, EventEmitter, Injectable, NgZone, OnDestroy, OnInit, Output, Renderer2 } from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class CustomHammerConfig extends HammerGestureConfig {
  override overrides = {
    swipe: { direction: Hammer.DIRECTION_ALL },
    pan: { direction: Hammer.DIRECTION_ALL },
  };
}

@Directive({
  selector: '[appGestureClose]',
  providers: [{
    provide: HAMMER_GESTURE_CONFIG,
    useClass: CustomHammerConfig
  }]
})
export class GestureCloseDirective implements OnInit, OnDestroy {
  @Output() closeGesture = new EventEmitter<void>();

  private hammer!: HammerManager;
  private readonly DRAG_THRESHOLD = 100;
  private readonly SCALE_FACTOR = 1;
  private isDragging = false;
  private initialY = 0;
  private currentY = 0;
  private dragHandle!: HTMLElement;

  constructor(
    private element: ElementRef,
    private ngZone: NgZone,
    private renderer: Renderer2
  ) {}

  ngOnInit() {
    this.setDragHandle();
    this.initializeHammer();
  }

  private setDragHandle() {
    this.dragHandle = this.element.nativeElement.querySelector('.drag-handle');
    if (!this.dragHandle) {
      console.warn('.drag-handle class is required!');
    }
  }

  private initializeHammer() {
    this.hammer = new Hammer.Manager(this.element.nativeElement);

    const pan = new Hammer.Pan({ direction: Hammer.DIRECTION_ALL, threshold: 0 });
    this.hammer.add(pan);

    this.hammer.on('panstart', (ev) => {
      if (this.isEventFromDragHandle(ev)) {
        this.ngZone.run(() => {
          this.handlePanStart(ev);
        });
      }
    });

    this.hammer.on('panmove', (ev) => {
      if (this.isDragging) {
        this.ngZone.run(() => {
          this.handlePanMove(ev);
        });
      }
    });

    this.hammer.on('panend', (ev) => {
      if (this.isDragging) {
        this.ngZone.run(() => {
          this.handlePanEnd(ev);
        });
      }
    });
  }

  private isEventFromDragHandle(event: HammerInput): boolean {
    return this.dragHandle && (event.target === this.dragHandle || this.dragHandle.contains(event.target as Node));
  }

  private handlePanStart(event: HammerInput) {
    this.isDragging = true;
    this.initialY = 0;
    this.currentY = 0;

    this.element.nativeElement.style.transition = 'none';
  }

  private handlePanMove(event: HammerInput) {
    if (!this.isDragging) return;

    this.currentY = Math.max(0, event.deltaY);

    const progress = Math.min(Math.abs(this.currentY) / 600, 1);
    const scale = this.SCALE_FACTOR + (progress * (1 - this.SCALE_FACTOR));

    this.updateElementTransform(this.currentY, scale);

    const opacity = 1 - (progress * 0.6);
    this.element.nativeElement.style.opacity = opacity;
  }

  private handlePanEnd(event: HammerInput) {
    if (!this.isDragging) return;

    this.isDragging = false;
    const shouldClose = this.currentY > this.DRAG_THRESHOLD;

    this.element.nativeElement.style.transition = 'all 0.3s ease-out';

    if (shouldClose) {
      this.animateClose();
      setTimeout(() => {
        this.closeGesture.emit();
      }, 300);
    } else {
      this.animateReset();
    }
  }

  private updateElementTransform(translateY: number, scale: number) {
    this.element.nativeElement.style.transform =
      `translateY(${translateY}px) scale(${scale})`;
  }

  private animateClose() {
    const windowHeight = window.innerHeight;
    this.updateElementTransform(windowHeight, 0.8);
    this.element.nativeElement.style.opacity = '0';
  }

  private animateReset() {
    this.updateElementTransform(0, 1);
    this.element.nativeElement.style.opacity = '1';
  }

  ngOnDestroy() {
    if (this.hammer) {
      this.hammer.destroy();
    }
  }
}
