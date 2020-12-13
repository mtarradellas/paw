import React, {useState, useContext} from 'react';
import {useTranslation} from "react-i18next";
import SmallCenteredContent from "../../components/SmallCenteredContent";
import LoginForm from "./LoginForm";
import {login, LOGIN_ERRORS} from "../api/authentication";
import {message} from "antd";
import LoginContext from '../../constants/loginContext';
import { useHistory } from 'react-router-dom'
import {HOME} from "../../constants/routes";

function LoginView(){
    const {t} = useTranslation("login");
    const [submitting, setSubmitting] = useState(false);
    const history = useHistory();


    const {login: onLogin} = useContext(LoginContext);

    const _onSubmit = async ({username, password}, {setErrors}) => {
        setSubmitting(true);
        try{
            const jwt = await login({username, password});

            onLogin({jwt, username});

            history.push(HOME);
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

            setSubmitting(false);
        }
    };

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <LoginForm onSubmit={_onSubmit} submitting={submitting}/>
    </SmallCenteredContent>
}

export default LoginView;