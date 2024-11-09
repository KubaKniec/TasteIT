import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {Comment} from "../../model/post/Comment";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {CommentValidator} from "../../validators/CommentValidator";
import {HotToastService} from "@ngneat/hot-toast";
import {User} from "../../model/user/User";
import {DateFormatter} from "../../helpers/DateFormatter";
import {Router} from "@angular/router";

type Author = {
  userId: string;
  displayName: string;
  profilePicture: string;
}

type CommentWithAuthor = {
  comment: Comment;
  author: Author;
}

@Component({
  selector: 'app-comments-section',
  templateUrl: './comments-section.component.html',
  styleUrls: ['./comments-section.component.css'],
  animations: [
    trigger('slideAnimation', [
      state('void', style({
        transform: 'translateY(100%)'
      })),
      state('visible', style({
        transform: 'translateY(0)'
      })),
      transition('void => visible', animate('300ms ease-out')),
      transition('visible => void', animate('300ms ease-in'))
    ]),
  ]
})
export class CommentsSectionComponent implements OnInit {
  @Input() postId!: string;
  @Output() refreshPost = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();
  currentState: string = 'visible';

  commentsWithAuthors: CommentWithAuthor[] = [];
  commentContent: string = '';
  state = 'enter'
  currentUserId: string = '';
  currentUserProfilePicture: string = '';

  constructor(
    private postService: PostService,
    private userService: UserService,
    private toastService: HotToastService,
    private router: Router
  ) {
  }

  async ngOnInit(): Promise<void> {
    await this.loadCommentsWithAuthors();
    let user: User = await this.userService.getUserByToken();
    this.currentUserId = user.userId || '';
    this.currentUserProfilePicture = user.profilePicture || '';
  }

  async loadCommentsWithAuthors(): Promise<void> {
    try {
      const comments = await this.postService.getPostComments(this.postId);
      const commentsWithAuthorsPromises = comments.map(async (comment) => {
        const user = await this.userService.getUserById(comment.userId);

        const author: Author = {
          userId: user.userId || '',
          displayName: user.displayName || 'Unknown',
          profilePicture: user.profilePicture || ''
        };

        return {
          comment,
          author
        };
      });
      this.commentsWithAuthors = await Promise.all(commentsWithAuthorsPromises);

    } catch (error) {
      this.toastService.error('Failed to load comments');
      console.error('Error loading comments with authors:', error);
    }
  }

  isCurrentUserAuthor(commentWithAuthor: CommentWithAuthor): boolean {
    return commentWithAuthor.author.userId === this.currentUserId;
  }

  deleteComment(commentWithAuthor: CommentWithAuthor) {
    this.postService.deletePostComment(this.postId, commentWithAuthor.comment.commentId)
      .then(async () => {
        await this.loadCommentsWithAuthors();
        this.refreshPost.emit();
      })
      .catch(() => this.toastService.error('Failed to delete comment'));
  }

  gotoProfile(userId: string) {
    this.router.navigate(['/user-profile', userId]);
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
        await this.loadCommentsWithAuthors();
        this.refreshPost.emit();
      });
  }

  protected readonly DateFormatter = DateFormatter;
}
