import {IComponentFactory} from "../../helpers/IComponentFactory";
import {IngredientViewComponent} from "../../components/ingredient-view/ingredient-view.component";
import {ComponentRef, ViewContainerRef} from "@angular/core";
import {IngredientObj} from "../../model/post/IngredientObj";
import {Ingredient} from "../../model/Ingredient";

export class IngredientViewFactoryService implements IComponentFactory<IngredientViewComponent>{

  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(ingredient: Ingredient): ComponentRef<IngredientViewComponent> {
    const componentRef = this.rootViewContainer.createComponent(IngredientViewComponent);
    componentRef.instance.ingredient = ingredient;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<IngredientViewComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }

}
