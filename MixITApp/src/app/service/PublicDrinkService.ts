import {Injectable} from "@angular/core";
import {Drink} from "../model/Drink";
import publicAPI from "../api/publicAPI";


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
}
