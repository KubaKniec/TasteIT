import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Tag} from "../../model/user/Tag";
import {HotToastService} from "@ngneat/hot-toast";
import {UserProfile} from "../../model/user/UserProfile";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user/User";
import {Router} from "@angular/router";
import {UsernameValidator} from "../../validators/UsernameValidator";
import {UserTags} from "../../model/user/UserTags";

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
    {tagName: 'Drinks'},
    {tagName: 'Meat'},
    {tagName: 'Vegetarian'},
    {tagName: 'Burgers'},
    {tagName: 'Pizza'},
    {tagName: 'Pasta'},
    {tagName: 'Italian'},
    {tagName: 'Asian'},
    {tagName: 'Mexican'},
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

  async setUpAccount() {
    let displayName = this.form.get('username')?.value;
    let birthdateInput = this.form.get('birthdate')?.value;
    let birthdate = new Date(birthdateInput);
    let day = birthdate.getDate().toString().padStart(2, '0');
    let month = (birthdate.getMonth() + 1).toString().padStart(2, '0');
    let year = birthdate.getFullYear().toString();
    let formattedDate = `${day}-${month}-${year}`;

    let userProfile: UserProfile = {
      bio: this.form.get('bio')?.value,
      displayName: displayName,
      profilePicture: 'placeholder.jpg',
      birthdate: formattedDate,
    }
    const userTags: UserTags = {
      mainTags: this.preferences,
      customTags: []
    }
    await this.updateUserAccount(userTags, userProfile);
  }

  async updateUserAccount(userTags: UserTags, userProfile: UserProfile){
    this.userService.updateUserTags(this.user.userId!, userTags).then(
      res => {
        return this.userService.updateUserProfile(this.user.userId!, userProfile);
      }
    ).then(
      res => {
        return this.userService.changeUserFirstLogin(this.user.userId!);
      }
    ).then(
      res => {
        this.hotToast.success('Account setup complete');
        return this.router.navigate(['/home']);
      }
    ).catch(
      err => {
        console.log(err);
        this.hotToast.error('An error occurred while setting up your account');
      }
    );
  }

  async ngOnInit(): Promise<void> {
    this.user = await this.userService.getUserByToken();
  }
}
