import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{
  constructor(private authService: AuthService, private hotToast: HotToastService, private router: Router) {
  }
  ngOnInit(): void {

  }
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required)
  })

  login() {
    if (this.loginForm.invalid) {
      if (this.loginForm.get('email')?.hasError('required')) {
        this.hotToast.error('Email is required!');
      } else if (this.loginForm.get('email')?.hasError('email')) {
        this.hotToast.error('Invalid email format!');
      }
      if (this.loginForm.get('password')?.hasError('required')) {
        this.hotToast.error('Password is required!');
      }
      return;
    }

    const email = this.loginForm.get('email')!.value;
    const password = this.loginForm.get('password')!.value;
    this.authService.loginWithEmailAndPassword(email!, password!)
      .then(() => {
        this.hotToast.success('Login successfully!', {
          duration: 1000
        });
        setTimeout(() => {
          this.router.navigate(['/home']);
        }, 200)
      })
      .catch((error) => {
        console.error('Login failed:', error); // Log the error for debugging
        this.hotToast.error('Login failed! Please check your credentials.');
      });
    }
}
