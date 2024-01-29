import {IComponentFactory} from "../../helpers/IComponentFactory";
import {AddToBarModalComponent} from "../../components/add-to-bar-modal/add-to-bar-modal.component";
import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class AddToBarModalFactoryService implements IComponentFactory<AddToBarModalComponent>{
  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(drinkId: number): ComponentRef<AddToBarModalComponent> {
    const componentRef = this.rootViewContainer.createComponent(AddToBarModalComponent);
    componentRef.instance.drinkId = drinkId;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<AddToBarModalComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }

}
