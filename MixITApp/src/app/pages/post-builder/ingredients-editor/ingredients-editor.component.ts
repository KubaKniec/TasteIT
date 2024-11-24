import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {PostBuilderModule} from "../shared/PostBuilderModule";

@Component({
  selector: 'app-ingredients-editor',
  templateUrl: './ingredients-editor.component.html',
  styleUrls: ['./ingredients-editor.component.css']
})
export class IngredientsEditorComponent implements PostBuilderModule, OnInit{
  @Output() close: EventEmitter<void> = new EventEmitter<void>();
  @Output() nextStep: EventEmitter<any> = new EventEmitter<any>();
  @Output() prevStep: EventEmitter<void> = new EventEmitter<void>();
  canProceed: boolean = false;

  ngOnInit(): void {
  }

  onClose(): void {
    this.close.emit();
  }
  onNextStep(): void {
    this.nextStep.emit();
  }
  onPrevStep(): void {
    this.prevStep.emit();
  }

}
