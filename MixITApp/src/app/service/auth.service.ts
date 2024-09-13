import {Injectable} from "@angular/core";
import authAPI from "../api/authAPI";
import {BehaviorSubject, Observable} from "rxjs";
import taste_api from "../api/taste_api";
import {UserService} from "./user.service";



@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private sessionTokenSubject: BehaviorSubject<string | null>;
  public sessionToken: Observable<string | null>;
  constructor(private userService: UserService) {
    this.sessionTokenSubject = new BehaviorSubject<string | null>(localStorage.getItem('sessionToken'));
    this.sessionToken = this.sessionTokenSubject.asObservable();
  }

  public get sessionTokenValue(): string | null{
    return this.sessionTokenSubject.value;
  }
  async register(email: string, username: string, password: string) {
    await authAPI.post('/register', {
      email,
      password
    }).then(res => {
      return Promise.resolve(res.status);
    }).catch(err => {
      return Promise.reject(err.response.status);
    })
  }
  async loginWithEmailAndPassword(email: string, password: string) {
    return await authAPI.post('/login', {
      email,
      password
    }).then(async res => {
      localStorage.setItem('sessionToken', res.data.sessionToken);
      this.sessionTokenSubject.next(res.data.sessionToken);
      return Promise.resolve(res.status);
    }).catch(err => {
      return Promise.reject(err.response.status);
    })

  }
  clearSession(){
    localStorage.removeItem('sessionToken');
    this.sessionTokenSubject.next(null);
  }
  async logout(){
    return await taste_api.get('/auth/logout').then(res => {
      this.clearSession();

    }).catch(err => {
      console.log("Session cleared client side");
      this.clearSession();
      return Promise.reject(err.response.status);
    })
  }
  isAuthenticated(): boolean {
    return this.sessionTokenSubject.value !== null;
  }
}
