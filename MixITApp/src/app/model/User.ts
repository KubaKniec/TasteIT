import {Role} from "./Role";
import {Bar} from "./Bar";
import {Ingredient} from "./Ingredient";
import {Drink} from "./Drink";

export interface User {
  idUser?: number;
  username?: string;
  email?: string;
  password?: string;
  role?: Role;
  bars?: Bar[];
  ingredients?: Ingredient[];
  favouriteDrinks?: Drink[];
  token?: string;

}
