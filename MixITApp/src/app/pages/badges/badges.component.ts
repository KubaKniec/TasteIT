import { Component } from '@angular/core';
import {UserService} from "../../service/user.service";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../model/user/User";


@Component({
  selector: 'app-badges',
  templateUrl: './badges.component.html',
  styleUrls: ['./badges.component.css']
})
export class BadgesComponent {

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
  ) { }
  currentUser: User = {badges: [] }
  userId:string = '';
  async ngOnInit(): Promise<void> { // nie odświeża się progres np po dodaniu posta:(
    await this.getUser()
    this.userId = this.currentUser.userId as string
    console.log('stolen id '+this.userId)
  }
  async getUser() {
    const userFromToken = await this.userService.getUserByToken();
    this.currentUser = await this.userService.getUserById(userFromToken.userId as string)
    console.log('Badges:', this.currentUser.badges);
  }
}
