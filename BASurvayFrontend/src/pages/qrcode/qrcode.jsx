import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import AuthService from "../../services/AuthService";
import TokenService from '../../services/TokenService';
import { useNavigate } from "react-router-dom";

const QrCode = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [error, setError] = useState("");
  const qrCode = location.state?.qrCode;
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
    <div className="content">
      <div className="user-menu">
        <img src={qrCode} alt="" />
        <label htmlFor="qrcode" className="username">
          2FA QR CODE
        </label>
        <br />
        <input
          type="text"
          className="placeholder-input"
          onChange={handleTwoFactoryChange}
        />
      </div>
      {error && <p>{error}</p>}
      <div className="cbottom">
        <button className="button" onClick={handleSubmit}>
          Gönder
        </button>
      </div>
    </div>
  );
};

export default QrCode;
