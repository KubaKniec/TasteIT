import {ComponentRef, ViewContainerRef} from "@angular/core";

export interface IComponentFactory<T>{
  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void;
  addDynamicComponent(...args: any[]): ComponentRef<T>;
  removeDynamicComponent(componentRef: ComponentRef<T>): void;
}
