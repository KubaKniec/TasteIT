import axios from "axios";
import {GlobalConfiguration} from "../config/GlobalConfiguration";

export default axios.create({
  baseURL: GlobalConfiguration.API_URL+"v1/public",
  headers:{
    Accept : "application/json",
  }
})
