import {Component, OnInit} from '@angular/core';
import {Filter} from "../../model/Filter";
import {Post} from "../../model/post/Post";
import {IngredientService} from "../../service/ingredient.service";
import {Ingredient} from "../../model/post/Ingredient";
import {CreatorService} from "../../service/creator.service";

@Component({
  selector: 'app-drink-builder',
  templateUrl: './drink-builder.component.html',
  styleUrls: ['./drink-builder.component.css']
})
export class DrinkBuilderComponent implements OnInit{
  constructor(
    private ingredientService: IngredientService,
    private creatorService: CreatorService
  ){}
  ingredients: Ingredient[] = [];
  filteredIngredients: Ingredient[] = [];
  searchPhrase: String = "";
  selectedIngredients: Ingredient[] = [];
  flexibleMatching: boolean = true;
  allowAlcohol: boolean = true;
  foundPost: Post[] = []
  ngOnInit(): void {
    this.ingredientService.getAll().subscribe({
      next: (ingredients: Ingredient[]) => {
        this.ingredients = this.removeDuplicatesFromList(ingredients)
          .sort((a: Ingredient, b: Ingredient) =>
            a.name.toLowerCase().localeCompare(b.name.toLowerCase())
          );
        this.filteredIngredients = this.ingredients;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
  generateDrinkRequest() {
    if (this.selectedIngredients.length === 0){
      this.foundPost = [];
      return;
    }
    const ingredientNames = this.selectedIngredients.map((ingredient) => ingredient.name);

    if (this.flexibleMatching) {
      this.creatorService.searchPostsWithAnyIngredient(ingredientNames).then((posts: Post[]) => {
        this.foundPost = posts;
      }).catch((error) => {
        console.log(error);
      });
    } else {
      this.creatorService.searchPostsWithAllIngredients(ingredientNames).then((posts: Post[]) => {
        this.foundPost = posts;
      }).catch((error) => {
        console.log(error);
      });
    }
  }
  removeDuplicatesFromList(ingredients: Ingredient[]): Ingredient[] {
    const uniqueMap = new Map<string, Ingredient>();

    ingredients.forEach((ingredient) => {
      if (!uniqueMap.has(ingredient.name.toLowerCase())) {
        uniqueMap.set(ingredient.name.toLowerCase(), ingredient);
      }
    });

    return Array.from(uniqueMap.values());
  }
  handleIngredientClick(ingredient: Ingredient) {
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
    this.filteredIngredients = this.ingredients.filter((ingredient) => ingredient.name.toLowerCase().includes(this.searchPhrase.toLowerCase()));
  }
}
