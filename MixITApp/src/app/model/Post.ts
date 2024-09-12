import {PostMedia} from "./PostMedia";
import {Recipe} from "./Recipe";
import {Tag} from "./Tag";
import {Like} from "./Like";

export interface Post {
  postId?: string;
  user_id?: string;
  postMedia?: PostMedia;
  recipe?: Recipe;
  tags?: Tag[];
  createdDate?: Date;
  likes?: Like[];
  comments?: Comment[];

}
