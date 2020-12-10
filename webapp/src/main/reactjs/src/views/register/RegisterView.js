import React from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import RegisterForm from "./RegisterForm";
import {useTranslation} from "react-i18next";

function RegisterView(){
    const {t} = useTranslation("register");

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <RegisterForm/>
    </SmallCenteredContent>
}

export default RegisterView;