import React, { useState } from "react";
import Button from "../../components/Button";
import Input from "../../components/Input";
import Layout from "../../components/Layout";
import "./userRegistration.css";
import Dropdown from "../../components/Dropdown";
import AuthService from "../../services/AuthService"

const UserRegistration = () => {

  const[user,setUser] = useState({
    password: "",
    firstName: "",
    lastName: "",
    roles:[
      
    ]
  })
  
  const [selection, setSelection] = useState(null);
  const options =[
    {label:"ADMIN", value:"ADMIN"},
    {label:"MANAGER", value:"MANAGER"},
    {label:"STUDENT", value:"STUDENT"},
    {label:"MASTER_TRAINER", value:"MASTER_TRAINER"},
    {label:"ASSISTANT_TRAINER", value:"ASSISTANT_TRAINER"}, 
  ];
  const onChange = (option) => {
    setSelection(option);
    setUser({...user,roles:[option.value]});
  };

  const handleCreate = (event) =>{
    event.preventDefault();
    console.log(user);
    console.log(typeof user.roles);
    AuthService.register(user).then(()=>{
      alert("başarılı");
    })
    .catch((error)=>{
      console.log(error);
    })

  }

  return (
    <Layout>
      <div className="flex flex-col bg-[#E5E5E5] h-full">
        <div className="p-5 text-start">
          Bitmesi bekleniyor
        </div>
        <div className="class1  flex justify-center align-center">
          <div className="class2  bg-[#F1F1F1] flex  justify-center align-center m-auto ">
            <div className="class3  bg-[#FEFEFE] m-auto  flex flex-col justify-center gap-14 ">
              <form onSubmit={handleCreate} className=" flex flex-col justify-center gap-14">
              <div className="flex flex-col gap-14 justify-center items-center">
               
                <Input placeholder="Ad" half required onChange={(e=>setUser({...user,firstName: e.target.value}))}/>
                <Input placeholder="Soyad" half required onChange={(e=>setUser({...user,lastName: e.target.value}))}/>
                <Input placeholder="Şifre" half required onChange={(e=>setUser({...user,password: e.target.value}))}/>
                <div className="w-1/2 flex justify-start">
                  <Dropdown options={options} onChange={onChange} value={selection}/>
                </div>
              </div>
              <div className="flex justify-center gap-7 flex-wrap">
                <Button className="" primary rounded bold>
                  KAYDET
                </Button>
                <Button className="" secondary rounded bold>
                  VAZGEÇ
                </Button>
              </div>
              </form>
            </div>
          </div>
        </div>
      </div>
      
    </Layout>
  );
};

export default UserRegistration;
