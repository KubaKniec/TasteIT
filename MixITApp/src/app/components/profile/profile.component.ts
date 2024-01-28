import {Component, OnInit} from '@angular/core';
import {GlobalConfiguration} from "../../config/GlobalConfiguration";
import {Router} from "@angular/router";
import {AuthService} from "../../service/AuthService";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  protected readonly GlobalConfiguration = GlobalConfiguration;
  constructor(private router: Router, private authService: AuthService) { }
  isAuthenticated: boolean = false;
  ngOnInit(): void {
    this.isAuthenticated = this.authService.isAuthenticated();
  }
  goto(url: string) {
    this.router.navigateByUrl(url).then(r => console.log(r));
  }
}
