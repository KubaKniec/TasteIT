import {Injectable} from "@angular/core";
import taste_api from "../api/taste_api";
import {Post} from "../model/Post";
@Injectable({
  providedIn: 'root'
})
export class PostService {
  async getFeed(){
    let posts: Post[] = [];
    const res = await taste_api.get('/post/feed')
    if (res.status != 200){
      throw new Error("Error getting feed");
    }
    posts = res.data.content
    return posts;
  }
  async getPostById(id: string){
    let post: Post;
    const res = await taste_api.get(`/post/${id}`)
    if (res.status != 200){
      throw new Error("Error getting post");
    }
    post = res.data
    return post;
  }

}
