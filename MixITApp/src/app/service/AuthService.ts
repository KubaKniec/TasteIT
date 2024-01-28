import {Injectable} from "@angular/core";
import authAPI from "../api/authAPI";
import {CookieService} from "ngx-cookie-service";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private cookieService: CookieService) {

  }
  async register(email: string, username: string, password: string) {
    await authAPI.post('/register', {
      email,
      username,
      password
    }).then(res => {
      return res.status;
    }).catch(err => {
      return err.response.status;
    })
  }
  async loginWithEmailAndPassword(email: string, password: string) {
    const response = await authAPI.post('/login', {
      email,
      password
    })
    if(response.status === 200){
      return response.data;
    }
    throw new Error("Error logging in");
  }
  isAuthenticated() {
    const sessionToken = this.cookieService.get('sessionToken');
    return !!sessionToken;
  }
}
