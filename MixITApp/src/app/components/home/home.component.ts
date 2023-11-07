import {Component, OnInit} from '@angular/core';
import {Drink} from "../../model/Drink";
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {Router} from "@angular/router";
import {async} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  allDrinks: Drink[] = [];
  dailyDrink!: Drink;
  constructor(private publicDrinkService: PublicDrinkService, private router: Router) {
  }
  async ngOnInit(): Promise<void> {
    this.publicDrinkService.getAllDrinks().then((drinks) => {
      this.allDrinks = drinks;
    }).catch((error) => {
      console.log(error);
    })
    this.dailyDrink = await this.publicDrinkService.getDailyDrink();
  }
  gotoDrink(id: number){
    this.router.navigate([`/drink/${id}`]).then();
  }
}
