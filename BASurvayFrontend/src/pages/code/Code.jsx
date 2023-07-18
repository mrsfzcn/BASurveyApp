import React, { useState } from "react";
import "./code.css";
import AuthService from "../../services/AuthService";
import TokenService from '../../services/TokenService';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import HeaderComponent from '../../utils/header/header';

const Code = () => {
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const [code, setCode] = useState({
    twoFactoryKey: "",
    token: localStorage.getItem("token") || "",
  });

  
  const handleTwoFactoryChange = (e) => {
    setCode({ ...code, twoFactoryKey: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!code.twoFactoryKey) {
      setError("Lütfen tüm alanları doldurunuz.");
      return;
    }

    if (code.twoFactoryKey.length !== 6) {
      setError("2FA Kodunuz 6 haneli olmalıdır.");
      return;
    }

    AuthService.verifyCode(code)
      .then((response) => {
        if (response.data === true) {
          const decodedToken = TokenService.decodeToken(code.token);
          if (decodedToken && decodedToken.role === 'ADMIN') {
            navigate("/adminhome");
          } 
        } else {
          alert("hata var");
        }
      })
      .catch((error) => {
        console.error(error);
        setError("Giriş yapılamadı. Lütfen bilgilerinizi kontrol ediniz.");
      });
  };

  return (
<>

<HeaderComponent />
    <div className="content-code">
      <div className="user-menu">
        <label htmlFor="code" className="username">
          2FA Code
        </label>
        <br />
        <input
          className="placeholder-input"
          type="text"
          id="code"
          placeholder="2FA Code"
          value={code.twoFactoryKey}
          onChange={handleTwoFactoryChange}
        />
      </div>
      {error && <p>{error}</p>}
      <div className="cbottom">
        <button className="button" onClick={handleSubmit}>
          Gönder
        </button>
        <Link to="/login">
          <button className="button" >
            Giriş sayfasına geri dön
          </button>
        </Link>
      </div>
    </div>
    </>
  );
};

export default Code;
