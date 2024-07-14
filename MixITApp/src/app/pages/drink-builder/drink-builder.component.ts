import {Component, OnInit} from '@angular/core';
import {PublicIngredientsService} from "../../service/PublicIngredientsService";
import {Filter} from "../../model/Filter";
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {Drink} from "../../model/Drink";

@Component({
  selector: 'app-drink-builder',
  templateUrl: './drink-builder.component.html',
  styleUrls: ['./drink-builder.component.css']
})
export class DrinkBuilderComponent implements OnInit{
  constructor(
    private publicIngredientsService: PublicIngredientsService,
    private publicDrinkService: PublicDrinkService){}
  ingredients: String[] = [];
  filteredIngredients: String[] = [];
  searchPhrase: String = "";
  selectedIngredients: String[] = [];
  flexibleMatching: boolean = false;
  allowAlcohol: boolean = true;
  generatedDrinks: Drink[] = []
  ngOnInit(): void {
    this.publicIngredientsService.getAllIngredientsNames().then((ingredients) => {
      this.ingredients = this.removeDuplicatesFromList(ingredients).sort((a, b) => a.toLowerCase().localeCompare(b.toLowerCase()));
      this.filteredIngredients = this.ingredients;
    }).catch((error) => {
      console.log(error);
    });
  }
  generateDrinkRequest(){
    if (this.selectedIngredients.length === 0) return;
    const filter: Filter = {
      ingredientNames: this.selectedIngredients.join(','),
      alcoholic: this.allowAlcohol,
      ...(this.flexibleMatching ? { minIngredientCount: 1 } : {}),
      matchType: this.flexibleMatching ? 'AT_LEAST' : 'ALL'
    }
    this.publicDrinkService.getGeneratedDrinks(filter).then((drinks) => {
      this.generatedDrinks = drinks;
    }).catch((error) => {
      console.log(error);
    })
  }
  removeDuplicatesFromList(list: String[]): String[] {
    const uniqueSet = new Set<String>();
    list.forEach((item) => {
      uniqueSet.add(item);
    });
    return Array.from(uniqueSet);
  }
  handleIngredientClick(ingredient: String) {
    this.searchPhrase = "";
    this.filterIngredientsByPhrase();
    if (this.selectedIngredients.includes(ingredient)) {
      this.selectedIngredients = this.selectedIngredients.filter((selectedIngredient) => selectedIngredient !== ingredient);
    } else {
      this.selectedIngredients.push(ingredient);
    }
    this.generateDrinkRequest();
  }

  filterIngredientsByPhrase() {
    this.filteredIngredients = this.ingredients.filter((ingredient) => ingredient.toLowerCase().includes(this.searchPhrase.toLowerCase()));
  }
}
