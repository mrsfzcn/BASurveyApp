import React, { useState } from "react";
import Sidebar from "./sidebar";
import Content from "./content";

export default function Main({token}) {
    const [selectedSubMenu, setSelectedSubMenu] = useState("");

    const handleSubMenuClick = (submenu) => {
        setSelectedSubMenu(submenu);
    };

    return (
        <main>
            <Sidebar onSubMenuClick={handleSubMenuClick} token={token}/>
            <Content selectedSubMenu={selectedSubMenu} token={token} />
        </main>
    );
}