import {Injectable} from "@angular/core";
import authAPI from "../api/authAPI";
import {HotToastService} from "@ngneat/hot-toast";

@Injectable({
  providedIn: 'root'
})
export class UserAuthenticationService{
  constructor(private hotToastService: HotToastService) {

  }
  register(email: string, password: string, username: string) {
    let user = {
      email: email,
      username: username,
      password: password
    }
    authAPI.post("/register", user).then((response) => {
      console.log(response)
      localStorage.setItem("token", response.data.token)
    }).catch((error) => {
      console.log(error)
      this.hotToastService.error('Error');
    })
  }
  loginWithEmailAndPassword(email: string, password: string) {
    let user = {
      email: email,
      password: password
    }
    authAPI.post("/login", user).then((response) => {
      console.log(response)
      localStorage.setItem("token", response.data.token)
      //routing to home
    })
  }
  logout() {

  }
  updateUser(){

  }
  getCurrentUser(){

  }
}
