import {Injectable} from "@angular/core";
import {Post} from "../model/post/Post";
import taste_api from "../api/taste_api";
import {Recipe} from "../model/post/Recipe";
import {Comment} from "../model/post/Comment";
import {BehaviorSubject} from "rxjs";
import {AxiosResponse} from "axios";

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private feedSubject = new BehaviorSubject<Post[]>([]);
  feed$ = this.feedSubject.asObservable();

  constructor() {}

  setFeed(feed: Post[]): void {
    this.feedSubject.next(feed);
  }

  getFeedState(): Post[] {
    return this.feedSubject.getValue();
  }

  clearFeedCache(): void {
    this.feedSubject.next([]);
  }

  private async handleApiResponse<T>(response: AxiosResponse<any, any>, extractContent: boolean = false): Promise<T> {
    try{
      if(response.status != 200){
        console.error('API Error:', response.data);
        return Promise.reject(`Error: ${response.status}`)
      }
      if (extractContent && response.data.content) {
        return response.data.content as T;
      }
      return Promise.resolve(response.data as T);
    }catch (error){
      console.error('API Error:', error);
      return Promise.reject('Error');
    }
  }

  async getFeed(page: number, size: number): Promise<Post[]> {
    const response = await taste_api.get(`/post/feed?page=${page}&size=${size}`);
    const posts = await this.handleApiResponse<Post[]>(response, true);
    this.setFeed([...this.getFeedState(), ...posts]);
    return posts;
  }
  async getPostById(id: string): Promise<Post> {
    const res = await taste_api.get(`/post/${id}`);
    return this.handleApiResponse<Post>(res);
  }

  async getPostRecipe(id: string): Promise<Recipe> {
    const res = await taste_api.get(`/post/${id}/recipe`);
    return this.handleApiResponse<Recipe>(res);
  }

  async searchPostByTitle(query: string): Promise<Post[]> {
    const res = await taste_api.get(`/post/search?query=${query}`);
    return this.handleApiResponse<Post[]>(res);
  }

  async getPostComments(id: string): Promise<Comment[]> {
    const res = await taste_api.get(`/post/${id}/comments`);
    return this.handleApiResponse<Comment[]>(res);
  }

  async createPostComment(id: string, content: string): Promise<Comment> {
    const res = await taste_api.post(`/post/${id}/comment`, { content });
    return this.handleApiResponse<Comment>(res);
  }

  async deletePostComment(postId: string, commentId: string): Promise<number> {
    const res = await taste_api.delete(`/post/${postId}/comment/${commentId}`);
    return await this.handleApiResponse<number>(res);
  }

  async likePost(id: string): Promise<any> {
    return await taste_api.post(`/post/${id}/like`)
      .catch(function (error) {
        if(error.response){
          console.log(error.response.data);
        }else{
          console.log('Error: '+error.message);
        }
      });
  }

  async unlikePost(id: string): Promise<any> {
    return await taste_api.delete(`/post/${id}/like`)
      .catch(function (error) {
        if(error.response){
          console.log(error.response.data);
        }else{
          console.log('Error: '+error.message);
        }
      });
  }
  async getLikedPosts(userId: string): Promise<Post[]>{
    const res = await taste_api.get(`/post/likedby/`+userId);
    return await this.handleApiResponse<Post[]>(res);
  }
  async createPost(post: Post): Promise<Post> {
    const res = await taste_api.post(`/post`, post);
    return this.handleApiResponse<Post>(res);
  }
}
