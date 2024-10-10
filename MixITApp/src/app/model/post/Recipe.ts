import {Ingredient} from "./Ingredient";
import {IngredientWrapper} from "./IngredientWrapper";

export interface Recipe{
  recipe_id: string;
  steps: Map<number, string>;
  pictures: string[];
  ingredientsWithMeasurements: Ingredient[];
}
