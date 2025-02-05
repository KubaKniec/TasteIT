import {Component, OnInit} from '@angular/core';
import {Ingredient} from "../../model/post/Ingredient";
import {FormControl} from "@angular/forms";
import {SearchService} from "../../service/search.service";
import {debounceTime, distinctUntilChanged, Subject, switchMap} from "rxjs";
import {GlobalConfiguration} from "../../config/GlobalConfiguration";

@Component({
  selector: 'app-feed-settings',
  templateUrl: './feed-settings.component.html',
  styleUrls: ['./feed-settings.component.css']
})
export class FeedSettingsComponent implements OnInit {
  useRecommendations = new FormControl(GlobalConfiguration.USE_RECOMMENDATION_ALGORITHM);
  searchQuery = new Subject<string>();
  suggestions: Ingredient[] = [];
  bannedIngredients: Ingredient[] = [];

  constructor(private searchService: SearchService) {
    this.useRecommendations.valueChanges.subscribe(val => {
      GlobalConfiguration.onToggleRecommendations(val as boolean);
    })
  }

  ngOnInit(): void {
    this.searchQuery.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (query.length < 2) {
          this.suggestions = [];
          return [];
        }
        return this.searchService.searchIngredients(query, 0, 5);
      })
    ).subscribe(results => {
      this.suggestions = results;
    });
  }

  onSearch(event: any): void {
    const query = event.target.value;
    this.searchQuery.next(query);
  }

  banIngredient(ingredient: Ingredient): void {
    if (!this.bannedIngredients.some(banned => banned.ingredientId === ingredient.ingredientId)) {
      this.bannedIngredients.push(ingredient);
    }
    this.searchQuery.next('');
    this.suggestions = [];
  }

  removeBannedIngredient(index: number): void {
    this.bannedIngredients.splice(index, 1);
  }
}
