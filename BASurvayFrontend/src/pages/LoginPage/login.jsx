import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../LoginPage/login.css";
import AuthService from "../../services/AuthService.js";
import vektor5 from "../../assets/images/Login/Vector5.png";
import vektor6 from "../../assets/images/Login/Vector6.png";
import vektor7 from "../../assets/images/Login/Vector7.png";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const validateEmail = (email) => {
    const re =
      /^[a-z0-9]((\.|\+)?[a-z0-9]){1,}@bilgeadam(boost)?(akademi)?\.com$/;
    return re.test(String(email).toLowerCase());
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError("");

    if (!email || !password) {
      setError("Lütfen tüm alanları doldurunuz.");
      return;
    }

    if (!validateEmail(email)) {
      setError("Lütfen geçerli bir e-mail adresi giriniz.");
      return;
    }
    if (password.length < 8) {
      setError("Şifreniz en az 8 karakter uzunluğunda olmalıdır.");
      return;
    }
    const loginData = {
      email,
      password,
    };
    AuthService.login(loginData)
      .then((response) => {
        console.log(response.data);
        localStorage.setItem("token", response.data.token);
        if (response.data.qrCode === null) {
          navigate("/code");
        } else navigate("/qrcode", { state: { qrCode: response.data.qrCode } });
      })
      .catch((error) => {
        console.error(error);
        setError("Giriş yapılamadı. Lütfen bilgilerinizi kontrol ediniz.");
      });
  };

  return (
    <div className="main">
      <div className="content">
      <img className="vektor5" src={vektor5} alt="vektor5" />
      <img className="vektor6" src={vektor6} alt="vektor6" />
      <img className="vektor7" src={vektor7} alt="vektor7" />
            <div className="user-menu">
              <label id="email" className="username">
                Kullanıcı Adı:
              </label>{" "}
              <br />
              <input
                className="placeholder-input"
                type="email"
                id="email"
                placeholder="Kullanıcı adınızı giriniz."
                value={email}
                onChange={handleEmailChange}
              />
              <br />
              <br />
              <label className="password">Şifre:</label> <br />
              <input
                className="placeholder-input"
                type="password"
                id="password"
                placeholder="Şifrenizi giriniz."
                value={password}
                onChange={handlePasswordChange}
              />
              
              {error && <p>{error}</p>}
              <br />
              <br />
              <div className="cbottom">
                <button className="button" onClick={handleSubmit}>
                  GİRİŞ
                </button>
              </div>
            </div>

      </div>
    </div>
  );
}

export default Login;
