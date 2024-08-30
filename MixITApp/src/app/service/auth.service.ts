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
    console.log(email, username, password);
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
      this.setSessionToken(response.data.sessionToken);
      return response.data;
    }
    throw new Error("Error logging in");
  }
  setSessionToken(sessionToken: string){
    localStorage.setItem('sessionToken', sessionToken);
    this.cookieService.set('sessionToken', sessionToken);
  }
  async logout(){
    await authAPI.post('/logout').then(res => {
      this.cookieService.delete('sessionToken');
      return res.status;
    }).catch(err => {
      return err.response.status;
    })
  }
  isAuthenticated(): boolean {
    return this.cookieService.check('sessionToken');
  }
}
