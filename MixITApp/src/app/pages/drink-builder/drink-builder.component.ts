import { Component, OnInit } from '@angular/core';
import { IngredientService } from '../../service/ingredient.service';
import { CreatorService } from '../../service/creator.service';
import { Ingredient } from '../../model/post/Ingredient';
import { Post } from '../../model/post/Post';
import { BehaviorSubject, debounceTime, switchMap } from 'rxjs';

@Component({
  selector: 'app-drink-builder',
  templateUrl: './drink-builder.component.html',
  styleUrls: ['./drink-builder.component.css']
})
export class DrinkBuilderComponent implements OnInit {
  private allIngredients: Ingredient[] = [];
  searchPhrase$ = new BehaviorSubject<string>('');
  selectedIngredients$ = new BehaviorSubject<Ingredient[]>([]);
  foundPosts$ = new BehaviorSubject<Post[]>([]);
  filteredIngredients$ = new BehaviorSubject<Ingredient[]>([]);

  flexibleMatching = true;
  allowAlcohol = true;

  constructor(
    private ingredientService: IngredientService,
    private creatorService: CreatorService
  ) {}

  ngOnInit(): void {
    this.loadIngredients();

    this.searchPhrase$
      .pipe(debounceTime(300))
      .subscribe((phrase) => this.filterIngredients(phrase));

    this.selectedIngredients$.subscribe(() => this.generateDrinkRequest());
  }

  private loadIngredients(): void {
    this.ingredientService.getAll().subscribe({
      next: (ingredients) => {
        this.allIngredients = this.removeDuplicates(ingredients).sort((a, b) =>
          a.name.toLowerCase().localeCompare(b.name.toLowerCase())
        );
        this.filteredIngredients$.next(this.allIngredients);
      },
      error: (error) => console.error('Error fetching ingredients:', error)
    });
  }

  private removeDuplicates(ingredients: Ingredient[]): Ingredient[] {
    return Array.from(
      new Map(ingredients.map((ingredient) => [ingredient.name.toLowerCase(), ingredient])).values()
    );
  }

  filterIngredients(phrase: string): void {
    const filtered = this.allIngredients.filter((ingredient) =>
      ingredient.name.toLowerCase().includes(phrase.toLowerCase())
    );
    this.filteredIngredients$.next(filtered);
  }
  get moreSelectedCount(): number {
    const selected = this.selectedIngredients$.value;
    return selected.length > 6 ? selected.length - 6 : 0;
  }

  handleIngredientClick(ingredient: Ingredient): void {
    const selected = this.selectedIngredients$.value;
    const updatedSelected = selected.includes(ingredient)
      ? selected.filter(i => i !== ingredient)
      : [...selected, ingredient];

    this.selectedIngredients$.next(updatedSelected);
    this.searchPhrase$.next('');
  }

  generateDrinkRequest(): void {
    const selectedNames = this.selectedIngredients$.value.map(i => i.name);

    if (selectedNames.length === 0) {
      this.foundPosts$.next([]);
      return;
    }

    const searchMethod = this.flexibleMatching
      ? this.creatorService.searchPostsWithAnyIngredient(selectedNames)
      : this.creatorService.searchPostsWithAllIngredients(selectedNames);

    searchMethod
      .then((posts) => this.foundPosts$.next(posts))
      .catch((error) => console.error('Error searching posts:', error));
  }

  updateSearchPhrase(phrase: string): void {
    this.searchPhrase$.next(phrase);
  }
}
