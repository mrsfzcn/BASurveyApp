import React, { useState } from "react";
import Sidebar from "./sidebar";
import Content from "./content";

export default function Main() {
    const [selectedSubMenu, setSelectedSubMenu] = useState("");
    const handleSubMenuClick = (submenu) => {
            setSelectedSubMenu(submenu);
    }
    
    return (
      <main>
        <Sidebar onSubMenuClick={handleSubMenuClick} />
        <Content selectedSubMenu={selectedSubMenu} />
      </main>
    );
  }