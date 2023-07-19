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
import CreateSurveyPage from "./pages/CreateSurveyPage/createsurvey"
import AddTag from "./pages/tag/AddTag";
import TagsTable from "./pages/tag/TagsTable";
import UserRegistration from "./pages/user/UserRegistration";
import TumKullanicilar from "./pages/user/TumKullanicilar";
import SendSurvey from "./pages/sendSurvey/SendSurvey";

function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/qrcode" element={<Qrcode />} />
        <Route path="/code" element={<Code />} />
        <Route path="/adminhome" element={<AdminHomePage />} />
        <Route path="/sendsurvey" element={<SendSurvey />} />
        <Route path="/createsurvey" element={<CreateSurveyPage />} />
        <Route path="/*" element={<Navigate to="/" />} />
        <Route path="/etiket">
          <Route index element={<TagsTable />} />
          <Route path="guncelle">
            <Route index element={<AddTag />} />
          </Route>
        </Route>
        <Route path="/kullanici">
          <Route index element={<TumKullanicilar />} />
          <Route path="ekle">
            <Route index element={<UserRegistration />} />
          </Route>
        </Route>

      </Routes>
    </Router>
  );
}

export default App;
