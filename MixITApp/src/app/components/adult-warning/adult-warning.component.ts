import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-adult-warning',
  templateUrl: './adult-warning.component.html',
  styleUrls: ['./adult-warning.component.css']
})
export class AdultWarningComponent {
  confirmInput: string = '';
  isButtonDisabled: boolean = true;
  @Output() close: EventEmitter<void> = new EventEmitter<void>();

  checkConfirmation() {
    if (this.confirmInput === 'I agree') {
      console.log('Confirmed');
      this.isButtonDisabled = false;
    }else{
      this.isButtonDisabled = true;
    }
  }
  onClose() {
    this.close.emit();
  }
}
