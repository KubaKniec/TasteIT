import { Component } from '@angular/core';
import {UserAuthenticationService} from "../../service/UserAuthenticationService";

@Component({
  selector: 'app-your-bar',
  templateUrl: './your-bar.component.html',
  styleUrls: ['./your-bar.component.css']
})
export class YourBarComponent {
  constructor(private userAuthenticationService: UserAuthenticationService) {
  }


}
