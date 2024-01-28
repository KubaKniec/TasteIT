import {Injectable} from "@angular/core";
import userAPI from "../api/userAPI";
import {User} from "../model/User";

@Injectable({
  providedIn: 'root'
})
export class UserService{
  async getUser() {
    const reponse = await userAPI.get('/profile');
    if(reponse.status === 200){
      let user: User = reponse.data;
      return user;
    }
    throw new Error("Error getting user");
  }
  async addDrinkToFavorite(drinkId: number) {
    const response = await userAPI.post('/favourites/'+ drinkId);
    if(response.status === 200){
      return console.log("Added drink to favorite");
    }
    throw new Error("Error adding drink to favorite");
  }
}
