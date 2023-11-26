import { Component } from '@angular/core';
import {GlobalConfiguration} from "../../config/GlobalConfiguration";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent {

  protected readonly GlobalConfiguration = GlobalConfiguration;
}
