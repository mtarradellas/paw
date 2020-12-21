import React from 'react';
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';
import {Link} from "react-router-dom";
import {LOGIN} from "../../constants/routes";

const FormItem = Form.Item;

const VALID_CHARACTERS = "^[a-zA-Z0-9\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                        "\u00F3\u00FA\u00F1\u00FC]*$";

function RegisterForm({onSubmit, submitting}){
    const {t} = useTranslation("register");

    return <Formik
        validationSchema={
            Yup.object().shape({
                username: Yup.string()
                    .min(4, ({min}) => (t('form.username.errors.tooShort', {min})))
                    .max(255, ({max}) => (t('form.username.errors.tooLong', {max})))
                    .matches(VALID_CHARACTERS, t('form.username.errors.validCharacters'))
                    .required(t('form.username.errors.required')),
                password: Yup.string()
                    .min(4, ({min}) => (t('form.password.errors.tooShort', {min})))
                    .max(254, ({max}) => (t('form.password.errors.tooLong', {max})))
                    .required(t('form.password.errors.required')),
                repeatPassword: Yup.string()
                    .oneOf([Yup.ref('password'), null], t('form.repeatPassword.errors.notEqual'))
                    .required(t('form.repeatPassword.errors.required')),
                email: Yup.string()
                    .email(t('form.email.errors.invalidEmail'))
                    .required(t('form.email.errors.required'))
            })
        }
        onSubmit={onSubmit}
        initialValues={
            {
                username: '',
                password: '',
                repeatPassword: '',
                email: ''
            }
        }
        >
        <Form
            layout={"vertical"}
        >
            <FormItem name={"username"} label={t('form.username.label')}>
                <Input name={"username"} placeholder={t('form.username.placeholder')}/>
            </FormItem>

            <FormItem name={"password"} label={t('form.password.label')}>
                <Input.Password name={"password"} placeholder={t('form.password.placeholder')}/>
            </FormItem>

            <FormItem name={"repeatPassword"} label={t('form.repeatPassword.label')}>
                <Input.Password name={"repeatPassword"} placeholder={t('form.repeatPassword.placeholder')}/>
            </FormItem>

            <FormItem name={"email"} label={t('form.email.label')}>
                <Input name={"email"} placeholder={t('form.email.placeholder')}/>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('form.submit')}
                </Button>
            </FormItem>

            <p>{t("form.existingAccount")} <Link to={LOGIN}>{t("form.login")}</Link></p>
        </Form>
    </Formik>
}

export default RegisterForm;