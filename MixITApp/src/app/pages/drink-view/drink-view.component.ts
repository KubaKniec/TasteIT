import {Component, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {Post} from "../../model/Post";
import {ActivatedRoute, Router} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {InstructionsFactoryService} from "../../service/factories/instructions-factory.service";
import {BodyScrollService} from "../../service/body-scroll.service";
import {IngredientViewFactoryService} from "../../service/factories/ingredient-view-factory.service";
import {NavigationService} from "../../service/navigation.service";
import {PostService} from "../../service/post.service";
import {Ingredient} from "../../model/Ingredient";
@Component({
  selector: 'app-drink-view',
  templateUrl: './drink-view.component.html',
  styleUrls: ['./drink-view.component.css']
})
export class DrinkViewComponent implements OnInit{
 activePost!: Post;
 drinkId!: string;
 constructor(private route: ActivatedRoute,
             private toast: HotToastService,
             private instructionsFactoryService: InstructionsFactoryService,
             private viewContainerRef: ViewContainerRef,
             private bodyScrollService: BodyScrollService,
             private ingredientViewFactoryService: IngredientViewFactoryService,
             public navigationService: NavigationService,
             private router: Router,
             private postService: PostService
 ){
   this.instructionsFactoryService.setRootViewContainerRef(this.viewContainerRef);
   this.ingredientViewFactoryService.setRootViewContainerRef(this.viewContainerRef);
 }

  async ngOnInit(): Promise<void> {
    this.drinkId = this.route.snapshot.params['id'] as string;
    try {
      this.activePost = await this.postService.getPostById(this.drinkId)
    } catch (e) {
      this.toast.error("Post not found or server error");
      //TODO: Redirect to not found page
      await this.router.navigate(['/home']);
    }
    console.log(this.activePost)
  }
  getIngredients(): Ingredient[]{
   return this.activePost.recipe?.ingredientsMeasurements.map((ingredientWrapper) => {
      return ingredientWrapper.ingredient;
   }) || [];
  }
  initializeInstructionsView(drink: Post){
   const componentRef = this.instructionsFactoryService.addDynamicComponent(drink);

   componentRef.instance.close.subscribe(() => {
     this.instructionsFactoryService.removeDynamicComponent(componentRef)
     this.bodyScrollService.enableScroll();
   });
  }
  initializeIngredientView(id: number){
   let ingredient = this.getIngredients().find((ingredient) => ingredient.ingredientId === id);
    if(!ingredient){
      this.toast.error("Cannot find ingredient with id: "+id)
      return;
    }
   const componentRef = this.ingredientViewFactoryService.addDynamicComponent(ingredient);
    componentRef.instance.close.subscribe(() => {
      this.ingredientViewFactoryService.removeDynamicComponent(componentRef)
    })

  }
}
