import axios from "axios";

const CREATE = "http://localhost:8090/api/v1/survey/create";

class SurvayService {
  create(survay) {
    const token = localStorage.getItem("token");
    const config = {
      headers: {
        Authorization: `Bearer ${token}`, 
      },
    };
    return axios.post(CREATE, survay, config);
  }
}

export default new SurvayService();