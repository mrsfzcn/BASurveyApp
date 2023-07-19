import React from "react";
import Sidebar from "./Sidebar";
import Header from "../components/header/Header";

const Layout = ({ children }) => {
  return (
    <div className="flex min-h-screen max-h-full">
      <Sidebar />
      <div className="flex flex-col flex-[8_8_0%] max-h-screen">
        <Header />
        <div className="h-full">{children}</div>
      </div>
    </div>
  );
};

export default Layout;
