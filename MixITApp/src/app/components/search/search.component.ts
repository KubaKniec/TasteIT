import {Component, OnInit} from '@angular/core';
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {HotToastService} from "@ngneat/hot-toast";
import {Drink} from "../../model/Drink";
import {Router} from "@angular/router";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{
  foundDrinks: Drink[] = []
  query: string = ''
  isLoading: boolean = false;
  drinkCategories: string[] = [];
  glassTypes: string[] = [];
  visibleCategories: number = 6;
  constructor(private publicDrinkService: PublicDrinkService,
              private toast: HotToastService,
              private router: Router
              ) {}
  ngOnInit(): void {
    this.publicDrinkService.getAllCategories().then((drinkCategories) => {
      this.drinkCategories = drinkCategories;
    }).catch((error) => {
      this.toast.error(error.message);
    })
    this.publicDrinkService.getAllGlassTypes().then((glassTypes) => {
      this.glassTypes = glassTypes;
    }).catch((error) => {
      this.toast.error(error.message);
    })
  }
  searchDrink(query: string){
    this.isLoading = true;
    if(query === ''){
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
  handleCategoryClick(category: string) {
    this.router.navigate(['/category', category]).then();
  }

  showAllCategories() {
    this.visibleCategories = this.drinkCategories.length;
  }

  hideAllCategories() {
    this.visibleCategories = 6;
  }
}
