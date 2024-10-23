import {Component, OnInit} from '@angular/core';
import {SearchService} from "../../service/search.service";
import {Post} from "../../model/post/Post";
import {FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged, filter, from, Observable, of, startWith, switchMap, tap} from "rxjs";
import {User} from "../../model/user/User";
import {Tag} from "../../model/user/Tag";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{
  constructor(
    private searchService: SearchService
              ) {}

  isLoading = false;
  foundPosts: Post[] = [];
  foundUsers: User[] = [];
  foundTags: Tag[] = [];
  currentPage = 0;
  searchControl = new FormControl();
  pageSize = 10;
  searchType: 'Posts' | 'Users' | 'Tags' = 'Posts';
  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        startWith(''),
        debounceTime(300),
        distinctUntilChanged(),
        filter(value => value.length >= 2 || value.length === 0),
        tap(() => {this.isLoading = true; this.currentPage = 0}),
        switchMap(value => {
          if (value.length === 0) {
            return of([]);
          } else {
            const encodedQuery = encodeURIComponent(value);
            return this.search(encodedQuery);
          }
        })
      )
      .subscribe(result => {
        this.assignResults(result);
        this.isLoading = false;
      });
  }
  assignResults(results: Post[] | User[] | Tag[]) {
    switch (this.searchType) {
      case 'Posts':
        this.foundPosts = results as Post[];
        break;
      case 'Users':
        this.foundUsers = results as User[];
        break;
      case 'Tags':
        this.foundTags = results as Tag[];
        break;
    }
  }
  search(value: string): Observable<any[]> {
    switch (this.searchType) {
      case 'Posts':
        return from(this.searchService.searchPosts(value));
      case 'Users':
        return from(this.searchService.searchUsers(value));
      case 'Tags':
        return from(this.searchService.searchTags(value));
      default:
        return of([]);
    }
  }
  setSearchType(type: 'Posts' | 'Users' | 'Tags') {
    this.searchType = type;
  }
  async loadMorePosts() {
    this.currentPage++;
    try {
      let newResults;
      switch (this.searchType) {
        case 'Posts':
          newResults = await this.searchService.searchPosts(this.searchControl.value, this.currentPage, this.pageSize);
          this.foundPosts = [...this.foundPosts, ...newResults];
          break;
        case 'Users':
          newResults = await this.searchService.searchUsers(this.searchControl.value, this.currentPage, this.pageSize);
          this.foundUsers = [...this.foundUsers, ...newResults];
          break;
        case 'Tags':
          newResults = await this.searchService.searchTags(this.searchControl.value, this.currentPage, this.pageSize);
          this.foundTags = [...this.foundTags, ...newResults];
          break;
      }
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading = false;
    }
  }


}
