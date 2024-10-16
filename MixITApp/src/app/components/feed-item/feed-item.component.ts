import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Haptics, ImpactStyle} from "@capacitor/haptics";
import {Post} from "../../model/post/Post";
import {PostService} from "../../service/post.service";
import {DateFormatter} from "../../helpers/DateFormatter";
import {User} from "../../model/user/User";
import {UserService} from "../../service/user.service";

@Component({
  selector: 'app-feed-item',
  templateUrl: './feed-item.component.html',
  styleUrls: ['./feed-item.component.css']
})
export class FeedItemComponent implements OnInit{
  @Input() feedItem: Post = {};
  @Output() gotoDrink: EventEmitter<any> = new EventEmitter<any>();
  @Output() likeEvent: EventEmitter<any> = new EventEmitter<any>();
  postAuthor: User = {};
  constructor(private postService: PostService,
              private userService: UserService
              ) {
  }
  async ngOnInit(): Promise<void>{
    this.postAuthor = await this.userService.getUserById(this.feedItem.userId!);
  }
  getAuthorName(): string {
    return this.postAuthor.displayName ?? 'Unknown';
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
  protected readonly DateFormatter = DateFormatter;
}
