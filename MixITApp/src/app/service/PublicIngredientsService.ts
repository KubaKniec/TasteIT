import {Injectable} from "@angular/core";
import publicAPI from "../api/publicAPI";
import {IngredientObj} from "../model/IngredientObj";

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
  async getById(id: number): Promise<IngredientObj>{
    const response = await publicAPI.get("/ingredient/id", {params: {id: id}});
    if (response.status === 200) {
      return response.data as IngredientObj;
    }
    throw new Error("Error getting ingredient by id");
  }
}
