import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Haptics, ImpactStyle} from "@capacitor/haptics";
import {Post} from "../../model/post/Post";
import {PostService} from "../../service/post.service";

@Component({
  selector: 'app-feed-item',
  templateUrl: './feed-item.component.html',
  styleUrls: ['./feed-item.component.css']
})
export class FeedItemComponent {
  @Input() feedItem: Post = {};
  @Output() gotoDrink: EventEmitter<any> = new EventEmitter<any>();
  @Output() likeEvent: EventEmitter<any> = new EventEmitter<any>();
  constructor(private postService: PostService) {
  }
  emitGotoDrink(): void {
    this.gotoDrink.emit(this.feedItem.postId);
  }
  async updatePost(): Promise<void> {
    this.feedItem = await this.postService.getPostById(this.feedItem.postId!);
  }

  async emitLike(event: Event) {
    event.stopPropagation();
    await Haptics.impact({style: ImpactStyle.Medium})

    this.feedItem.likedByCurrentUser ?
        await this.postService.unlikePost(this.feedItem.postId!) :
        await this.postService.likePost(this.feedItem.postId!)

    await this.updatePost();
    this.likeEvent.emit();
  }

  emitComment(event: Event) {
    event.stopPropagation();
    console.log('Comment clicked');

  }
  getDate(): string {
    let date = new Date(this.feedItem.createdDate!);
    let now = new Date();
    let diffInMilliseconds = now.getTime() - date.getTime();
    let diffInHours = Math.floor(diffInMilliseconds / (1000 * 60 * 60));
    let diffInDays = Math.floor(diffInMilliseconds / (1000 * 60 * 60 * 24));

    if (diffInHours < 1) {
      return 'less than an hour ago';
    } else if (diffInHours < 24) {
      return `${diffInHours} hours ago`;
    } else if (diffInDays <= 7) {
      return `${diffInDays} days ago`;
    } else {
      return date.toLocaleDateString('en-GB');
    }
  }


}
