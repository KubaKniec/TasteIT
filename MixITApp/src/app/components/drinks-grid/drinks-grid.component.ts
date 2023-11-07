import {Component, Input} from '@angular/core';
import {Drink} from "../../model/Drink";
import {Router} from "@angular/router";

@Component({
  selector: 'app-drinks-grid',
  templateUrl: './drinks-grid.component.html',
  styleUrls: ['./drinks-grid.component.css']
})
export class DrinksGridComponent {
  @Input() iterableDrinks: Drink[] = [];
  constructor(private router: Router) {
  }

  goto(idDrink: number) {
    this.router.navigate([`/drink/${idDrink}`]).then();
  }
}
