import {Injectable} from "@angular/core";
import {Drink} from "../model/Drink";
import api from "../api/api";

@Injectable({
    providedIn: 'root'
  })
export class DemoService{

  async getAllDrinks(){
    let drinks: Drink[] = [];
    const response = await api.get("/demo/drink/all")
    if(response.status === 200){
      for(let drink of response.data){
        drinks.push(drink);
      }
      return drinks;
    }
    throw new Error("Error getting all drinks");
  }
  async getDrinkById(id: number): Promise<Drink>{
    const response = await api.get(`/demo/drink/${id}`);
    if(response.status === 200){
      return response.data;
    }
    throw new Error("Error getting drink by id");
  }
}
