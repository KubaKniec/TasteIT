import {Component, OnInit} from '@angular/core';
import {Drink} from "../../model/Drink";
import {ActivatedRoute} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {DemoService} from "../../service/DemoService";

@Component({
  selector: 'app-drink-view',
  templateUrl: './drink-view.component.html',
  styleUrls: ['./drink-view.component.css']
})
export class DrinkViewComponent implements OnInit{
 activeDrink!: Drink;
 drinkId!: number;

 constructor(private route: ActivatedRoute,
             private toast: HotToastService,
             private demoService: DemoService
 ){}

  async ngOnInit(): Promise<void> {
    this.drinkId = this.route.snapshot.params['id'];
    try {
      this.activeDrink = await this.demoService.getDrinkById(this.drinkId)
    } catch (e) {
      this.toast.error("Drink not found or backend is down")
    }
  }
}
