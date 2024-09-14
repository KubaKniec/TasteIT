import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Tag} from "../../model/Tag";
import {HotToastService} from "@ngneat/hot-toast";
import {UserProfile} from "../../model/UserProfile";
import {UserService} from "../../service/user.service";
import {User} from "../../model/User";
import {Router} from "@angular/router";

@Component({
  selector: 'app-setup-profile',
  templateUrl: './setup-profile.component.html',
  styleUrls: ['./setup-profile.component.css']
})
export class SetupProfileComponent implements OnInit {
  form: FormGroup;
  currentStep: number = 1;
  totalSteps: number = 5;
  preferences: Tag[] = [];
  user!: User;
  tags: Tag[] = [
    {tag_id: '1', tag: 'Drinks'},
    {tag_id: '2', tag: 'Meat'},
    {tag_id: '3', tag: 'Vegetarian'},
    {tag_id: '4', tag: 'Burgers'},
    {tag_id: '5', tag: 'Pizza'},
    {tag_id: '6', tag: 'Pasta'},
    {tag_id: '7', tag: 'Italian'},
    {tag_id: '8', tag: 'Asian'},
    {tag_id: '9', tag: 'Mexican'},
  ]

  constructor(
    private fb: FormBuilder,
    private hotToast: HotToastService,
    private userService: UserService,
    private router: Router
    ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      birthdate: ['', Validators.required],
      bio: [''],
    })
  }
  getProgress(){
    return (this.currentStep / this.totalSteps) * 100;
  }
  goNext(){
    if (this.currentStep >= this.totalSteps) return;
    console.log(this.isStepValid())
    if(this.isStepValid()){
      this.currentStep++;
    }else{
      this.triggerValidationError();
      this.form.markAllAsTouched();
    }
  }
  goToPreviousStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }
  triggerValidationError(){
    this.hotToast.error('Please fill out the required fields');
  }
  isStepValid(): boolean{
    if (this.currentStep === 1){
      return this.form.get('username')?.valid && this.form.get('username')?.valid || false;
    }else if (this.currentStep === 2){
      return this.form.get('birthdate')?.valid || false;
    }else if (this.currentStep === 3){
      return true;
    }else if(this.currentStep === 4){
      return this.preferences.length >= 3;
    }
    return false;
  }

  isSelected(tag: Tag) {
    return this.preferences.includes(tag);
  }
  toggleTagSelection(tag: Tag){
    if (this.isSelected(tag)){
      this.preferences = this.preferences.filter(t => t.tag_id !== tag.tag_id);
    }else{
      this.preferences.push(tag);
    }
  }

  setUpAccount() {
    let userProfile: UserProfile = {
      bio: this.form.get('bio')?.value,
      displayName: this.form.get('username')?.value,
      profilePicture: 'placeholder.jpg',
      birthdate: this.form.get('birthdate')?.value,
    }
    this.userService.updateUserProfile(this.user.userId!, userProfile).then(
      res => {
        this.userService.changeUserFirstLogin(this.user.userId!).then(
          res => {
            this.hotToast.success('Account setup complete');
            this.router.navigate(['/home']).then();
          }
        )
      }
    )
    console.log(userProfile)
  }

  async ngOnInit(): Promise<void> {
    this.user = await this.userService.getUserByToken();
  }
}
