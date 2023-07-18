import React, { useState } from "react";
import "./header.css";
import logoImage from "../../assets/images/Login/bilgeAdamAkademi.png";

const HeaderComponent = () => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isProfileOpen, setIsProfileOpen] = useState(false);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleProfile = () => {
    setIsProfileOpen(!isProfileOpen);
  };

  return (
    <header>
      <section className="header-content">
        <h1 className="brand-logo">
          <a href="#" className="logo">
            <img src={logoImage} alt="Logo" />
            <div className="logo-line"></div>
            <span className="logo-text">Survey App</span>
          </a>
        </h1>
        <div className="circle-button" onClick={toggleProfile}></div>
      </section>
      {isProfileOpen && (
        <div className="profile-popup">
          <ul>
            <li>Profil bilgileri</li>
            <li>Rol Değiştir</li>
            <li>Çıkış</li>
          </ul>
        </div>
      )}
    </header>
  );
};

export default HeaderComponent;