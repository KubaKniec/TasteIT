import {Injectable} from "@angular/core";
import {Post} from "../model/post/Post";
import taste_api from "../api/taste_api";

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  async searchPosts(query: string, page?: number, size?: number, isAlcoholic?: boolean,): Promise<Post[]> {
    try {
      const params: any = { query };
      if (isAlcoholic !== undefined) params.isAlcoholic = isAlcoholic;
      if (page !== undefined) params.page = page;
      if (size !== undefined) params.size = size;

      const res = await taste_api.get('search/posts', { params });
      return res.data.content as Post[];
    } catch (error: any) {
      return Promise.reject(error.response?.data || error);
    }
  }
  async searchUsers(query: string, page?: number, size?: number): Promise<any> {
    try {
      const params: any = { query };
      if (page !== undefined) params.page = page;
      if (size !== undefined) params.size = size;

      const res = await taste_api.get('search/users', { params });
      return res.data.content;
    } catch (error: any) {
      return Promise.reject(error.response?.data || error);
    }
  }
  async searchTags(query: string, page?: number, size?: number): Promise<any> {
    try {
      const params: any = { query };
      if (page !== undefined) params.page = page;
      if (size !== undefined) params.size = size;

      const res = await taste_api.get('search/tags', { params });
      return res.data;
    } catch (error: any) {
      return Promise.reject(error.response?.data || error);
    }
  }




}
