import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {Post} from "../../model/Post";

@Component({
  selector: 'app-category-view',
  templateUrl: './category-view.component.html',
  styleUrls: ['./category-view.component.css']
})
export class CategoryViewComponent implements OnInit{
  category!: string;
  drinks: Post[] = [];
  isLoaded: boolean = false;
  constructor(
    private route: ActivatedRoute,
    private toast: HotToastService,

  ) {}
  ngOnInit(): void {
    this.category = this.route.snapshot.params['category'];
    // this.publicDrinkService.getFilteredDrinks(this.category).then((drinks) => {
    //   this.drinks = drinks;
    // }).catch((error) => {
    //   this.toast.error(error.message);
    // }).finally(() => {
    //   this.isLoaded = true;
    // })
  }

}
