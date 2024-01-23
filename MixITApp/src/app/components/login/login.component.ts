import { Component } from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/AuthService";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  constructor(private authService: AuthService, private hotToast: HotToastService) {
  }
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  })

  login() {
    if(this.loginForm.invalid) {
      this.hotToast.error('Invalid form!');
      return;
    }
    const email = this.loginForm.get('email')!.value;
    const password = this.loginForm.get('password')!.value;
    this.authService.loginWithEmailAndPassword(email!, password!).then(()=>{
      this.hotToast.success('Login successfully!');
    });
  }
}
