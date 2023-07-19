import React, { useState } from "react";
import Button from "../../components/Button";
import Input from "../../components/Input";
import Layout from "../../components/Layout";
import "./SendSurvey.css";
import Dropdown from "../../components/Dropdown";

const SendSurvey = () => {
  const [data, setUser] = useState({
    surveyname: "Anket Adı",
    surveynameis: "UI / UX ve Dijital Tasarım Eğitimi Değerlendirme Anketi",
    surveytitle: "Konu Başlığı",
    titleanswer: "UI / UX",
    colon: ":",
    classtosend: "Atanacağı Sınıf",
  });

  const [selection, setSelection] = useState(null);
  const options = [
    { label: "JAVA-1", value: "JAVA-1" },
    { label: "JAVA-2", value: "JAVA-2" },
    { label: "JAVA-3", value: "JAVA-3" },
    { label: "JAVA-4", value: "JAVA-4" },
    { label: "JAVA-5", value: "JAVA-5" },
  ];
  const onChange = (option) => {
    setSelection(option);
    setUser({ ...user, roles: [option.value] });
  };

  const handleCreate = (event) => {
    event.preventDefault();
    console.log(user);
    console.log(typeof user.roles);
    AuthService.register(user)
      .then(() => {
        alert("başarılı");
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <Layout>
      <div className="flex flex-col bg-[#E5E5E5] h-full">
        <div className="p-5 text-start">Bitmesi bekleniyor</div>
        <div className="outerclass flex justify-center align-center">
          <div className="innerclass bg-[#F1F1F1] flex  justify-center align-center m-auto ">
            <div className="insideclass bg-[#FEFEFE] m-auto  flex flex-col justify-center gap-14 ">
              <form
                onSubmit={handleCreate}
                className=" flex flex-col justify-center items-center gap-14"
              >
                <div className="flex flex-col justify-center gap-14">
                  <div className="flex flex-row gap-5 justify-between items-center p-7 mt-0">
                    <Input
                      className=""
                      value={data.surveynameis}
                      eigthypercent
                    ></Input>
                    <Button className="bg-[#e0e0e0] " rounded bold>
                      Bul
                    </Button>
                  </div>
                  <div className="flex flex-col gap-5">
                    <div className="flex ">
                      <p className=" w-32 text-left font-semibold">{data.surveyname}</p>
                      <p className=" w-8 align-middle">{data.colon}</p>
                      <p className=" w-128">{data.surveynameis}</p>
                    </div>
                    <div className="flex">
                      <p className=" w-32 text-left font-semibold" >{data.surveytitle}</p>
                      <p className=" w-8 align-middle">{data.colon}</p>
                      <p className=" w-128">{data.titleanswer}</p>
                    </div>
                    <div className="flex items-center font-semibold">
                      <p className=" w-32 text-left">{data.classtosend}</p>
                      <p className=" w-8 align-middle">{data.colon}</p>
                      <Dropdown
                        className=" w-128"
                        options={options}
                        onChange={onChange}
                        value={selection}
                      />
                    </div>
                  </div>
                  <div className="flex gap-7 justify-center flex-wrap mt-20 mb-10">
                  <Button className="" primary rounded bold>
                    ATAMA YAP
                  </Button>
                  <Button className="" secondary rounded bold>
                    VAZGEÇ
                  </Button>
                </div>
                </div>

              </form>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default SendSurvey;
