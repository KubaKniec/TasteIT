import axios from "axios";
import {environment} from "../../environments/environment";

const authAPI =  axios.create({
  baseURL: environment.API_URL+"api/v1/auth",
  headers:{
    Accept : "application/json",
  },
  withCredentials: true,
})
export default authAPI;
