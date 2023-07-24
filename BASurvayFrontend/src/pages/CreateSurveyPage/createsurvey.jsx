import { useState } from "react";
import Input from "../../components/Input";
import Button from "../../components/Button";
import Layout from "../../components/Layout";
import { useNavigate } from "react-router-dom";
import SurveyService from "../../services/SurveyService.js";
import icon from "../../assets/icons/icon.png";
import "./createsurvey.css";

function CreateSurvey() {
  const [surveyTitle, setSurveyTitle] = useState("");
  const [courseTopic, setCourseTopic] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleTitleChange = (e) => {
    setSurveyTitle(e.target.value);
  };

  const handleTopicChange = (e) => {
    setCourseTopic(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!surveyTitle || !courseTopic) {
      setError("Lütfen tüm alanları doldurunuz!");
      return;
    }

    const createSurveyData = {
      surveyTitle,
      courseTopic,
    };

    SurveyService.create(createSurveyData)
      .then((response) => {
        const surveyOid = response.data.surveyOid; 
        navigate("/addquestion", { state: { surveyTitle, surveyOid } }); 
        console.log(surveyOid)

      })
      .catch((error) => {
        console.log(error.response);
        if (error.response && error.response.data && error.response.data.customMessage) {
          setError(error.response.data.customMessage);
        } else {
          setError("Bir hata oluştu!");
        }
      });
  };

  return (
    <Layout>
      <div className="flex h-full ">
        <div className="flex-[8_8_0%] flex  justify-center items-center ">
          <div className="flex justify-center items-center bg-gray-300 w-11/12 h-5/6 rounded">
            <div className="flex bg-white justify-center items-center w-9/12 h-5/6 rounded">
              <form className="space-y-8 ">
                <Input
                  type="text"
                  placeholder="Yeni anket adını yazınız"
                  value={surveyTitle}
                  onChange={handleTitleChange}
                  className="w-full"
                />
                <Input
                  type="text"
                  placeholder="Konu başlığını yazınız"
                  value={courseTopic}
                  onChange={handleTopicChange}
                  className="w-full"
                />
                {error && <p>{error}</p>}
                <div className="flex justify-center items-center">
                  <label htmlFor="submitButton" className="mr-2 text-lg font-bold font-poppins">
                    Soru ekle/düzenle
                  </label>
                  <Button primary circle fat onClick={handleSubmit} id="submitButton">
                    <img src={icon} alt="Icon" className="w-8 h-10" />
                  </Button>
                </div>
                <Button secondary rounded onClick={() => navigate("/adminhome")}>
                  VAZGEÇ
                </Button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}

export default CreateSurvey;
