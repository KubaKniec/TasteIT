import {Component, OnInit} from '@angular/core';
import {Ingredient} from "../../model/post/Ingredient";
import {FormControl} from "@angular/forms";
import {SearchService} from "../../service/search.service";
import {debounceTime, distinctUntilChanged, Subject, switchMap} from "rxjs";
import {ConfigurationService} from "../../service/configuration.service";
import {UserService} from "../../service/user.service";
import {HotToastService} from "@ngneat/hot-toast";
import {Tag} from "../../model/user/Tag";

@Component({
  selector: 'app-feed-settings',
  templateUrl: './feed-settings.component.html',
  styleUrls: ['./feed-settings.component.css']
})
export class FeedSettingsComponent implements OnInit {
  useRecommendations = new FormControl(this.configurationService.useRecommendationAlgorithm);

  ingredientSearchQuery = new Subject<string>();
  ingredientSuggestions: Ingredient[] = [];
  bannedIngredients: Ingredient[] = [];
  isLoadingIngredients = false;

  tagSearchQuery = new Subject<string>();
  tagSuggestions: Tag[] = [];
  bannedTags: Tag[] = [];
  isLoadingTags = false;

  constructor(
    private searchService: SearchService,
    private configurationService: ConfigurationService,
    private userService: UserService,
    private toastService: HotToastService
  ) {
    this.useRecommendations.valueChanges.subscribe(val => {
      this.configurationService.setUseRecommendationAlgorithm(val as boolean);
    });
  }

  async ngOnInit(): Promise<void> {
    try {
      this.isLoadingIngredients = true;
      this.isLoadingTags = true;

      const [ingredients, tags] = await Promise.all([
        this.userService.getBannedIngredients(),
        this.userService.getBannedTags()
      ]);

      this.bannedIngredients = ingredients;
      this.bannedTags = tags;
    } catch (error) {
      console.error('Failed to load banned items:', error);
      this.toastService.error('Failed to load banned items');
    } finally {
      this.isLoadingIngredients = false;
      this.isLoadingTags = false;
    }

    this.ingredientSearchQuery.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (query.length < 2) {
          this.ingredientSuggestions = [];
          return [];
        }
        return this.searchService.searchIngredients(query, 0, 5);
      })
    ).subscribe(results => {
      this.ingredientSuggestions = results;
    });

    this.tagSearchQuery.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(query => {
        if (query.length < 2) {
          this.tagSuggestions = [];
          return [];
        }
        return this.searchService.searchTags(query, 0, 5);
      })
    ).subscribe(results => {
      this.tagSuggestions = results;
    });
  }

  onIngredientSearch(event: any): void {
    const query = event.target.value;
    this.ingredientSearchQuery.next(query);
  }

  onTagSearch(event: any): void {
    const query = event.target.value;
    this.tagSearchQuery.next(query);
  }

  async banIngredient(ingredient: Ingredient): Promise<void> {
    if (!this.bannedIngredients.some(banned => banned.ingredientId === ingredient.ingredientId)) {
      try {
        const updatedIngredients = [...this.bannedIngredients, ingredient];
        await this.userService.updateBannedIngredients(updatedIngredients);
        this.bannedIngredients = updatedIngredients;
      } catch (error) {
        console.error('Failed to ban ingredient:', error);
        this.toastService.error('Unexpected error occurred while banning ingredient');
      }
    }
    this.ingredientSearchQuery.next('');
    this.ingredientSuggestions = [];
  }

  async banTag(tag: Tag): Promise<void> {
    if (!this.bannedTags.some(banned => banned.tagId === tag.tagId)) {
      try {
        const updatedTags = [...this.bannedTags, tag];
        await this.userService.updateBannedTags(updatedTags);
        this.bannedTags = updatedTags;
      } catch (error) {
        console.error('Failed to ban tag:', error);
        this.toastService.error('Unexpected error occurred while banning tag');
      }
    }
    this.tagSearchQuery.next('');
    this.tagSuggestions = [];
  }

  async removeBannedIngredient(index: number): Promise<void> {
    try {
      const updatedIngredients = [
        ...this.bannedIngredients.slice(0, index),
        ...this.bannedIngredients.slice(index + 1)
      ];
      await this.userService.updateBannedIngredients(updatedIngredients);
      this.bannedIngredients = updatedIngredients;
    } catch (error) {
      console.error('Failed to remove banned ingredient:', error);
      this.toastService.error('Failed to remove ingredient');
    }
  }

  async removeBannedTag(index: number): Promise<void> {
    try {
      const updatedTags = [
        ...this.bannedTags.slice(0, index),
        ...this.bannedTags.slice(index + 1)
      ];
      await this.userService.updateBannedTags(updatedTags);
      this.bannedTags = updatedTags;
    } catch (error) {
      console.error('Failed to remove banned tag:', error);
      this.toastService.error('Failed to remove tag');
    }
  }
}
