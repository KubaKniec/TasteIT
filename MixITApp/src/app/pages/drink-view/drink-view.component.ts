import {Component, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {Drink} from "../../model/Drink";
import {ActivatedRoute} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {InstructionsFactoryService} from "../../service/factories/InstructionsFactoryService";
import {BodyScrollService} from "../../service/BodyScrollService";
import {PublicIngredientsService} from "../../service/PublicIngredientsService";
import {IngredientViewFactoryService} from "../../service/factories/IngredientViewFactoryService";
import {UserService} from "../../service/UserService";
import {AddToBarModalFactoryService} from "../../service/factories/AddToBarModalFactoryService";
@Component({
  selector: 'app-drink-view',
  templateUrl: './drink-view.component.html',
  styleUrls: ['./drink-view.component.css']
})
export class DrinkViewComponent implements OnInit, OnDestroy{
 activeDrink!: Drink;
 drinkId!: number;
 constructor(private route: ActivatedRoute,
             private toast: HotToastService,
             private publicDrinkService: PublicDrinkService,
             private instructionsFactoryService: InstructionsFactoryService,
             private viewContainerRef: ViewContainerRef,
             private bodyScrollService: BodyScrollService,
             private publicIngredientsService: PublicIngredientsService,
             private ingredientViewFactoryService: IngredientViewFactoryService,
             private userService: UserService,
             private addToBarModalFactoryService: AddToBarModalFactoryService,
 ){
   this.instructionsFactoryService.setRootViewContainerRef(this.viewContainerRef);
   this.ingredientViewFactoryService.setRootViewContainerRef(this.viewContainerRef);
   this.addToBarModalFactoryService.setRootViewContainerRef(this.viewContainerRef);
 }

  async ngOnInit(): Promise<void> {
    this.bodyScrollService.disableScroll();
    this.drinkId = this.route.snapshot.params['id'];
    try {
      this.activeDrink = await this.publicDrinkService.getDrinkById(this.drinkId)
    } catch (e) {
      this.toast.error("Drink not found or backend is down")
    }
  }
  addToFavorite(){
   this.userService.getUser().then((user) => {
      this.userService.addDrinkToFavorite(this.drinkId).then(() => {
        this.toast.success("Added to favorite");
      }).catch((e) => {
        this.toast.error("You must be logged in to add to favorite");
      })
   }).catch((e) => {
      this.toast.error("You must be logged in to add to favorite");
   })
  }

  ngOnDestroy(): void {
    this.bodyScrollService.enableScroll();
  }
  showTestToast() {
    this.toast.success("Test toast");
  }
  initializeInstructionsView(drink: Drink){
   const componentRef = this.instructionsFactoryService.addDynamicComponent(drink);

   componentRef.instance.close.subscribe(() => {
     this.instructionsFactoryService.removeDynamicComponent(componentRef)
   });
  }
  initializeIngredientView(id: number){
    this.publicIngredientsService.getById(id).then((ingredient) => {
      const componentRef = this.ingredientViewFactoryService.addDynamicComponent(ingredient);
      componentRef.instance.close.subscribe(() => {
        this.ingredientViewFactoryService.removeDynamicComponent(componentRef)
      });
    }).catch((e) => {
      this.toast.error("Cannot fetch ingredient with id: "+id)
    })
  }
  initializeAddToBarView(drinkId: number){
   this.userService.getUser().then((user) => {
     const componentRef = this.addToBarModalFactoryService.addDynamicComponent(drinkId);
     componentRef.instance.close.subscribe(() => {
       this.addToBarModalFactoryService.removeDynamicComponent(componentRef)
     });
   }).catch((e) => {
      this.toast.error("You must be logged in to add to bar");
   })
  }
}
