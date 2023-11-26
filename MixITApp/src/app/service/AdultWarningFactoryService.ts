import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {IComponentFactory} from "../helpers/IComponentFactory";
import {AdultWarningComponent} from "../components/adult-warning/adult-warning.component";

@Injectable({
  providedIn: 'root'
})
export class AdultWarningFactoryService implements IComponentFactory<AdultWarningComponent>{
  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(): ComponentRef<AdultWarningComponent> {
    return this.rootViewContainer.createComponent(AdultWarningComponent);

  }

  removeDynamicComponent(componentRef: ComponentRef<AdultWarningComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }

}
