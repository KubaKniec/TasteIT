import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {
  DrinkBuilderConfigurationComponent
} from "../components/drink-builder-configuration/drink-builder-configuration.component";
import {IComponentFactory} from "../helpers/IComponentFactory";

@Injectable({
  providedIn: 'root'
})
export class DrinkBuilderConfigurationFactoryService implements IComponentFactory<DrinkBuilderConfigurationComponent>{
  private rootViewContainer!: ViewContainerRef;

  public setRootViewContainerRef(viewContainerRef: ViewContainerRef) {
    this.rootViewContainer = viewContainerRef;
  }
  public addDynamicComponent(ingredients: String[]) {
    const componentRef = this.rootViewContainer.createComponent(DrinkBuilderConfigurationComponent);
    componentRef.instance.ingredients = ingredients;
    return componentRef;
  }
  public removeDynamicComponent(componentRef: ComponentRef<DrinkBuilderConfigurationComponent>) {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }
}
