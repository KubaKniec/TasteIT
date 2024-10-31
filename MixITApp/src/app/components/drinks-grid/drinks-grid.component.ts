import {Component, Input} from '@angular/core';
import {Post} from "../../model/post/Post";
import {Router} from "@angular/router";

@Component({
  selector: 'app-drinks-grid',
  templateUrl: './drinks-grid.component.html',
  styleUrls: ['./drinks-grid.component.css']
})
export class DrinksGridComponent {
  @Input() iterableDrinks: Post[] = [];
  constructor(private router: Router) {
  }
  goto(idDrink: string) {
    this.router.navigate([`/drink/${idDrink}`]).then();
  }
}
