import {Component, Input} from '@angular/core';
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

  gotoPost(postId: string) {
    this.router.navigate([`/drink/${postId}`]);
  }
}
