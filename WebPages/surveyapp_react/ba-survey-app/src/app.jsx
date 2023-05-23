import React from 'react';
import Header from "./components/header";
import Main from "./components/main";
import { Fragment } from "react";

export default function App(props) {
    const { token } = props;

    return (
        <Fragment>
            <Header />
            <Main token={token}/>
        </Fragment>
    );
}


