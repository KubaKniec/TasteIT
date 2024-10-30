import {Component, EventEmitter, Input, Output} from '@angular/core';
import {FoodList} from "../../model/FoodList";

@Component({
  selector: 'app-foodlist-grid',
  templateUrl: './foodlist-grid.component.html',
  styleUrls: ['./foodlist-grid.component.css']
})
export class FoodlistGridComponent {
  @Input() foodLists: FoodList[] = [];
  @Output() foodListClicked = new EventEmitter<FoodList>();


  onFoodListClicked(foodList: FoodList){
    this.foodListClicked.emit(foodList);
  }
}
