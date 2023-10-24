import {Ingredient} from "./Ingredient";

export interface Drink{
  id: number;
  name: string;
  ingredients: Ingredient[];
  instructions: string[];
  isAlcoholic: boolean;
  glassType: string;
  image: string;
  category: string;

}
