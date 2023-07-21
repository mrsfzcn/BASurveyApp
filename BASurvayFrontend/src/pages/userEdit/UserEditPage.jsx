import React, { useState, useEffect } from 'react';
import axios from "axios";
import ContentHeading from '../../components/ContentHeading';
import Sidebar from '../../components/Sidebar';
import "./UserEditPage.css"

const UserEditPage = () => {
  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [mail, setMail] = useState("");
  const [role, setRole] = useState("");
  const [userId] = useState(2); // Geçici olarak konulmuştur localstorege'a eklendiğinde kendiliğinden veriyi çekecektir.
  // const userId1 = localStorage.getItem("userId");
  const [userEmail, setUserEmail] = useState(""); 
  const token = localStorage.getItem("token");
  const [initialUserData, setInitialUserData] = useState({
    name: "",
    surname: "",
    mail: "",
    role: "",
  });
  

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
  
      const response = await axios.get(`http://localhost:8090/api/v1/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
  
      const data = response.data;
  
      setName(data.firstName);
      setSurname(data.lastName);
      setMail(data.email);
      setUserEmail(data.email);
      setRole(data.authorizedRole);

      setInitialUserData({
        name: data.firstName,
        surname: data.lastName,
        mail: data.email,
        role: data.authorizedRole,
      });
    } catch (error) {
      console.error("Error fetching data:", error);
      console.log(error);
    }
  };
  
  const handleChange = (event) => {
    const { id, value } = event.target;
    
    if (id === "userEmail") {
      setUserEmail(value);
    }
  };

  const handleCancel = () => {
    setName(initialUserData.name);
    setSurname(initialUserData.surname);
    setMail(initialUserData.mail);
    setRole(initialUserData.role);
  };

  const namehandleChange = (event) => {
     event.preventDefault
      setName(event.target.value);
  };
  const surnamehandleChange = (event) => {
     event.preventDefault
      setSurname(event.target.value);
  };
  const mailhandleChange = (event) => {
     event.preventDefault
      setMail(event.target.value);
  };
  const rolehandleChange = (event) => {
     event.preventDefault
      setRole(event.target.value);
  };
  
const handleSubmit = async () => {

  const firstName = name;
  const lastName = surname;
  const email = mail;
  const authorizedRole = role;

     axios.put(
      `http://localhost:8090/api/v1/user/update/${userEmail}`,
      {
        firstName:name,
        lastName:surname,
        email:mail,
        authorizedRole:role,
      },
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    ).then((response) => {  console.log("Update successful:", response.data)})
    .catch((error) => {console.error("Error updating data:", error)})
    console.log(name);
    console.log(firstName);
    console.log(lastName);
    };

    

  return (
    <div className='user-edit-page'>
      <div className='sidebar'>
        <Sidebar />
      </div>
      <div className='outer-react'>
        <ContentHeading />
        <div className='inner-react'>
          <div className="inner2-react">
            <div className="inputs">
              <input type="text" id='name' value={name} onChange={namehandleChange} />
              <input type="text" id='surname' value={surname} onChange={surnamehandleChange} />
              <input type="text" id='mail' value={mail} onChange={mailhandleChange} />
              <input type="text" id='role' value={role} onChange={rolehandleChange} />
            </div>
            <div className='buttons'>
              <button className='kaydet' onClick={handleSubmit}>KAYDET</button>
              <button className='vazgec'onClick={handleCancel}>VAZGEÇ</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserEditPage;