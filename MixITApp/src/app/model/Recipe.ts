import {Ingredient} from "./Ingredient";

export interface Recipe{
  recipe_id: string;
  steps: Map<number, string>;
  pictures: Map<number, string>;
  ingredients: Ingredient[];
}
