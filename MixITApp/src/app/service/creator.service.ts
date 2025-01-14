import {Injectable} from "@angular/core";
import {Post} from "../model/post/Post";
import taste_api from "../api/taste_api";

@Injectable({
  providedIn: 'root'
})
export class CreatorService {
  constructor() {
  }
  async searchPostsWithAnyIngredient(ingredientNames: string[]): Promise<Post[]>{
    try{
      const res = await taste_api.put('creator/any', ingredientNames);
      return res.data as Post[];
    }
    catch(error: any){
      return Promise.reject(error.response?.data || error);
    }
  }
  async searchPostsWithAllIngredients(ingredientNames: string[]): Promise<Post[]>{
    try{
      const res = await taste_api.put('creator/all', ingredientNames);
      return res.data as Post[];
    }
    catch(error: any){
      return Promise.reject(error.response?.data || error);
    }
  }
}
