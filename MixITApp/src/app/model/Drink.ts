import {Ingredient} from "./Ingredient";

export interface Drink{
  idDrink: number;
  apiId: number;
  name: string;
  instructions?: string[];
  glassType: string;
  image: string;
  category: string;
  alcoholic: boolean;
  ingredients?: Ingredient[];

}
