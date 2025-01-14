import {Injectable} from "@angular/core";
import {LoggerService} from "./logger.service";
import taste_api from "../api/taste_api";
import {FoodList} from "../model/FoodList";
import {GenericResponse} from "../model/GenericResponse";

@Injectable({
  providedIn: 'root'
})
export class FoodlistService{
  constructor(private logger: LoggerService) {}

  async createFoodList(name: string){
    try{
      const res = await taste_api.post(`foodlist/`, name);
      return res.data;
    }catch (error: any){
      this.logger.logError('Error creating food list', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async getFoodListById(id: string): Promise<FoodList>{
    try{
      const res = await taste_api.get(`foodlist/${id}`);
      return res.data as FoodList;
    }catch (error: any){
      this.logger.logError('Error fetching food list by ID', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async getAllFoodLists(): Promise<FoodList[]>{
    try{
      const res = await taste_api.get('foodlist/simple');
      return res.data as FoodList[];
    }catch (error: any){
      this.logger.logError('Error fetching all food lists', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async updateFoodListName(id: string, name: string): Promise<GenericResponse>{
    try{
      const res = await taste_api.put(`foodlist/name/${id}`, {name: name});
      return res.data;
    }catch (error: any){
      this.logger.logError('Error updating food list name', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async deleteFoodList(id: string): Promise<GenericResponse>{
    try{
      const res = await taste_api.delete(`foodlist/${id}`);
      return res.data;
    }catch (error: any){
      this.logger.logError('Error deleting food list', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async addPostToFoodList(foodListId: string, postId: string): Promise<GenericResponse>{
    try{
      const res = await taste_api.post(`foodlist/post/${foodListId}`, {postId: postId});
      return res.data;
    }catch (error: any){
      this.logger.logError('Error adding post to food list', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
  async removePostFromFoodList(foodListId: string, postId: string): Promise<GenericResponse>{
    try{
      const res = await taste_api.delete(`foodlist/post/${foodListId}`,{data: {postId: postId}});
      return res.data;
    }catch (error: any){
      this.logger.logError('Error removing post from food list', error.response?.data || error);
      return Promise.reject(error.response?.data || error);
    }
  }
}
