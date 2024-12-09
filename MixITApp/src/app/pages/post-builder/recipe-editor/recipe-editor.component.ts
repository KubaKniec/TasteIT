import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {PostBuilderModule} from "../shared/PostBuilderModule";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-recipe-editor',
  templateUrl: './recipe-editor.component.html',
  styleUrls: ['./recipe-editor.component.css']
})
export class RecipeEditorComponent implements PostBuilderModule, OnInit {
  @Output() close: EventEmitter<void> = new EventEmitter<void>();
  @Output() nextStep: EventEmitter<any> = new EventEmitter<any>();
  @Output() prevStep: EventEmitter<void> = new EventEmitter<void>();
  canProceed: boolean = false;

  instructionsForm: FormGroup;
  steps: Map<number, string> = new Map();
  currentStepNumber:number = 1;

  constructor(private fb: FormBuilder) {
    this.instructionsForm = this.fb.group({
      currentStep: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.instructionsForm.valueChanges.subscribe(() => {
      this.validateForm();
    });
  }

  validateForm(): void {
    this.canProceed = this.steps.size > 0 &&
      this.instructionsForm.valid;
  }

  addStep(): void {
    const stepDescription = this.instructionsForm.get('currentStep')?.value.trim();
    if (stepDescription) {
      this.steps.set(this.currentStepNumber, stepDescription);
      this.currentStepNumber++;
      this.instructionsForm.get('currentStep')?.reset();
      this.validateForm();
    }
  }

  removeStep(stepNumber: number): void {
    this.steps.delete(stepNumber);
    this.validateForm();
  }

  getOrderedSteps(): [number, string][] {
    return Array.from(this.steps.entries()).sort((a, b) => a[0] - b[0]);
  }

  onClose(): void {
    this.close.emit();
  }

  onContinue(): void {
    if (this.canProceed) {
      this.nextStep.emit({ steps: this.steps });
    }
  }

  onPrevStep(): void {
    this.prevStep.emit();
  }

}
