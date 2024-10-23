import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {SearchService} from "../../service/search.service";
import {Post} from "../../model/post/Post";
import {FormControl} from "@angular/forms";
import {debounceTime, distinctUntilChanged, filter, from, of, startWith, switchMap, tap} from "rxjs";
import {User} from "../../model/user/User";
import {Tag} from "../../model/user/Tag";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit{
  constructor(private router: Router,
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
        })
      )
      .subscribe(result => {
        switch (this.searchType) {
          case 'Posts':
            this.foundPosts = result;
            break;
          case 'Users':
            this.foundUsers = result;
            break;
          case 'Tags':
            this.foundTags = result;
            break;
        }
        this.isLoading = false;
      });
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
