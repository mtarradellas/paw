import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import SmallCenteredContent from "../../components/SmallCenteredContent";
import LoginForm from "./LoginForm";
import {login, LOGIN_ERRORS} from "../../api/authentication";
import {message} from "antd";
import useLogin from "../../hooks/useLogin";
import {GET_LOGGED_USER_ERRORS, getLoggedUser} from "../../api/users";

function LoginView(){
    const {t} = useTranslation("login");
    const [submitting, setSubmitting] = useState(false);


    const {login: onLogin} = useLogin();

    const _onSubmit = async ({username, password, rememberMe}, {setErrors}) => {
        console.log(rememberMe)
        setSubmitting(true);
        try{
            const jwt = await login({username, password});

            const {id, mail, isAdmin, status} = await getLoggedUser(jwt);

            onLogin({jwt, username, id, mail, isAdmin, status}, rememberMe);
        }catch (e) {
            switch (e) {
                case LOGIN_ERRORS.INVALID_USERNAME_OR_PASSWORD:
                    setErrors({globalError: t('form.invalidUsernameOrPassword')});
                    break;
                case GET_LOGGED_USER_ERRORS.CONN_ERROR:
                case GET_LOGGED_USER_ERRORS.FORBIDDEN:
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