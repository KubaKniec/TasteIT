import {Injectable} from "@angular/core";
import authAPI from "../api/authAPI";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor() {

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
    await authAPI.post('/login', {
      email,
      password
    }).then(res => {
      console.log(res);
      return res.status;
    }).catch(err => {
      return err.response.status;
    })
  }
}
