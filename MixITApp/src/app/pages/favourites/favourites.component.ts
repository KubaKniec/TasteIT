import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";
import {Drink} from "../../model/Drink";
import {User} from "../../model/User";
import {PublicDrinkService} from "../../service/public.drink.service";

@Component({
  selector: 'app-favourites',
  templateUrl: './favourites.component.html',
  styleUrls: ['./favourites.component.css']
})
export class FavouritesComponent implements OnInit{
  constructor(private userService: UserService, private toast: HotToastService, private router: Router, private publicDrinkService: PublicDrinkService) {
  }
  user: User = {};
  favouriteDrinks: Drink[] = [];
  shouldShow: boolean = false;
  async ngOnInit(): Promise<void> {
    try {
      this.user = await this.userService.getUser();
      this.user.favouriteDrinks!.forEach(drink => {
        this.favouriteDrinks.push(drink);
      });
      console.log('ok');
    } catch (err) {
      this.toast.info("You must be logged in to view your favourites");
      this.router.navigate(['/login']).then();
    } finally {
      this.shouldShow = true;
    }

  }

}
