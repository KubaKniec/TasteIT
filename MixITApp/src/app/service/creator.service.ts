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
      const res = await taste_api.get('creator/any', {params: {ingredientNames: ingredientNames}});
      return res.data.content as Post[];
    }
    catch(error: any){
      return Promise.reject(error.response?.data || error);
    }
  }
  async searchPostsWithAllIngredients(ingredientNames: string[]): Promise<Post[]>{
    try{
      const res = await taste_api.get('creator/all', {params: {ingredientNames: ingredientNames}});
      return res.data.content as Post[];
    }
    catch(error: any){
      return Promise.reject(error.response?.data || error);
    }
  }
}
