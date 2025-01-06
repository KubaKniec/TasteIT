import {Ingredient} from "./Ingredient";
import {IngredientWrapper} from "./IngredientWrapper";

export interface Recipe{
  steps?: Map<number, string>;
  pictures?: Map<number, string>;
  ingredientsWithMeasurements?: Ingredient[];
}
