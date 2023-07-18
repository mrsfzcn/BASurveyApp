import { useState } from "react";
import Input from "../../components/Input";
import Button from "../../components/Button";
import Sidebar from "../../components/Sidebar";
import { useNavigate } from "react-router-dom";
import SurvayService from "../../services/SurveyService.js"
import icon from "../../assets/icons/icon.png";
import HeaderComponent from '../../utils/header/header';
import "./createsurvey.css"

function CreateSurvay(){
    const [surveyTitle, setSurveyTitle] = useState("");
    const [courseTopic, setCourseTopic] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleTitleChange = (e) => {
        setSurveyTitle(e.target.value);
    }

    const handleTopicChange = (e) => {
        setCourseTopic(e.target.value);
    }
    const handleSubmit =(e) => {
        e.preventDefault();
        setError("");

        if(!surveyTitle || !courseTopic){
            setError("Lütfen tüm alanları doldurunuz!");
            return;
        }

        const createSurvayData ={
            surveyTitle,
            courseTopic,
        };
        console.log(createSurvayData);
        SurvayService.create(createSurvayData)
        .then((response) => {
            console.log(response.data);
            if(response.data==true){
                navigate("/")
            }
        })
        .catch((error) => {
            console.log(error);
            setError("Bir hata oluştu anket bilgilerini kontrol ediniz.")
        });


    };

    return (
        <> 
        <HeaderComponent />
             <div className="flex min-h-screen ">
          <Sidebar />
         
          <div className="flex-[8_8_0%] flex justify-center items-center">
          <div className="flex justify-center items-center bg-gray-300 w-9/12 h-5/6  ">
              <div className="flex bg-white justify-center items-center w-9/12 h-5/6" >
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
        <div className="flex justify-center items-center">
        <label htmlFor="submitButton" className="mr-2 text-lg font-bold font-poppins">Soru ekle/düzenle</label>
              <Button primary circle fat onClick={handleSubmit} id="submitButton">
                <img src={icon} alt="Icon" className="w-8 h-10" />
              </Button>
            </div>
            <Button secondary rounded  onClick={handleSubmit} id="submitButton">
                VAZGEÇ
              </Button>
                </form>
              </div>
            </div>
          </div>
        </div>
        </>
   
      );
      
      
}

export default CreateSurvay;