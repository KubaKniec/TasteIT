import {Injectable} from "@angular/core";
import publicAPI from "../api/publicAPI";

@Injectable({
  providedIn: 'root'
})
export class PublicIngredientsService{
  async getAllIngredientsNames(): Promise<String[]> {
    let ingredients: String[] = [];
    const response = await publicAPI.get("/ingredient/all");
    if (response.status === 200) {
      for (let ingredient of response.data) {
        ingredients.push(ingredient.name);
      }
      return ingredients;
    }
    throw new Error("Error getting all ingredients");
  }
}
