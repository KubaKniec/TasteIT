import {Component, Input} from '@angular/core';
import {Post} from "../../model/post/Post";

@Component({
  selector: 'app-daily-drink',
  templateUrl: './daily-drink.component.html',
  styleUrls: ['./daily-drink.component.css']
})
export class DailyDrinkComponent {
  @Input() dailyDrink!: Post;

}
