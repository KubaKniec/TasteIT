import axios from "axios";
import {environment} from "../../environments/environment";

export default axios.create({
  baseURL: environment.API_URL+"v1/public",
  headers:{
    Accept : "application/json",
  }
})
