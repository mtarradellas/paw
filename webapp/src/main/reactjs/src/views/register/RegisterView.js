import React, {useState} from 'react';
import SmallCenteredContent from "../../components/SmallCenteredContent";
import RegisterForm from "./RegisterForm";
import {useTranslation} from "react-i18next";
import {register, REGISTER_ERRORS} from "../api/authentication";
import {message} from 'antd';

function RegisterView(){
    const {t} = useTranslation("register");

    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async (values, {setErrors}) => {
        setSubmitting(true);

        try{
            await register(values);
        }catch (e) {
            switch (e) {
                case REGISTER_ERRORS.DUPLICATE_USERNAME:
                    setErrors({username: t('form.username.errors.duplicated')});
                    break;
                case REGISTER_ERRORS.DUPLICATE_EMAIL:
                    setErrors({email: t('form.email.errors.duplicated')});
                    break;
                case REGISTER_ERRORS.CONN_ERROR:
                    message.error(t('form.conError'));
                    break;
            }
        }finally {
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

export default RegisterView;