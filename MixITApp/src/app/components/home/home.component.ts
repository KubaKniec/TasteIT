import {Component, OnInit} from '@angular/core';
import {Drink} from "../../model/Drink";
import {DemoService} from "../../service/DemoService";
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{
  allDrinks: Drink[] = [];
  constructor(private demoService: DemoService, private router: Router) {
  }
  ngOnInit(): void {
    this.demoService.getAllDrinks().then((drinks) => {
      this.allDrinks = drinks;
      console.log(this.allDrinks);
    }).catch((error) => {
      console.log(error);
    })
  }
  gotoDrink(id: number){
    this.router.navigate([`/drink/${id}`]).then();
  }
}
