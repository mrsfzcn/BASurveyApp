import { useState } from "react";
import reactLogo from "./assets/react.svg";
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
import CreateSurveyPage from "./pages/CreateSurveyPage/createsurvey";
import AdminHomePage from "./pages/adminHome/AdminHomePage";
import QuestionListPage from "./pages/questionPage/questionList/QuestionListPage";
import AddTag from "./pages/tag/AddTag";
import TagsTable from "./pages/tag/TagsTable";
import UserRegistration from "./pages/user/UserRegistration";
import TumKullanicilar from "./pages/user/TumKullanicilar";
import AddQuestion from "./pages/CreateSurveyPage/AddQuestion";
import SendSurvey from "./pages/sendSurvey/SendSurvey";
import TumAnketler from "./pages/surveys/TumAnketler";
import AnketEkle from "./pages/surveys/AnketEkle";
import AnketDuzenle from "./pages/surveys/AnketDuzenle";
import UpdateTag from "./pages/tag/UpdateTag";
import PreviewSurvey from "./pages/CreateSurveyPage/PreviewSurvey";

function App() {
  const [count, setCount] = useState(0);

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/qrcode" element={<Qrcode />} />
        <Route path="/code" element={<Code />} />
        <Route path="/adminhome" element={<AdminHomePage />} />
        <Route path="/questionlist" element={<QuestionListPage/>}/>
        <Route path="/sendsurvey" element={<SendSurvey />} />
        <Route path="/createsurvey" element={<CreateSurveyPage />} />
        <Route path="/addquestion" element={<AddQuestion />} />
        <Route path="/preview" element={<PreviewSurvey />} />
        <Route path="/*" element={<Navigate to="/" />} />
        <Route path="/etiket">
          <Route index element={<TagsTable />} />
          <Route path="guncelle">
            <Route index element={<UpdateTag />} />
          </Route>
        </Route>
        <Route path="/kullanici">
          <Route index element={<TumKullanicilar />} />
          <Route path="ekle">
            <Route index element={<UserRegistration />} />
          </Route>
        </Route>
        <Route path="/anketler">
          <Route index element={<TumAnketler />} />
          <Route path="guncelle/:id">
            <Route index element={<AnketDuzenle />} />
          </Route>
          <Route path="ekle">
            <Route index element={<AnketEkle />} />
          </Route>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
