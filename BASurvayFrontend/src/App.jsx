import { useState } from "react";
import reactLogo from "./assets/react.svg";

import "./App.css";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Link,
  Navigate,
} from "react-router-dom";
import Login from "./pages/LoginPage/login";
import Qrcode from "./pages/qrcode/qrcode";
import Code from "./pages/code/Code";
import AdminHomePage from "./pages/adminHome/AdminHomePage"

function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/qrcode" element={<Qrcode />} />
        <Route path="/code" element={<Code />} />
        <Route path="/adminhome" element={<AdminHomePage />} />
        <Route path="/*" element={<Navigate to="/" />} />
      </Routes>
    </Router>
  );
}

export default App;
