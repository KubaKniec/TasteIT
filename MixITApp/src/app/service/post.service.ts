import {Injectable} from "@angular/core";
import {Post} from "../model/Post";
import taste_api from "../api/taste_api";
@Injectable({
  providedIn: 'root'
})
export class PostService {
  constructor() {
  }
  async getFeed(page: number, size: number): Promise<Post[]>{
    let posts: Post[] = [];
    const res = await taste_api.get(`/post/feed?page=${page}&size=${size}`)
    if (res.status != 200){
      return Promise.reject(res.status);
    }
    posts = res.data.content
    return Promise.resolve(posts);
  }
  async getPostById(id: string): Promise<Post>{
    let post: Post;
    const res = await taste_api.get(`/post/${id}`)
    if (res.status != 200){
      return Promise.reject(res.status);
    }
    post = res.data
    return Promise.resolve(post);
  }
  async searchPostByTitle(query: string): Promise<Post[]>{
    let posts: Post[] = [];
    const res = await taste_api.get(`/post/search?query=${query}`)
    if (res.status != 200){
      return Promise.reject(res.status);
    }
    posts = res.data
    return posts;
  }

}
