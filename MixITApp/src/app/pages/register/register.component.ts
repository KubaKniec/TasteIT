import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit{
  constructor(
    private authServive: AuthService,
    private toastService: HotToastService,
    private router: Router) {}

  ngOnInit(): void {

    }

  registerForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    username: new FormControl('', Validators.required),
    password: new FormControl('', Validators.required),
    confirmPassword: new FormControl('', Validators.required)
  })
  validateConfirmPassword(): boolean{
    const password = this.registerForm.get('password')!.value;
    const confirmPassword = this.registerForm.get('confirmPassword')!.value;

    if(password !== confirmPassword) {
      this.toastService.error('Passwords do not match!');
      return false;
    }
    return true;
  }
  register(){
    if(this.registerForm.invalid) {
      this.toastService.error('Invalid form!');
      return;
    }
    if(!this.validateConfirmPassword()){
      return;
    }
    const email = this.registerForm.get('email')!.value;
    const username = this.registerForm.get('username')!.value;
    const password = this.registerForm.get('password')!.value;
    this.authServive.register(email!, username!, password!).then(()=>{
      this.toastService.success('Register successfully!');
      this.router.navigate(['/login']).then();
    })

  }
}
