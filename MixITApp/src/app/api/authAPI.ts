import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8080/v1/users",
  headers:{
    Accept : "application/json",
  }
})
