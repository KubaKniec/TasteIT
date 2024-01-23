import { Component } from '@angular/core';
import {AuthService} from "../../service/AuthService";

@Component({
  selector: 'app-your-bar',
  templateUrl: './your-bar.component.html',
  styleUrls: ['./your-bar.component.css']
})
export class YourBarComponent {
  constructor(private userAuthenticationService: AuthService) {
  }


}
