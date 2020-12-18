import React, {useState} from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import {useTranslation} from "react-i18next";
import {message} from 'antd';
import {useHistory, useParams} from 'react-router-dom';
import {LOGIN} from "../../constants/routes";
import ResetPasswordForm from "./ResetPasswordForm";
import {FORGOT_PASSWORD_ERRORS, RESET_PASSWORD_ERRORS, resetPassword} from "../../api/authentication";

function ResetPasswordView({}){
    const {token} = useParams();
    const {t} = useTranslation("forgotPassword");
    const history = useHistory();

    const [submitting, setSubmitting] = useState(false);
    console.log(token)
    const _onSubmit = async (values, {setErrors}) => {
        setSubmitting(true);

        try{
            console.log(token)
            await resetPassword({password: values.password, token});

            history.push(LOGIN);

        }catch (e) {
            switch (e) {
                case RESET_PASSWORD_ERRORS.INVALID_TOKEN:
                    message.error(t('token.invalid'));
                    break;
                case FORGOT_PASSWORD_ERRORS.CONN_ERROR:
                default:
                    message.error(t('form.conError'));
                    break;
            }
            setSubmitting(false);
        }

    };

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <ResetPasswordForm
            onSubmit={_onSubmit}
            submitting={submitting}
        />
    </SmallCenteredContent>
}

export default ResetPasswordView;