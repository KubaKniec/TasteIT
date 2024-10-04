import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {Post} from "../../model/post/Post";
import {ActivatedRoute, Router} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {InstructionsFactoryService} from "../../service/factories/instructions-factory.service";
import {BodyScrollService} from "../../service/body-scroll.service";
import {IngredientViewFactoryService} from "../../service/factories/ingredient-view-factory.service";
import {NavigationService} from "../../service/navigation.service";
import {PostService} from "../../service/post.service";
import {Ingredient} from "../../model/Ingredient";
import {Recipe} from "../../model/post/Recipe";
import {CommentsSectionFactoryService} from "../../service/factories/comments-section-factory.service";

@Component({
  selector: 'app-drink-view',
  templateUrl: './drink-view.component.html',
  styleUrls: ['./drink-view.component.css']
})
export class DrinkViewComponent implements OnInit{
 activePost!: Post;
 recipe!: Recipe;
 drinkId!: string;
 isLoaded: boolean = false;
 constructor(private route: ActivatedRoute,
             private toast: HotToastService,
             private instructionsFactoryService: InstructionsFactoryService,
             private viewContainerRef: ViewContainerRef,
             private bodyScrollService: BodyScrollService,
             private ingredientViewFactoryService: IngredientViewFactoryService,
             public navigationService: NavigationService,
             private router: Router,
             private postService: PostService,
             private commentsSectionFactoryService: CommentsSectionFactoryService,
 ){
   this.instructionsFactoryService.setRootViewContainerRef(this.viewContainerRef);
   this.ingredientViewFactoryService.setRootViewContainerRef(this.viewContainerRef);
    this.commentsSectionFactoryService.setRootViewContainerRef(this.viewContainerRef);
 }

  async ngOnInit(): Promise<void> {
    this.drinkId = this.route.snapshot.params['id'] as string;
    try {
      this.activePost = await this.postService.getPostById(this.drinkId)
      this.recipe = await this.getRecipe();
    } catch (e) {
      this.toast.error("Check your internet connection and try again");
      await this.router.navigate(['/home']);
    }
    this.isLoaded = true;
  }
   getIngredients(): Ingredient[] {
    return this.recipe.ingredientsMeasurements.map((ingredientWrapper) => {
      return ingredientWrapper.ingredient;
    }) || [];
  }
  refreshPost(){
    this.postService.getPostById(this.activePost.postId!).then((post) => {
      this.activePost = post;
    })
  }
  async getRecipe() {
    return await this.postService.getPostRecipe(this.activePost.postId!);
  }
  async initializeInstructionsView(post: Post) {
    const recipe = await this.getRecipe();
    const componentRef = this.instructionsFactoryService.addDynamicComponent(post, recipe);

    componentRef.instance.close.subscribe(() => {
      this.instructionsFactoryService.removeDynamicComponent(componentRef)
      this.bodyScrollService.enableScroll();
    });
  }
   initializeIngredientView(id: number) {
    let ingredient = this.getIngredients().find((ingredient) => ingredient.ingredientId === id);
    if (!ingredient) {
      this.toast.error("Cannot find ingredient with id: " + id)
      return;
    }
    const componentRef = this.ingredientViewFactoryService.addDynamicComponent(ingredient);
    componentRef.instance.close.subscribe(() => {
      this.ingredientViewFactoryService.removeDynamicComponent(componentRef)
    })
  }
  preventScroll(event: TouchEvent) {
    event.preventDefault();
  }
  initializeCommentSection(postId: string) {
   const componentRef = this.commentsSectionFactoryService.addDynamicComponent(postId);
   componentRef.instance.close.subscribe(() => {
      this.commentsSectionFactoryService.removeDynamicComponent(componentRef)
   })
    componentRef.instance.refreshPost.subscribe(() => {
      this.refreshPost();
    })
  }
}
