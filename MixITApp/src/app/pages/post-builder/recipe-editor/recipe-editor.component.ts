import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PostBuilderModule} from "../shared/PostBuilderModule";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {PostData} from "../shared/postData";
import {PostBuilderService} from "../shared/postBuilder.service";
import {debounceTime} from "rxjs";

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
  @Input() postData!: PostData;
  private orderedSteps: [number, string][] = [];
  instructionsForm: FormGroup;
  steps: Map<number, string> = new Map();
  currentStepNumber: number = 1;

  constructor(private fb: FormBuilder, private postBuilderService: PostBuilderService) {
    this.instructionsForm = this.fb.group({
      currentStep: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  checkIfCanProceed(): void {
    this.canProceed = this.steps.size > 0;
  }

  initializePostData(){
    const currentPostData = this.postBuilderService.getCurrentPostData();
    if(currentPostData.recipe?.steps){
      this.steps = new Map(currentPostData.recipe.steps);
      this.checkIfCanProceed();
    }
  }

  ngOnInit(): void {
    this.instructionsForm.valueChanges.pipe(
      debounceTime(300)
    ).subscribe(() => {
      this.validateForm();
    });
    this.initializePostData();
  }

  validateForm(): void {

  }

  addStep(): void {
    const stepDescription = this.instructionsForm.get('currentStep')?.value.trim();
    if (stepDescription) {
      this.steps.set(this.currentStepNumber, stepDescription);
      this.currentStepNumber++;
      this.instructionsForm.get('currentStep')?.reset();
      this.checkIfCanProceed();
      this.updateOrderedSteps();
    }
    this.postBuilderService.updatePostData({
      recipe: {
        steps: this.steps
      }
    })
  }
  private updateOrderedSteps(): void {
    this.orderedSteps = Array.from(this.steps.entries()).sort((a, b) => a[0] - b[0]);
  }


  removeStep(stepNumber: number): void {
    this.steps.delete(stepNumber);
    this.checkIfCanProceed();
  }

  getOrderedSteps(): [number, string][] {
    return this.orderedSteps;
  }
  trackByFn(index: number, item: [number, string]): number {
    return item[0];
  }


  onClose(): void {
    this.close.emit();
  }

  onContinue(): void {
    this.postBuilderService.updatePostData({
      recipe: {
        steps: this.steps
      }
    });
    this.nextStep.emit({ steps: this.steps });
  }

  onPrevStep(): void {
    this.prevStep.emit();
  }
}
