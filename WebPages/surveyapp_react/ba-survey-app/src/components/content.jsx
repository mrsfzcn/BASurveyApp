import React from "react";
import QuestionTag from "./questionTag";
import ClassroomTag from "./classroomTag";
import QuestionType from "./questionType";
import SurveyTag from "./surveyTag";
import CreateSurvey from "./createSurvey";
import AddUser from "./addUser";
import AllUser from "./allUser.jsx";
import SendSurveyPassive from "./sendSurveyPassive.jsx";
import AllStudents from "./allStudents.jsx";
import AllTrainers from "./allTrainers.jsx";
import AssignStudentPassive from "./assignStudentPassive.jsx";

export default function Content({ selectedSubMenu, token }) {
    let content = null;

    switch (selectedSubMenu) {
        case "all-users":
            content = <AllUser/>
            break;
        case "add-user":
            content = <AddUser />
            break;
        case "edit-user":
            content = (
                <div>
                    <h2>Kullanıcı Düzenle</h2>
                    <p>Bu alanda var olan kullanıcılar düzenlenebilir.</p>
                </div>
            );
            break;
        case "all-surveys":
            content = (
                <div>
                    <h2>Tüm Anketler</h2>
                    <p>Bu alanda tüm anketler listelenir.</p>
                </div>
            );
            break;
        case "create-survey":
            content = <CreateSurvey/>
            break;
        case "send-survey":
            content = <SendSurveyPassive/>
            break;
        case "survey-tags":
            content = <SurveyTag token={token}/>
            break;
        case "list-questions":
            content = (
                <div>
                    <h2>Soruları Listeleme</h2>
                    <p>Bu alanda tüm sorular listelenir.</p>
                </div>
            );
            break;
        case "add-question":
            content = (
                <div>
                    <h2>Soru Ekleme</h2>
                    <p>Bu alanda soru eklenir.</p>
                </div>
            );
            break;
        case "question-tags":
            content = <QuestionTag token={token}/>
            break;
        case "question-types":
            content = <QuestionType />
            break;
        case "all-students":
            content = <AllStudents/>
            break;
        case "all-trainers":
            content = <AllTrainers/>
            break;
        case "classroom-tags":
            content = <ClassroomTag token={token}/>
            break;
        case "assign-classroom":
            content = <AssignStudentPassive/>
            break;
        case "reporting-1":
            content = (
                <div>
                    <h2>Raporlama-1 İşlemi</h2>
                    <p>Bu alanda raporlama-1 işlemi yapılır.</p>
                </div>
            );
            break;
        case "reporting-2":
            content = (
                <div>
                    <h2>Raporlama-2 İşlemi</h2>
                    <p>Bu alanda raporlama-2 işlemi yapılır.</p>
                </div>
            );
            break;
        case "reporting-3":
            content = (
                <div>
                    <h2>Raporlama-3 İşlemi</h2>
                    <p>Bu alanda raporlama-3 işlemi yapılır.</p>
                </div>
            );
            break;
        default:
            content = (
                <div>
                    <h2>Hoşgeldiniz!</h2>
                    <p>Lütfen bir menü seçeneği seçin.</p>
                </div>
            );
    }

    return <div className="content">{content}</div>;
}
