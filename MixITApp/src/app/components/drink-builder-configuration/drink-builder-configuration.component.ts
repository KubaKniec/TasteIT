import {Component, EventEmitter, Input, Output, ViewContainerRef} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {DrinkBuilderResultsFactoryService} from "../../service/DrinkBuilderResultsFactoryService";
import {Filter} from "../../model/Filter";

@Component({
  selector: 'app-drink-builder-configuration',
  templateUrl: './drink-builder-configuration.component.html',
  styleUrls: ['./drink-builder-configuration.component.css'],
  animations: [
    trigger('dialog', [
      state('void', style({
        transform: 'scale3d(0.8, 0.8, 0.8)',
        opacity: 0
      })),
      state('enter', style({
        transform: 'scale3d(1, 1, 1)',
        opacity: 1
      })),
      transition('void => enter', [
        animate('300ms cubic-bezier(.8, -0.6, 0.2, 1.5)')
      ]),
      transition('enter => void', [
        animate('300ms cubic-bezier(.8, -0.6, 0.2, 1.5)')
      ])
    ])
  ]
})
export class DrinkBuilderConfigurationComponent {
  @Input() ingredients: String[] = [];
  @Output() additionalFilters: EventEmitter<any> | undefined;
  @Output() close = new EventEmitter<void>();
  state: string = 'enter'
  alcohol: boolean = false;
  constructor(private viewContainerRef: ViewContainerRef,
              private drinkBuilderResultsFactoryService: DrinkBuilderResultsFactoryService) {
    this.drinkBuilderResultsFactoryService.setRootViewContainerRef(this.viewContainerRef);
  }

  onClose(){
    this.state = 'void';
    setTimeout(()=>{
      this.close.emit()
    }, 200)
  }

  setAlcoholicFalse() {
    this.alcohol = false;
  }
  setAlcoholicTrue() {
    this.alcohol = true;
  }
  buildFilter(){
    let filter: Filter = {
      ingredients: this.ingredients,
      alcohol: this.alcohol
    }
    const componentRef = this.drinkBuilderResultsFactoryService.addDynamicComponent(filter);
    componentRef.instance.close.subscribe(() => {
      this.drinkBuilderResultsFactoryService.removeDynamicComponent(componentRef)
      this.onClose();
    });
  }
}
