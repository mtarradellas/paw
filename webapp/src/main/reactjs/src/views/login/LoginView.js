import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import SmallCenteredContent from "../../components/SmallCenteredContent";
import LoginForm from "./LoginForm";
import {login, LOGIN_ERRORS} from "../api/authentication";
import {message} from "antd";

function LoginView(){
    const {t} = useTranslation("login");
    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async ({username, password}, {setErrors}) => {
        setSubmitting(true);
        try{
            await login({username, password})
        }catch (e) {
            switch (e) {
                case LOGIN_ERRORS.INVALID_USERNAME_OR_PASSWORD:
                    setErrors({globalError: t('form.invalidUsernameOrPassword')});
                    break;
                case LOGIN_ERRORS.CONN_ERROR:
                default:
                    message.error(t('form.conError'));
                    break;
            }
        }finally {
            setSubmitting(false);
        }
    };

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <LoginForm onSubmit={_onSubmit} submitting={submitting}/>
    </SmallCenteredContent>
}

export default LoginView;