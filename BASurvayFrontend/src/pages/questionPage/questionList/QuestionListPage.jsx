import React, { useEffect, useState } from 'react';
import axios from "axios";
import "./questionListPage.css";
import Table from '../../../components/questionTable/Table';
import Layout from '../../../components/Layout'

function QuestionListPage() {
  const [surveys, setSurveys] = useState([]);
  const header = ["No", "Soru", "Soru Etiketleri", "Soru Tipi", "İşlemler"];
  const token = localStorage.getItem("token");
  console.log("merhaba");
  // http://localhost:8090/api/v1/questions/findallquestion
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8090/api/v1/questions/findallquestion`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setSurveys(response.data);
        console.log(response.data);
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, []);
  
  return (
    <Layout>

        <div className='flex flex-col  gap-10 bg-slate-100 h-full'>
        <div className='h-10'>Is your name red?</div>
        <Table  data={surveys} header={header} useIcon={true} useLabel={true}/>
          </div>
        
        </Layout>
  )
}

export default QuestionListPage