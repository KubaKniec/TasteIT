import {Component, OnInit} from '@angular/core';
import {UserService} from "../../service/user.service";
import {PostService} from "../../service/post.service";
import {User} from "../../model/user/User";
import {Post} from "../../model/post/Post";


@Component({
  selector: 'app-user-likes',
  templateUrl: './user-likes.component.html',
  styleUrls: ['./user-likes.component.css']
})
export class UserLikesComponent implements OnInit{

  constructor(
    private userService: UserService,
    private postService: PostService
  ) { }
  likedPosts: Post[] = [];
  user: User = {}
  isLoading: boolean = true;

  async ngOnInit(): Promise<void> {
    this.user = await this.userService.getUserByToken();
    this.likedPosts = await this.postService.getLikedPosts(this.user.userId!);
    this.isLoading = false;
  }

}
