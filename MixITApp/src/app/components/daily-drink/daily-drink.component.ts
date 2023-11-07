import {Component, Input} from '@angular/core';
import {Drink} from "../../model/Drink";

@Component({
  selector: 'app-daily-drink',
  templateUrl: './daily-drink.component.html',
  styleUrls: ['./daily-drink.component.css']
})
export class DailyDrinkComponent {
  @Input() dailyDrink!: Drink;

}
