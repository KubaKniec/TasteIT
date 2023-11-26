import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-install-app-modal',
  templateUrl: './install-app-modal.component.html',
  styleUrls: ['./install-app-modal.component.css']
})
export class InstallAppModalComponent {
  @Output() close: EventEmitter<void> = new EventEmitter<void>();

}
