import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Post} from "../../model/post/Post";
import {Router} from "@angular/router";

@Component({
  selector: 'app-post-grid',
  templateUrl: './post-grid.component.html',
  styleUrls: ['./post-grid.component.css']
})
export class PostGridComponent implements OnChanges{
  constructor(private router: Router) {
  }
  @Input() posts: Post[] = [];
  @Output() loadMore = new EventEmitter<void>();
  @Input() mode: 'normal' | 'select' = 'normal';
  @Output() postClick = new EventEmitter<Post>();
  @Output() selectionChange = new EventEmitter<Post[]>();
  selectedPosts: Set<Post> = new Set();

  ngOnChanges(changes: SimpleChanges) {
    if (changes['mode']) {
      const previousMode = changes['mode'].previousValue;
      const currentMode = changes['mode'].currentValue;

      if (previousMode === 'select' && currentMode === 'normal') {
        this.clearSelectedPosts();
      }
    }
  }
  clearSelectedPosts() {
    this.selectedPosts.clear();
    this.selectionChange.emit([]);
  }
  onScroll(){
    this.loadMore.emit();
  }
  handlePostClick(post: Post) {
    if (this.mode === 'normal') {
      this.postClick.emit(post);
      this.gotoPost(post.postId || '0');
    }
    if (this.mode === 'select') {
      this.toggleSelection(new Event('click'), post);
    }
  }
  toggleSelection(event: Event, post: Post) {
    event.stopPropagation();
    if (this.selectedPosts.has(post)) {
      this.selectedPosts.delete(post);
    } else {
      this.selectedPosts.add(post);
    }
    this.selectionChange.emit([...this.selectedPosts]);
  }
  isSelected(post: Post): boolean {
    return this.selectedPosts.has(post);
  }

  gotoPost(postId: string) {
    this.router.navigate([`/drink/${postId}`]);
  }
}
