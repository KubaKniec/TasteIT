import { Injectable } from '@angular/core';
import taste_api from "../api/taste_api";
import {User} from "../model/user/User";
import {UserProfile} from "../model/user/UserProfile";
import {UserTags} from "../model/user/UserTags";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  async getUserById(id: string): Promise<User> {
    let user: User;
    const res = await taste_api.get(`user/getUserById/${id}`)
    if (res.status != 200) {
      return Promise.reject(res.status);
    }
    user = res.data
    return Promise.resolve(user);
  }
  async getUserByToken(): Promise<User> {
    let user: User;
    const res = await taste_api.get(`user/getUserByToken`)
    if (res.status != 200) {
      return Promise.reject(res.status);
    }
    user = res.data
    return Promise.resolve(user);
  }
  async updateUserProfile(userId: string, userProfile: UserProfile): Promise<User>{
    const res = await taste_api.post(`user/updateUserProfile/${userId}`, userProfile)
    if (res.status != 200) {
      return Promise.reject(res.status);
    }
    return Promise.resolve(res.data);

  }
  async changeUserFirstLogin(userId: string){
    const res = await taste_api.post(`user/changeUserFirstLogin/${userId}`)
    if (res.status != 200) {
      return Promise.reject(res.status);
    }
    return Promise.resolve(res.data);
  }
  async updateUserTags(userId: string, userTags: UserTags){
    const res = await taste_api.post(`user/changeUserTags/${userId}`, userTags)
    if (res.status != 200) {
      return Promise.reject(res.status);
    }
    return Promise.resolve(res.data);
  }
}
