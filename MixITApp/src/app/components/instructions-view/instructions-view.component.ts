import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { Post } from '../../model/post/Post';
import { Recipe } from '../../model/post/Recipe';
import {HAMMER_GESTURE_CONFIG, HammerGestureConfig } from '@angular/platform-browser';

@Component({
  selector: 'app-recipe-view',
  templateUrl: './instructions-view.component.html',
  styleUrls: ['./instructions-view.component.css'],
  animations: [
    trigger('slideAnimation', [
      state('void', style({
        transform: 'translateY(100%)'
      })),
      state('visible', style({
        transform: 'translateY(0)'
      })),
      transition('void => visible', animate('300ms ease-out')),
      transition('visible => void', animate('300ms ease-in'))
    ]),
    trigger('contentAnimation', [
      transition(':increment', [
        style({ transform: 'translateX(100%)' }),
        animate('300ms ease-out', style({ transform: 'translateX(0)' }))
      ]),
      transition(':decrement', [
        style({ transform: 'translateX(-100%)' }),
        animate('300ms ease-out', style({ transform: 'translateX(0)' }))
      ])
    ])
  ],
  providers: [
    {
      provide: HAMMER_GESTURE_CONFIG,
      useClass: HammerGestureConfig
    }
  ]
})
export class InstructionsViewComponent implements OnInit{
  @Input() post!: Post;
  @Input() recipe!: Recipe;
  @Output() close = new EventEmitter<void>();

  currentStep = 0;
  currentState = 'visible';

  ngOnInit() {
    if (this.recipe?.steps) {
      this.recipe.steps = new Map<number, string>(
        Object.entries(this.recipe.steps).map(([key, value]) => [parseInt(key), value as string])
      );
    }
  }

  getProgressSteps(): number[] {
    const totalSteps = this.getTotalSteps();
    return totalSteps > 0 ? Array(totalSteps + 2).fill(0) : [0];
  }

  getTotalSteps(): number {
    return this.recipe?.steps?.size || 0;
  }

  getCurrentInstruction(): string {
    return this.recipe?.steps?.get(this.currentStep) || '';
  }

  nextStep() {
    if (this.currentStep <= this.getTotalSteps()) {
      this.currentStep++;
    }
  }

  previousStep() {
    if (this.currentStep > 0) {
      this.currentStep--;
    }
  }

  onSwipeLeft() {
    this.nextStep();
  }

  onSwipeRight() {
    this.previousStep();
  }

  onClose() {
    this.currentState = 'void';
    setTimeout(() => this.close.emit(), 300);
  }

  isLastStep(): boolean {
    return this.currentStep > this.getTotalSteps();
  }
}
