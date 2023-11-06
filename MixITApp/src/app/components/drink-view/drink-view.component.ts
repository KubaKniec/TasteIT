import {Component, OnDestroy, OnInit, ViewContainerRef} from '@angular/core';
import {Drink} from "../../model/Drink";
import {ActivatedRoute} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {DemoService} from "../../service/DemoService";
import {InstructionsFactoryService} from "../../service/InstructionsFactoryService";
import {BodyScrollService} from "../../service/BodyScrollService";
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
             private demoService: DemoService,
             private instructionsFactoryService: InstructionsFactoryService,
             private viewContainerRef: ViewContainerRef,
             private bodyScrollService: BodyScrollService
 ){
   this.instructionsFactoryService.setRootViewContainerRef(this.viewContainerRef);
 }

  async ngOnInit(): Promise<void> {
    this.bodyScrollService.disableScroll();
    this.drinkId = this.route.snapshot.params['id'];
    try {
      this.activeDrink = await this.demoService.getDrinkById(this.drinkId)
    } catch (e) {
      this.toast.error("Drink not found or backend is down")
    }
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
}
