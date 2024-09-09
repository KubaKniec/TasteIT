import {Injectable} from "@angular/core";
import {Post} from "../model/Post";
import taste_api from "../api/taste_api";
import {Recipe} from "../model/Recipe";
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
  async getPostRecipe(id: string): Promise<Recipe>{
    const res = await taste_api.get(`/post/${id}/recipe`)
    if (res.status != 200){
      return Promise.reject(res.status);
    }
    return Promise.resolve(res.data as Recipe);
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
