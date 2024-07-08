import {Component, OnInit} from '@angular/core';
import {Drink} from "../../model/Drink";
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  allDrinks: Drink[] = [];
  dailyDrink!: Drink;
  popularDrinks: Drink[] = [];
  nonAlkDrinks: Drink[] = [];
  selectedChip: string = 'popular'
  greeting: string = ''
  constructor(private publicDrinkService: PublicDrinkService, private router: Router) {
  }
  selectChip(chip: string): void {
    this.selectedChip = chip;
    if(this.selectedChip == undefined){
      this.selectedChip = 'popular';
    }
  }
  async ngOnInit(): Promise<void> {
    this.greeting = this.getGreetingDependingOnTime();
    this.publicDrinkService.getAllDrinks().then((drinks) => {
      this.allDrinks = drinks;
    }).catch((error) => {
      console.log(error);
    })
    this.dailyDrink = await this.publicDrinkService.getDailyDrink();
    this.popularDrinks = await this.publicDrinkService.getPopularDrinks();
    this.nonAlkDrinks = await this.publicDrinkService.getFilteredDrinks( '',false, '');
  }
  gotoDrink(id: number){
    this.router.navigate([`/drink/${id}`]).then();
  }
  getGreetingDependingOnTime(): string {
    const currentHour = new Date().getHours();
    if (currentHour >= 5 && currentHour < 12) {
      return "Good Morning!";
    } else if (currentHour >= 12 && currentHour < 18) {
      return "Good Afternoon!";
    } else {
      return "Good Evening!";
    }
  }

    getIterableDrinkBasedOnSelectedChip(): Drink[] {
    if(this.selectedChip == 'noalc'){
      return this.nonAlkDrinks;
    }
    if(this.selectedChip == 'popular'){
      return this.popularDrinks;
    }
    if(this.selectedChip =='All'){
      return this.allDrinks;
    }
    return this.allDrinks;
   }
}
