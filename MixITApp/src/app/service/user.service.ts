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
  async createBar(barName: string){
    const response = await userAPI.post('/bar', {name: barName});
    if(response.status === 200){
      return console.log("Created bar");
    }
    throw new Error("Error creating bar");
  }
  async addDrinkToBar(barId: number, drinkId:number){
    const response = await userAPI.post('/bar/'+barId+'/'+drinkId);
    if(response.status === 200){
      return console.log("Added drink to bar");
    }
    throw new Error("Error adding drink to bar");
  }
  async getDrinksDromBar(barId: number){
    const response = await userAPI.get('/bar/'+barId);
    if(response.status === 200){
      return response.data;
    }
    throw new Error("Error getting drinks from bar");
  }

}
