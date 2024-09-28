import {IComponentFactory} from "../../helpers/IComponentFactory";
import {ComponentRef, ViewContainerRef} from "@angular/core";
import {CommentsSectionComponent} from "../../components/comments-section/comments-section.component";

export class CommentsSectionFactoryService implements IComponentFactory<CommentsSectionComponent>{

  private rootViewContainer!: ViewContainerRef;
  addDynamicComponent(postId: string): ComponentRef<CommentsSectionComponent> {
    const componentRef = this.rootViewContainer.createComponent(CommentsSectionComponent);
    componentRef.instance.postId = postId;
    return componentRef;
  }

  removeDynamicComponent(componentRef: ComponentRef<CommentsSectionComponent>): void {
    this.rootViewContainer.remove(this.rootViewContainer.indexOf(componentRef.hostView));
  }

  setRootViewContainerRef(viewContainerRef: ViewContainerRef): void {
    this.rootViewContainer = viewContainerRef;
  }

}
