import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {User} from "../../model/user/User";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit{
  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
  ) { }
  user: User = {};
  userId: string = '';
  loggedUserId: string = '';
  ngOnInit(): void {
    this.userId = this.route.snapshot.params['id'] as string;
    this.userService.getUserById(this.userId).then(user => {
      this.user = user;
    });
    this.userService.getUserByToken().then(user => {
      this.loggedUserId = user.userId!;
    });

  }
  isVisitor(): boolean {
    return this.loggedUserId !== this.userId;
  }

}
