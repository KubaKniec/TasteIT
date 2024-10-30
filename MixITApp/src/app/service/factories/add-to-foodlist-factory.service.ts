import {IComponentFactory} from "../../helpers/IComponentFactory";
import {AddToFoodListComponent} from "../../components/add-to-food-list/add-to-food-list.component";
import {ComponentRef, ViewContainerRef} from "@angular/core";

export class AddToFoodlistFactoryService implements IComponentFactory<AddToFoodListComponent>{
  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(userId: string, postId: string): ComponentRef<AddToFoodListComponent> {
    const componentRef = this.rootViewContainer.createComponent(AddToFoodListComponent);
    componentRef.instance.userId = userId;
    componentRef.instance.postId = postId;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<AddToFoodListComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }


}
