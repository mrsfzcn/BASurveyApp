import axios from "axios";

const CREATE = "http://localhost:8090/api/v1/survey/create";
const QUESTIONS = "http://localhost:8090/api/v1/questions";
const ADD_QUESTIONS_TO_SURVEY = "http://localhost:8090/api/v1/survey/add-questions-to-survey";

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

  addQuestionsToSurvey(addQuestion) {
    const token = localStorage.getItem("token");
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    };
    
    return axios.post(ADD_QUESTIONS_TO_SURVEY, addQuestion, config);
  }
}

export default new SurveyService();
