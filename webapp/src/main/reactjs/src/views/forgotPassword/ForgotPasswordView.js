import React, {useState} from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import {useTranslation} from "react-i18next";
import {forgotPassword, FORGOT_PASSWORD_ERRORS} from "../../api/authentication";
import {message} from 'antd';
import ForgotPasswordForm from "./ForgotPasswordForm";

function ForgotPasswordView(){
    const {t} = useTranslation("forgotPassword");

    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async (values, {setErrors}) => {
        setSubmitting(true);

        try{
            await forgotPassword(values);

        }catch (e) {
            switch (e) {
                case FORGOT_PASSWORD_ERRORS.NON_EXISTENT_EMAIL:
                    setErrors({username: t('form.username.errors.nonExistent')});
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
        <ForgotPasswordForm
            onSubmit={_onSubmit}
            submitting={submitting}
        />
    </SmallCenteredContent>
}

export default ForgotPasswordView;