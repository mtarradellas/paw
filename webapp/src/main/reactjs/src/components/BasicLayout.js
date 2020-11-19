import React from 'react';
import Header from "./Header";
import Footer from "./Footer";

function BasicLayout({children}){
    return <>
        <Header/>
        {children}
        <Footer/>
    </>;
}

export default BasicLayout;