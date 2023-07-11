import React, { useState } from "react";
import { createRoot } from "react-dom/client";
import "./assets/styles/index.css";
import App from "./app";
import Login from "./login";

const rootEl = document.getElementById("root");

const Root = () => {
  const [loggedIn, setLoggedIn] = useState(false);
  const [token, setToken] = useState(null);

  const handleLogin = async (email, password) => {
    try {
      const requestData = {
        email: email,
        password: password,
      };

      const response = await fetch("http://localhost:8090/auth/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        const data = await response.json();
        console.log(data);
        setToken(data?.token);
        // setLoggedIn(true);
      } else {
        throw new Error("Login request failed");
      }
    } catch (error) {
      console.error(error);
    }
  };

  return loggedIn ? <App token={token} /> : <Login onLogin={handleLogin} />;
};

createRoot(rootEl).render(<Root />);
