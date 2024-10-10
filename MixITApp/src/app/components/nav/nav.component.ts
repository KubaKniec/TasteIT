import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit{
  constructor(private router: Router, private userService: UserService) {
  }
  userId: string = '';
  async ngOnInit(): Promise<void> {
    const user = await this.userService.getUserByToken();

    this.userId = user.userId!;
  }
  goto(url: string){
    this.router.navigate([url]).then();
  }
  activeRoute(url: string) {
    if (this.router.url.startsWith(url)) {
      return 'nav-item active';
    }
    return 'nav-item';
  }
}
