import {ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {IComponentFactory} from "../../helpers/IComponentFactory";
import {InstallAppModalComponent} from "../../components/install-app-modal/install-app-modal.component";

@Injectable({
  providedIn: 'root'
})
export class InstallAppModalFactoryService implements IComponentFactory<InstallAppModalComponent>{
  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(): ComponentRef<InstallAppModalComponent> {
    return this.rootViewContainer.createComponent(InstallAppModalComponent);
  }

  removeDynamicComponent(componentRef: ComponentRef<InstallAppModalComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }


}
