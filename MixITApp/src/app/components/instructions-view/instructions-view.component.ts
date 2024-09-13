import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Post} from "../../model/Post";
import {BodyScrollService} from "../../service/body-scroll.service";
import {Recipe} from "../../model/Recipe";
import {Ingredient} from "../../model/Ingredient";
@Component({
  selector: 'app-instructions-view',
  templateUrl: './instructions-view.component.html',
  styleUrls: ['./instructions-view.component.css'],
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
export class InstructionsViewComponent implements OnInit, OnDestroy{
  @Input() post!: Post;
  @Input() recipe!: Recipe;
  @Output() close = new EventEmitter<void>();
  currentStep: number = 0;
  state = 'enter'
  isDone: boolean = false

  constructor(
    private bodyScrollService: BodyScrollService
  ) { }

  getIngredients(): Ingredient[]{
    return this.recipe.ingredientsMeasurements.map((ingredientWrapper) => {
      return ingredientWrapper.ingredient;
    }) || [];

  }
  getTotalSteps(): number{
    return this.recipe.steps.size || 0;
  }
  isIngredientStep(): boolean{
    return this.currentStep === 0;
  }
  getCurrentInstruction(): string{
    return this.recipe.steps.get(this.currentStep) || '';
  }
  nextStep(){
    if(this.currentStep < this.getTotalSteps()){
      this.currentStep++;
    }else{
      this.isDone = true;
    }
  }
  previousStep(){
    if(this.currentStep > 0){
      this.currentStep--;
    }
  }
  onClose(){
    this.state = 'void';
    setTimeout(()=>{
      this.close.emit()
    }, 200)
  }

  ngOnDestroy(): void {

  }


  ngOnInit(): void {
    this.bodyScrollService.disableScroll();
    this.recipe.steps = new Map<number, string>(
      Object.entries(this.recipe.steps).map(([key, value]) => [parseInt(key), value as string])
    );
  }
}
