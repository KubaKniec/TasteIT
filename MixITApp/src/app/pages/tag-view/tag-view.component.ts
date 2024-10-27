import {Component, OnInit} from '@angular/core';
import {NavigationService} from "../../service/navigation.service";
import {SearchService} from "../../service/search.service";
import {ActivatedRoute} from "@angular/router";
import {Post} from "../../model/post/Post";

@Component({
  selector: 'app-tag-view',
  templateUrl: './tag-view.component.html',
  styleUrls: ['./tag-view.component.css']
})
export class TagViewComponent implements OnInit {

  constructor(
    public navigationService: NavigationService,
    private searchService: SearchService,
    private route: ActivatedRoute
  ) { }
  tagId: string = '';
  isLoading: boolean = true;
  posts: Post[] = [];
  tagName: string = '';
  currentPage: number = 0;

  async ngOnInit(): Promise<void> {
    this.route.queryParams.subscribe(params => {
      this.tagName = params['tagName'];
    });
    this.tagId = this.route.snapshot.params['id'] as string;
    this.posts = await this.searchService.getPostsByTag(this.tagId, this.currentPage);
    this.isLoading = false;
  }
  async loadMorePosts() {
    this.currentPage++;
    let newPosts = await this.searchService.getPostsByTag(this.tagId, this.currentPage);
    this.posts = [...this.posts, ...newPosts];
  }

}
