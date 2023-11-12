import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PublicDrinkService} from "../../service/PublicDrinkService";
import {Filter} from "../../model/Filter";
import {Drink} from "../../model/Drink";

@Component({
  selector: 'app-drink-builder-results',
  templateUrl: './drink-builder-results.component.html',
  styleUrls: ['./drink-builder-results.component.css']
})
export class DrinkBuilderResultsComponent implements OnInit{
  @Input() filters!: Filter;
  @Output() close = new EventEmitter<void>();
  generatedDrinks: Drink[] = [];
  isLoaded: boolean = false;
  constructor(private publicDrinkService: PublicDrinkService){
  }


  onClose(){
    this.close.emit()
  }

  async ngOnInit(): Promise<void> {
    this.publicDrinkService.getGeneratedDrinks(this.filters).then((drinks) => {
      this.generatedDrinks = drinks;
      console.log(this.generatedDrinks);
      this.isLoaded = true;
    })
  }
}
