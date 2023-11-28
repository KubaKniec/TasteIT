import {Injectable} from "@angular/core";
import {Drink} from "../model/Drink";
import publicAPI from "../api/publicAPI";
import {Filter} from "../model/Filter";


@Injectable({
    providedIn: 'root'
  })
export class PublicDrinkService {

  async getAllDrinks(){
    let drinks: Drink[] = [];
    const response = await publicAPI.get("/drink/all")
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting all drinks");
  }
  async getGeneratedDrinks(filter: Filter): Promise<Drink[]> {
    let drinks: Drink[] = [];
    const response = await publicAPI.get('/drink/filter/withIngredients', {
      params: {
        ingredientNames: filter.ingredients.join(','), //?
        alcoholic: filter.alcohol,
        matchType : 'ALL'
      }
    })
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting generated drinks");
  }
  async getDrinkById(id: number): Promise<Drink>{
    const response = await publicAPI.get(`/drink/${id}`);
    if(response.status === 200){
      return response.data;
    }
    throw new Error("Error getting drink by id");
  }
  async getDailyDrink(): Promise<Drink>{
    const response = await publicAPI.get("/drink/daily");
    if(response.status === 200){
      return response.data;
    }
    throw new Error("Error getting daily drink");
  }
  async getPopularDrinks(): Promise<Drink[]>{
    let drinks: Drink[] = [];
    const response = await publicAPI.get("/drink/popular");
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting popular drinks");
  }
  async getFilteredDrinks(category: string, alcoholic: boolean, glassType: string): Promise<Drink[]>{
    let drinks: Drink[] = [];
    let queryParams: any = {}
    if (category) queryParams.category = category;
    if (alcoholic !== null) queryParams.alcoholic = alcoholic;
    if (glassType) queryParams.glassType = glassType;
    const response = await publicAPI.get('/drink/filter',{
      params: queryParams
    })
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting filtered drinks");
  }
  async searchForDrinks(query: string): Promise<Drink[]>{
    let drinks: Drink[] = [];
    const response = await publicAPI.get('/drink/search',{
      params: {
        query: query
      }
    })
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting search drinks");
  }
  async getAllCategories(): Promise<string[]>{
    let categories: string[] = [];
    const response = await publicAPI.get("/categories/getAll");
    if(response.status === 200){
      for(let category of response.data){
        categories.push(category);
      }
      return categories;
    }
    throw new Error("Error getting all categories");
  }
  async getAllGlassTypes(): Promise<string[]>{
    let glassTypes: string[] = [];
    const response = await publicAPI.get("/glassTypes/getAll");
    if(response.status === 200){
      for(let glassType of response.data){
        glassTypes.push(glassType);
      }
      return glassTypes;
    }
    throw new Error("Error getting all glass types");
  }
}
