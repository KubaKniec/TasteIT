import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Post } from '../model/post/Post';
import { User } from '../model/user/User';
import { Tag } from '../model/user/Tag';

interface SearchState {
  searchTerm: string;
  searchType: 'Posts' | 'Users' | 'Tags';
  currentPage: number;
  foundPosts: Post[];
  foundUsers: User[];
  foundTags: Tag[];
}

@Injectable({
  providedIn: 'root'
})
export class SearchStateService {
  private initialState: SearchState = {
    searchTerm: '',
    searchType: 'Posts',
    currentPage: 0,
    foundPosts: [],
    foundUsers: [],
    foundTags: []
  };

  private searchState = new BehaviorSubject<SearchState>(this.initialState);

  getState() {
    return this.searchState.asObservable();
  }

  updateState(newState: {
    searchTerm: string | null;
    searchType: "Posts" | "Users" | "Tags";
    foundTags: Tag[];
    foundPosts: Post[];
    foundUsers: User[];
    currentPage: number
  }) {
    this.searchState.next(<SearchState>{
      ...this.searchState.value,
      ...newState
    });
  }

  clearState() {
    this.searchState.next(this.initialState);
  }
}
