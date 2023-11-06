import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Drink} from "../../model/Drink";
import {BodyScrollService} from "../../service/BodyScrollService";
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
  @Input() drink!: Drink;
  @Output() close = new EventEmitter<void>();
  currentStep: number = 0;
  state = 'enter'
  isDone: boolean = false

  constructor(
    private bodyScrollService: BodyScrollService
  ) { }
  getTotalSteps(){
    return this.drink.instructions!.length;
  }
  isIngredientStep(){
    return this.currentStep === 0;
  }
  getCurrentInstruction(){
    return this.drink.instructions![this.currentStep - 1]
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
  }
}
