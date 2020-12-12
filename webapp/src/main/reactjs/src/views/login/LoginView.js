import React from 'react';
import {useTranslation} from "react-i18next";
import SmallCenteredContent from "../../components/SmallCenteredContent";
import LoginForm from "./LoginForm";

function LoginView(){
    const {t} = useTranslation("login");

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <LoginForm/>
    </SmallCenteredContent>
}

export default LoginView;