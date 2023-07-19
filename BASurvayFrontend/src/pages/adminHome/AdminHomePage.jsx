// AdminHome.js
import React, { useState } from "react";
import Layout from "../../components/Layout";
import Dropdown from "../../components/Dropdown";
import "../adminHome/adminhomepage.css";
import ReportingSvg from "./reportingSvg";
import ClassroomSvg from "./classroomSvg";
import SurveySvg from "./surveySvg";
import UserGroupSvg from "./userGroupSvg";
import QuestionSvg from "./questionSvg";

function AdminHome() {
  const [name, setName] = useState("");
  const [selection, setSelection] = useState(null);

  const handleChange = (e) => {
    setName(e.target.value);
  };

  const handleUserClick = (event) => {
    const target = event.target;
      console.log("User area clicked!");
    
  };

  const handleSurveyClick = (event) => {
    const target = event.target;
    
      console.log("Survey area clicked!");
    
  };
  const handleReportingClick = (event) => {
    const target = event.target;
    
      console.log("Reporting area clicked!");
   
  };
  const handleQuestionClick = (event) => {
    const target = event.target;
    
      console.log("Question area clicked!");
   
  };
  const handleClassroomClick = (event) => {
    const target = event.target;
   
      console.log("Classroom area clicked!");
  
  };

  const options = [
    { label: "Red", value: "red" },
    { label: "Green", value: "green" },
    { label: "Blue", value: "blue" },
  ];

  const onChange = (option) => {
    setSelection(option);
  };

  return (
    <Layout>
      <div className="flex">
        <Dropdown value={selection} onChange={onChange} options={options} />
        <Dropdown value={selection} onChange={onChange} options={options} />
      </div>
      <div className="question-container" onClick={handleQuestionClick}>
   <QuestionSvg/>
    </div>
    <div className="classroom-container" onClick={handleClassroomClick}>
    <ClassroomSvg/>
    </div>
    <div className="survey-container" onClick={handleSurveyClick}>
    <SurveySvg/> 
    </div>
    <div className="user-container" onClick={handleUserClick}>  
    <UserGroupSvg/> 
    </div>
    <div className="circle-reporting" onClick={handleReportingClick}> 
    <div className="svg-reporting" >
      <ReportingSvg/>
      </div>
      <div>
      <p>Raporlama İşlemleri</p>
      </div>
    </div>
    </Layout>
  );
}

export default AdminHome;
