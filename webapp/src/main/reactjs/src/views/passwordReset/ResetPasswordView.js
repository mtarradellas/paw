import React, {useState} from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import RegisterForm from "./RegisterForm";
import {useTranslation} from "react-i18next";
import {register, REGISTER_ERRORS} from "../../api/authentication";
import {message} from 'antd';
import {useHistory} from 'react-router-dom';
import {VERIFY_EMAIL} from "../../constants/routes";

function ResetPasswordView(){
    const {t} = useTranslation("forgotPassword");
    const history = useHistory();

    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async (values, {setErrors}) => {
        setSubmitting(true);

        try{
            await register(values);

            history.push(VERIFY_EMAIL);

        }catch (e) {
            message.error(t('form.conError'));
            setSubmitting(false);
        }

    };

    return <SmallCenteredContent>
        <h1>{t('title')}</h1>
        <RegisterForm
            onSubmit={_onSubmit}
            submitting={submitting}
        />
    </SmallCenteredContent>
}

export default ResetPasswordView;