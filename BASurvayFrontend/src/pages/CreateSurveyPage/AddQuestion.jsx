import { useState, useEffect } from "react";
import Layout from "../../components/Layout";
import Button from "../../components/Button";
import { useNavigate } from "react-router-dom";
import SurveyService from "../../services/SurveyService.js"
import "./createsurvey.css"
import { useLocation } from "react-router-dom";

function AddQuestion(){
  const location = useLocation();
  const surveyTitle = location.state.surveyTitle;
  const [questions, setQuestions] = useState([]);
  const [selectedQuestions, setSelectedQuestions] = useState([]);
  const navigate = useNavigate();

  const navigateMain = (e) =>{
    navigate("/adminhome");
  }
  useEffect(() => {
    SurveyService.getQuestions()
      .then(response => {
        setQuestions(response.data);
      })
      .catch(error => {
        console.error("Sorular listelenirken bir hata oldu.", error);
      });
  }, []);

  function handleQuestionClick(question) {
    setQuestions(questions.filter(q => q.questionOid !== question.questionOid));
    setSelectedQuestions([...selectedQuestions, question]);
  }

  function handleSelectedQuestionClick(question) {
    setSelectedQuestions(selectedQuestions.filter(q => q.questionOid !== question.questionOid));
    setQuestions([...questions, question]);
  }

  return (
    <Layout>
      <div className="flex h-full justify-center items-center flex-col">
        <div className="bg-gray-300 w-11/12 h-5/6 flex items-center justify-center rounded">
          <div className="bg-white h-5/6 w-2/5 m-8 rounded flex flex-col">
            <div className="flex flex-row ">
                 <h2 className="text-left font-bold m-2 p-2">Anket Adı: </h2>
                 <p className="m-2 p-2">{surveyTitle}</p>
            </div>
           
            {selectedQuestions.map((question, index) => (
              <div key={index} className="bg-green-400 m-2 p-2 rounded-full"onClick={() => handleSelectedQuestionClick(question)}>
                <p>{question.questionString}</p>
              </div>
            ))}
          </div>
          <div className="bg-white h-5/6 w-2/5 m-8 rounded flex flex-col  overflow-auto">
          <h2 className="text-left font-bold m-2 p-2">Sorular </h2>
            {questions.map((question, index) => (
              <div key={index} className="bg-green-400 m-2 p-2 rounded-full" onClick={() => handleQuestionClick(question)}>
                <p>{question.questionString}</p>
              </div>
            ))}
          </div>
        </div>
        <div className="flex flex-row gap-8">
        <Button primary rounded className="mt-8 ">
          KAYDET
        </Button>
        <Button secondary rounded className="mt-8" onClick= {navigateMain}>
          VAZGEÇ
        </Button>
        </div>
      </div>
    </Layout>
  );
}

export default AddQuestion;
