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
      this.toastService.error('Please fill in all fields!');
      return;
    }
    if(!this.validateConfirmPassword()){
      this.toastService.error('Passwords do not match!');
      return;
    }
    const email = this.registerForm.get('email')!.value;
    const password = this.registerForm.get('password')!.value;
    this.authServive.register(email!, password!).then(()=>{
      this.toastService.success('Register successfully!');
      this.router.navigate(['/login']).then();
    })

  }
}
