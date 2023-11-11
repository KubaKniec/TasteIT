import {Component, OnInit} from '@angular/core';
import {PublicIngredientsService} from "../../service/PublicIngredientsService";

@Component({
  selector: 'app-drink-builder',
  templateUrl: './drink-builder.component.html',
  styleUrls: ['./drink-builder.component.css']
})
export class DrinkBuilderComponent implements OnInit{
  constructor(
    private publicIngredientsService: PublicIngredientsService,

              ){}
  ingredients: String[] = [];
  filteredIngredients: String[] = [];
  searchPhrase: String = "";
  selectedIngredients: String[] = [];
  ngOnInit(): void {
    this.publicIngredientsService.getAllIngredientsNames().then((ingredients) => {
      this.ingredients = ingredients;
      this.ingredients = this.removeDuplicateIngredients(this.ingredients);
      this.filteredIngredients = ingredients;
    }).catch((error) => {
      console.log(error);
    });
  }
  removeDuplicateIngredients(ingredients: String[]): String[] {
    return ingredients.filter((ingredient, index) => ingredients.indexOf(ingredient) === index);
  }
  handleIngredientClick(ingredient: String) {
    if (this.selectedIngredients.includes(ingredient)) {
      this.selectedIngredients = this.selectedIngredients.filter((selectedIngredient) => selectedIngredient !== ingredient);
    } else {
      this.selectedIngredients.push(ingredient);
    }
  }

  filterIngredientsByPhrase() {
    this.filteredIngredients = this.ingredients.filter((ingredient) => ingredient.toLowerCase().includes(this.searchPhrase.toLowerCase()));
  }
}
