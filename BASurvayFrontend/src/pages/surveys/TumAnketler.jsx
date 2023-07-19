import React, { useState } from 'react'
import Table from '../../components/Table/Table'
import Sidebar from "../../components/Sidebar";

import "./TumAnketler.css";
import Layout from '../../components/Layout';
const TumAnketler = () => {


  const [surveys,setSurveys] = useState([
    {
      no: "1",
      anketAdi: "Boost Değerlendirme anketi - JAVA",
      konuBasligi: "java",
      anketEtiket: "etiket iconu",
    },
    {
      no: "2",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "3",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "4",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "5",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "6",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "7",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "8",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "9",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "10",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "11",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },

    {
      no: "12",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "13",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "14",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "15",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "16",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "17",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
    {
      no: "18",
      anketAdi: "Boost Değerlendirme anketi - .NET",
      konuBasligi: ".NET",
      anketEtiket: "etiket iconu",
    },
  ]);

  const header = [
    "No",
    "Anket Adı",
    // "Anket Etiketleri",
    "Konu Başlığı",

  ];

  const deleteTableRows = (index) => {
    const rows = [...surveys];
    rows.splice(index,1);
    setSurveys(rows);
  }
 
  const handleChange = (index,evnt) => {
    const {name,value} = evnt.target;
    const rowsInput = [...surveys];
    rowsInput[index][name] = value;
    setSurveys(rowsInput);
  }

  return (

    <Layout>
    <div className='flex flex-col  gap-10 bg-slate-100 h-full'>
      <div className='h-10'>dasda</div>
      <Table  data={surveys} header={header} useIcon={true} useLabel={true} deleteTableRows={deleteTableRows} handleChange={handleChange} />
      </div>
    </Layout>


    
   
  )
}

export default TumAnketler;