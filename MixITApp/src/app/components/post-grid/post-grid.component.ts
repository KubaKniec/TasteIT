import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Post} from "../../model/post/Post";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post-grid',
  templateUrl: './post-grid.component.html',
  styleUrls: ['./post-grid.component.css']
})
export class PostGridComponent {
  constructor(private router: Router) {
  }
  @Input() posts: Post[] = [];
  @Output() loadMore = new EventEmitter<void>();

  onScroll(){
    this.loadMore.emit();
  }

  gotoPost(postId: string) {
    this.router.navigate([`/drink/${postId}`]);
  }
}
