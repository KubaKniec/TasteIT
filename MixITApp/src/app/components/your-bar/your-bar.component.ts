import {Component, OnInit} from '@angular/core';
import {User} from "../../model/User";
import {Bar} from "../../model/Bar";
import {UserService} from "../../service/UserService";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-your-bar',
  templateUrl: './your-bar.component.html',
  styleUrls: ['./your-bar.component.css']
})
export class YourBarComponent implements OnInit{
  user: User = {}
  userBars: Bar[] = [];
  constructor(
    private userService: UserService,
    private toast: HotToastService,
  ) {}
  ngOnInit(): void {
    this.userService.getUser().then((user) => {
      this.user = user;
      this.user.bars?.forEach((bar) => {
        this.userBars.push(bar);
      })
    }).catch((e) => {
      console.log(e);
    })
  }
  createBar(){
    this.userService.createBar('Sample bar').then(r => {
      this.toast.success('Bar created');
    })
  }


}
