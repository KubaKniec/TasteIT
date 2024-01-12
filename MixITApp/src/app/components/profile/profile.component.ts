import { Component } from '@angular/core';
import {GlobalConfiguration} from "../../config/GlobalConfiguration";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  protected readonly GlobalConfiguration = GlobalConfiguration;
  constructor(private router: Router) { }
  goto(url: string) {
    this.router.navigateByUrl(url).then(r => console.log(r));
  }
}
