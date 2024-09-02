import { ComponentRef, Injectable, ViewContainerRef} from "@angular/core";
import {InstructionsViewComponent} from "../../components/instructions-view/instructions-view.component";
import {Post} from "../../model/Post";
import {IComponentFactory} from "../../helpers/IComponentFactory";
import {Recipe} from "../../model/Recipe";

@Injectable({
  providedIn: 'root'
})
export class InstructionsFactoryService implements IComponentFactory<InstructionsViewComponent>{
  private rootViewContainer!: ViewContainerRef;

  constructor() {
  }
  public setRootViewContainerRef(viewContainerRef: ViewContainerRef) {
    this.rootViewContainer = viewContainerRef;
  }
  public addDynamicComponent(post: Post, recipe: Recipe) {
    const componentRef = this.rootViewContainer.createComponent(InstructionsViewComponent);
    componentRef.instance.post = post;
    componentRef.instance.recipe = recipe;
    return componentRef;
  }
  public removeDynamicComponent(componentRef: ComponentRef<InstructionsViewComponent>) {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }
}
