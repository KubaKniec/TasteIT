import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/AuthService";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";
import {UserService} from "../../service/UserService";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  constructor(private authService: AuthService, private hotToast: HotToastService, private router: Router, private userService: UserService) {
  }
  ngOnInit(): void {
    this.userService.getUser().then(user => {
      this.router.navigate(['/profile']);
    }).catch(err => {

    })
  }
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  })

  login() {
    if(this.loginForm.invalid) {
      this.hotToast.error('Incorrect login or password!');
      return;
    }
    const email = this.loginForm.get('email')!.value;
    const password = this.loginForm.get('password')!.value;
    this.authService.loginWithEmailAndPassword(email!, password!).then(()=>{
      this.hotToast.success('Login successfully!');
      this.router.navigate(['/profile']);
    }).catch(err => {
      this.hotToast.error('Incorrect login or password!');
    });
  }
}
