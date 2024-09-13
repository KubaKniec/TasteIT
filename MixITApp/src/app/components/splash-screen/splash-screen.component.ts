import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {BodyScrollService} from "../../service/body-scroll.service";

@Component({
  selector: 'app-splash-screen',
  templateUrl: './splash-screen.component.html',
  styleUrls: ['./splash-screen.component.css'],
  animations: [
    trigger('dialog', [
      state('void',style({
        transform: 'scale3d(.3,.3,.3)',
        opacity: 0
      })),
      state('enter', style({
        transform: 'scale3d(1,1,1)',
        opacity: 1
      })),
      transition('* => *', animate('300ms cubic-bezier(.8, -0.6, 0.2, 1.5)'))
    ])
  ]
})
export class SplashScreenComponent implements OnInit, OnDestroy {
  @Output() close = new EventEmitter<void>();
  @Input() title!: string;
  @Input() content: { icon: string, subtitle: string, text: string }[] = [];
  @Input() actionButtonLabel!: string;
  @Input() closeButtonLabel!: string;
  @Output() actionButton = new EventEmitter<void>();
  state = 'enter'
constructor(private bodyScrollService: BodyScrollService) {
}
  onClose() {
    this.state = 'void';
    setTimeout(() => {
      this.close.emit()
    }, 200)
  }
  onActionButton() {
    this.actionButton.emit();
  }

  ngOnInit(): void {
    this.bodyScrollService.disableScroll();
    document.addEventListener('touchmove', this.preventScroll, { passive: false });
  }

  ngOnDestroy(): void {
    this.bodyScrollService.enableScroll();
    document.removeEventListener('touchmove', this.preventScroll);
  }
  preventScroll(event: TouchEvent) {
    event.preventDefault();
  }
}
