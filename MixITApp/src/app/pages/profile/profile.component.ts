import {Component, OnInit} from '@angular/core';
import {GlobalConfiguration} from "../../config/GlobalConfiguration";
import {Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {UserService} from "../../service/user.service";
import {User} from "../../model/User";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  protected readonly GlobalConfiguration = GlobalConfiguration;
  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
    private toast: HotToastService) { }
  isAuthenticated: boolean = false;
  user: User = {};
  ngOnInit(): void {
    this.userService.getUser().then(user => {
      this.isAuthenticated = true;
      this.user = user;
      console.log(user);
    }).catch(err => {
      console.log('Not logged in');
    })
  }
  logout() {
    this.authService.logout()
      .then(res => {
        this.toast.success("Logged out");
        this.router.navigate(['/profile']).then();
        this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
          this.router.navigate(['/profile']);
        });
      })
  }
  goto(url: string) {
    if(url === 'login' && this.isAuthenticated){
      return;
    }
    this.router.navigateByUrl(url).then();
  }
}
