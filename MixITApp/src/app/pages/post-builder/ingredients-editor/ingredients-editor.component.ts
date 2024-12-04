import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {PostBuilderModule} from "../shared/PostBuilderModule";
import {debounceTime, distinctUntilChanged, Subject, switchMap} from "rxjs";
import {Ingredient} from "../../../model/post/Ingredient";
import {Measurement} from "../../../model/post/Measurement";
import {SearchService} from "../../../service/search.service";
import {IngredientService} from "../../../service/ingredient.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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

  searchQuery = new Subject<string>();
  suggestions: Ingredient[] = [];
  selectedIngredients: (Ingredient & { measurement: Measurement })[] = [];
  showNewIngredientForm = false;
  showMeasurementModal = false;

  units = ['ml', 'g', 'pcs', 'tbsp', 'tsp', 'cup']; // This should be fetched from the server ig
  newIngredientForm: FormGroup;
  measurementForm: FormGroup;
  currentIngredient: Ingredient | null = null;
  constructor(
    private searchService: SearchService,
    private ingredientService: IngredientService,
    private fb: FormBuilder
  ) {
    this.newIngredientForm = this.fb.group({
      name: ['', Validators.required],
      description: [''],
      type: [''],
      isAlcohol: [false],
      strength: [''],
    });

    this.measurementForm = this.fb.group({
      value: ['', Validators.required],
      unit: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.searchQuery.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => this.searchService.searchIngredients(query, 0, 5))
    ).subscribe(results => {
      this.suggestions = results;
    });
  }
  
  onSearch(event: Event) {
    const term = (event.target as HTMLInputElement).value;
    if (term.length >= 2) {
      this.searchQuery.next(term);
    } else {
      this.suggestions = [];
    }
  }

  selectIngredient(ingredient: Ingredient) {
    this.currentIngredient = ingredient;
    this.showMeasurementModal = true;
    this.suggestions = [];
  }

  addMeasurement() {
    if (this.measurementForm.valid && this.currentIngredient) {
      const ingredientWithMeasurement = {
        ...this.currentIngredient,
        measurement: this.measurementForm.value
      };
      this.selectedIngredients.push(ingredientWithMeasurement);
      this.showMeasurementModal = false;
      this.currentIngredient = null;
      this.measurementForm.reset();
      this.onNextStep();
    }
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
