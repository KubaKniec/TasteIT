import {Like} from "./Like";
import {FoodList} from "./FoodList";
import {Badge} from "./Badge";
import {Tag} from "./Tag";
import {Authentication} from "./Authentication";

export interface User {
  user_id?: number;
  displayName?: string;
  email?: string;
  bio?: string;
  profilePicture?: string;
  createdAt?: Date;
  birthDate?: Date;
  likes?: Like[];
  foodLists?: FoodList[];
  following?: String[];
  followers?: String[];
  badges?: Badge[];
  preferences?: Tag[];
  authentication?: Authentication;
}

