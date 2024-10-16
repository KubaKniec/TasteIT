import {Component, EventEmitter, Input, OnInit, Output, ViewContainerRef} from '@angular/core';
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {Comment} from "../../model/post/Comment";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {CommentValidator} from "../../validators/CommentValidator";
import {HotToastService} from "@ngneat/hot-toast";
import {User} from "../../model/user/User";
import {DateFormatter} from "../../helpers/DateFormatter";

@Component({
  selector: 'app-comments-section',
  templateUrl: './comments-section.component.html',
  styleUrls: ['./comments-section.component.css'],
  animations: [
    trigger('dialog', [
      state('void', style({
        transform: 'translateY(20px)',
        opacity: 0
      })),
      state('enter', style({
        transform: 'translateY(0)',
        opacity: 1
      })),
      transition('void => enter', animate('300ms cubic-bezier(0.25, 0.8, 0.25, 1)')),
      transition('enter => void', animate('300ms cubic-bezier(0.25, 0.8, 0.25, 1)'))
    ])
  ]
})
export class CommentsSectionComponent implements OnInit{
  @Input() postId!: string;
  @Output() refreshPost = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();
  comments: Comment[] = [];
  commentContent: string = '';
  state = 'enter'
  userNames: { [userId: string]: string } = {};
  currentUserId: string = '';

  constructor(
    private postService: PostService,
    private userService: UserService,
    private toastService: HotToastService
    ) {
  }
  async ngOnInit(): Promise<void> {
    this.comments = await this.postService.getPostComments(this.postId);
    await this.loadUserNames();
    let user: User = await this.userService.getUserByToken()
    this.currentUserId = user.userId || '';
  }
  isCurrentUserAuthor(comment: Comment): boolean{
    return comment.userId === this.currentUserId;
  }
  deleteComment(comment: Comment){
    this.postService.deletePostComment(this.postId, comment.commentId)
      .then(async () => {
        await this.ngOnInit();
        this.refreshPost.emit();
      })
      .catch(() => this.toastService.error('Failed to delete comment'));
  }
  async loadUserNames() {
    for (const comment of this.comments) {
      const commentAuthor = await this.userService.getUserById(comment.userId);
      this.userNames[comment.userId] = commentAuthor.displayName || 'Unknown';
    }
  }
  onClose() {
    this.state = 'void';
    this.close.emit()
  }
  async addComment() {
    if (!CommentValidator.isValid(this.commentContent)) {
      this.toastService.error('Failed to add comment');
      return;
    }
    await this.postService.createPostComment(this.postId, this.commentContent)
      .catch(() => this.toastService.error('Failed to add comment'))
      .finally(async () => {
        this.commentContent = '';
        await this.ngOnInit();
        this.refreshPost.emit();
      });
  }
  protected readonly DateFormatter = DateFormatter;
}
