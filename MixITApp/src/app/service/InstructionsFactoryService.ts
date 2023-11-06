import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {InstructionsViewComponent} from "../components/instructions-view/instructions-view.component";
import {Drink} from "../model/Drink";

@Injectable({
  providedIn: 'root'
})
export class InstructionsFactoryService{
  private rootViewContainer!: ViewContainerRef;

  constructor() {
  }
  public setRootViewContainerRef(viewContainerRef: ViewContainerRef) {
    this.rootViewContainer = viewContainerRef;
  }
  public addDynamicComponent(drink: Drink) {
    const componentRef = this.rootViewContainer.createComponent(InstructionsViewComponent);
    componentRef.instance.drink = drink;
    return componentRef;
  }
  public removeDynamicComponent(componentRef: ComponentRef<InstructionsViewComponent>) {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }
}
