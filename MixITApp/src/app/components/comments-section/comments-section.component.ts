import {Component, EventEmitter, Input, OnInit, Output, ViewContainerRef} from '@angular/core';
import {PostService} from "../../service/post.service";
import {UserService} from "../../service/user.service";
import {Comment} from "../../model/post/Comment";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {CommentValidator} from "../../validators/CommentValidator";
import {HotToastService} from "@ngneat/hot-toast";

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

  constructor(
    private postService: PostService,
    private userService: UserService,
    private toastService: HotToastService
    ) {
  }
  getUserName(userId: string): string {
    return 'John Doe';
  }
  async ngOnInit(): Promise<void> {

  }
  onClose() {
    this.state = 'void';
    this.close.emit()
  }
  addComment(){

  }

}
