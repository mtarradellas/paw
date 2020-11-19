import React from 'react';
import Header from "./Header";
import Footer from "./Footer";

function BasicLayout({children}){
    return <div className={"basicLayout"}>
        <Header/>
        {children}
        <Footer/>
    </div>;
}

export default BasicLayout;