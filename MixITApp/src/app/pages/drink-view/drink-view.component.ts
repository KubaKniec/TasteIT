import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {Post} from "../../model/post/Post";
import {ActivatedRoute, Router} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {InstructionsFactoryService} from "../../service/factories/instructions-factory.service";
import {BodyScrollService} from "../../service/body-scroll.service";
import {IngredientViewFactoryService} from "../../service/factories/ingredient-view-factory.service";
import {NavigationService} from "../../service/navigation.service";
import {PostService} from "../../service/post.service";
import {Ingredient} from "../../model/post/Ingredient";
import {Recipe} from "../../model/post/Recipe";
import {CommentsSectionFactoryService} from "../../service/factories/comments-section-factory.service";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user/User";

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
 postAuthor: User = {};
 isPostLikedByCurrentUser: boolean = false;
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
             private userService: UserService
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
    this.postAuthor = await this.userService.getUserById(this.activePost.userId!)
    this.isPostLikedByCurrentUser = this.activePost.likedByCurrentUser || false;
  }
  gotoProfile(userId: string){
    this.router.navigate(['/user-profile', userId]).then();
  }

  getAuthorName(): string{
   return this.postAuthor.displayName || "Unknown"
  }

   getIngredients(): Ingredient[] {
    return this.recipe.ingredientsWithMeasurements.map((ingredient) => {
      return ingredient;
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

  async toggleLike(){
   if(this.isPostLikedByCurrentUser){
     await this.postService.unlikePost(this.activePost.postId!)
     await this.ngOnInit();
   }else{
      await this.postService.likePost(this.activePost.postId!)
      await this.ngOnInit();
   }
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
  // Nie wiem co to tu robi ale kiedys tego potrzebowalem to moze sie przyda
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
