import {PostMedia} from "./PostMedia";
import {Recipe} from "./Recipe";
import {Tag} from "../user/Tag";
import {Like} from "./Like";
import {EPostType} from "./EPostType";

export interface Post {
  postId?: string;
  userId?: string;
  postType?: EPostType;
  postMedia?: PostMedia;
  tags?: Tag[];
  createdDate?: Date;
  likesCount?: number;
  Recipe?: Recipe;
  commentsCount?: number;
  likedByCurrentUser?: boolean;

}
