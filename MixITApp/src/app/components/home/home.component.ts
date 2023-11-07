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
  constructor(private publicDrinkService: PublicDrinkService, private router: Router) {
  }
  selectChip(chip: string): void {
    this.selectedChip = chip;
    if(this.selectedChip == undefined){
      this.selectedChip = 'popular';
    }
  }
  async ngOnInit(): Promise<void> {
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
