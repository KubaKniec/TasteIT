import { Component } from '@angular/core';
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {HotToastService} from "@ngneat/hot-toast";
import {Drink} from "../../model/Drink";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent {
  constructor(private publicDrinkService: PublicDrinkService,
              private toast: HotToastService
              ) {}
  foundDrinks: Drink[] = []
  query: string = ''
  isLoading: boolean = false;
  didComponentJustLoad: boolean = true;
  searchDrink(query: string){
    this.didComponentJustLoad = false;
    this.isLoading = true;
    if(query === '' || query.length < 2){
      this.isLoading = false;
      this.foundDrinks = [];
      return;
    }
    this.publicDrinkService.searchForDrinks(query).then((drinks) => {
      this.foundDrinks = drinks;
    }).catch((error) => {
      this.toast.error(error.message);
    }).finally(() => {
      this.isLoading = false
      }
    )
  }
}
