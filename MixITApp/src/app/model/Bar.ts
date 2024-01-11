import {Drink} from "./Drink";

export interface Bar {
  idBar: number;
  name: string;
  drinks: Drink[];
}
