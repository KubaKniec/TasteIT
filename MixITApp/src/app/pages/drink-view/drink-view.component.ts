import {Component, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {Post} from "../../model/Post";
import {ActivatedRoute, Router} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {InstructionsFactoryService} from "../../service/factories/instructions-factory.service";
import {BodyScrollService} from "../../service/body-scroll.service";
import {IngredientViewFactoryService} from "../../service/factories/ingredient-view-factory.service";
import {NavigationService} from "../../service/navigation.service";
import {PostService} from "../../service/post.service";
@Component({
  selector: 'app-drink-view',
  templateUrl: './drink-view.component.html',
  styleUrls: ['./drink-view.component.css']
})
export class DrinkViewComponent implements OnInit{
 activeDrink!: Post;
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
      this.activeDrink = await this.postService.getPostById(this.drinkId)
    } catch (e) {
      this.toast.error("Post not found or server error");
      //TODO: Redirect to not found page
      await this.router.navigate(['/home']);
    }
    console.log(this.activeDrink)
  }
  addToFavorite(){
   // this.userService.getUser().then((user) => {
   //    this.userService.addDrinkToFavorite(this.drinkId).then(() => {
   //      this.toast.success("Added to favorite");
   //    }).catch((e) => {
   //      this.toast.error("You must be logged in to add to favorite");
   //    })
   // }).catch((e) => {
   //    this.toast.error("You must be logged in to add to favorite");
   // })
  }
  initializeInstructionsView(drink: Post){
   const componentRef = this.instructionsFactoryService.addDynamicComponent(drink);

   componentRef.instance.close.subscribe(() => {
     this.instructionsFactoryService.removeDynamicComponent(componentRef)
     this.bodyScrollService.enableScroll();
   });
  }
  initializeIngredientView(id: number){
    // this.publicIngredientsService.getById(id).then((ingredient) => {
    //   const componentRef = this.ingredientViewFactoryService.addDynamicComponent(ingredient);
    //   componentRef.instance.close.subscribe(() => {
    //     this.ingredientViewFactoryService.removeDynamicComponent(componentRef)
    //   });
    // }).catch((e) => {
    //   this.toast.error("Cannot fetch ingredient with id: "+id)
    // })
  }
}
