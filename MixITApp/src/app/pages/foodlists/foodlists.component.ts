import {Component, OnInit} from '@angular/core';
import {FoodlistService} from "../../service/foodlist.service";
import {UserService} from "../../service/user.service";
import {User} from "../../model/user/User";
import {FoodList} from "../../model/FoodList";
import {Router} from "@angular/router";

@Component({
  selector: 'app-foodlists',
  templateUrl: './foodlists.component.html',
  styleUrls: ['./foodlists.component.css']
})
export class FoodlistsComponent implements OnInit {

  constructor(
    private foodListService: FoodlistService,
    private userService: UserService,
    private router: Router
  ) { }
  currentUser: User = {}
  foodLists: FoodList[] = []
  isLoading: boolean = true
  ngOnInit(): void {
    Promise.all([this.getFoodLists(), this.getUser()]).then(r => {
      this.isLoading = false
    })
  }
  async getFoodLists() {
    this.foodLists = await this.foodListService.getAllFoodLists()
  }
  async getUser() {
    this.currentUser = await this.userService.getUserByToken()
  }

  goto($event: FoodList) {
    this.router.navigate(["/foodlist", $event.foodListId])
  }
}
