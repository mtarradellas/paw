import React, {useState} from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import {useTranslation} from "react-i18next";
import {message} from 'antd';
import {useHistory, useParams} from 'react-router-dom';
import {VERIFY_EMAIL} from "../../constants/routes";
import ResetPasswordForm from "./ResetPasswordForm";
import {resetPassword} from "../../api/authentication";

function ResetPasswordView({token}){
    const {t} = useTranslation("forgotPassword");
    const history = useHistory();

    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async (values, {setErrors}) => {
        setSubmitting(true);

        try{
            await resetPassword(values, token);

            history.push(VERIFY_EMAIL);

        }catch (e) {
            message.error(t('form.conError'));
            setSubmitting(false);
        }

    };

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <div>{token}</div>
        <ResetPasswordForm
            onSubmit={_onSubmit}
            submitting={submitting}
        />
    </SmallCenteredContent>
}

export default ResetPasswordView;