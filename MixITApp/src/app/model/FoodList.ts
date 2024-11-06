import {Post} from "./post/Post";

export interface FoodList{
  foodListId: string;
  name: string;
  createdDate: Date;
  postsList: Post[];
  postsCount: number;
}
