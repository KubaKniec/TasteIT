import {ComponentRef, Injectable, ViewContainerRef} from '@angular/core';
import {IComponentFactory} from "../../helpers/IComponentFactory";
import {SplashScreenComponent} from "../../components/splash-screen/splash-screen.component";

@Injectable({
  providedIn: 'root'
})
export class SplashScreenFactoryService implements IComponentFactory<SplashScreenComponent>{

  private rootViewContainer!: ViewContainerRef;

  addDynamicComponent(title: string, content: any, actionButtonLabel: string, closeButtonLabel: string): ComponentRef<SplashScreenComponent> {
    const componentRef = this.rootViewContainer.createComponent(SplashScreenComponent);
    componentRef.instance.title = title;
    componentRef.instance.content = content;
    componentRef.instance.actionButtonLabel = actionButtonLabel;
    componentRef.instance.closeButtonLabel = closeButtonLabel;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<SplashScreenComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }
}
