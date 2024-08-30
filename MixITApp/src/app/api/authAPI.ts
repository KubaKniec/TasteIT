import axios from "axios";

const authAPI =  axios.create({
  baseURL: "http://localhost:8080/api/v1/auth",
  headers:{
    Accept : "application/json",
  },
  withCredentials: true,
})
export default authAPI;
