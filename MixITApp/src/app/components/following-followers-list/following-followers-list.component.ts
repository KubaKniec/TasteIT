import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {User} from "../../model/user/User";
import {UserService} from "../../service/user.service";
import {NavigationService} from "../../service/navigation.service";

@Component({
  selector: 'app-following-followers-list',
  templateUrl: './following-followers-list.component.html',
  styleUrls: ['./following-followers-list.component.css']
})
export class FollowingFollowersListComponent implements OnInit {
  title = '';
  persons: User[] = [];
  userId = '';
  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    public navigationService: NavigationService
  ) { }

  async ngOnInit(): Promise<void> {
    this.userId = this.route.snapshot.paramMap.get('id') || '';
    this.route.url.subscribe(async urlSegment => {
      const path = urlSegment.map(segment => segment.path).join('/');
      if (path.includes('following')) {
        this.title = 'Following';
        await this.loadFollowing();
      } else if (path.includes('followers')) {
        this.title = 'Followers';
        await this.loadFollowers();
      }
    })
  }

  async loadFollowing() {
    this.persons = await this.userService.getFollowing(this.userId);
  }
  async loadFollowers() {
    this.persons = await this.userService.getFollowers(this.userId);
  }
}
