import {Component, OnInit} from '@angular/core';
import {PublicIngredientsService} from "../../service/PublicIngredientsService";

@Component({
  selector: 'app-drink-builder',
  templateUrl: './drink-builder.component.html',
  styleUrls: ['./drink-builder.component.css']
})
export class DrinkBuilderComponent implements OnInit{
  constructor(
    private publicIngredientsService: PublicIngredientsService,

              ){}
  ingredients: String[] = [];
  ngOnInit(): void {
    this.publicIngredientsService.getAllIngredientsNames().then((ingredients) => {
      this.ingredients = ingredients;
    }).catch((error) => {
      console.log(error);
    });
  }

}
