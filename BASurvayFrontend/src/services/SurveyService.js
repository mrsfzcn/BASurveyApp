import axios from "axios";

const CREATE = "http://localhost:8090/api/v1/survey/create";
const QUESTIONS = "http://localhost:8090/api/v1/questions";

class SurveyService {
  create(survey) {
    const token = localStorage.getItem("token");
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
    return axios.post(CREATE, survey, config);
  }

  getQuestions() {
    const token = localStorage.getItem("token");
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
    return axios.get(QUESTIONS, config);
  }
}

export default new SurveyService();