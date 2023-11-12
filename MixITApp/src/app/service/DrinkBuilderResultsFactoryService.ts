import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {IComponentFactory} from "../helpers/IComponentFactory";
import {DrinkBuilderResultsComponent} from "../components/drink-builder-results/drink-builder-results.component";

@Injectable({
  providedIn: 'root'
})
export class DrinkBuilderResultsFactoryService implements IComponentFactory<DrinkBuilderResultsComponent>{
  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(filters: any): ComponentRef<DrinkBuilderResultsComponent> {
    const componentRef = this.rootViewContainer.createComponent(DrinkBuilderResultsComponent);
    componentRef.instance.filters = filters;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<DrinkBuilderResultsComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }


}
