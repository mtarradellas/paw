import React from 'react';
import {Form, Input, Checkbox} from "formik-antd";
import {ErrorMessage, Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';
import {Link} from "react-router-dom";
import {FORGOT_PASSWORD, REGISTER} from "../../constants/routes";

const FormItem = Form.Item;

function RegisterForm({onSubmit, submitting}){
    const {t} = useTranslation("login");

    return <Formik
        validationSchema={
            Yup.object().shape({
                username: Yup.string()
                    .required(t('form.username.errors.required')),
                password: Yup.string()
                    .required(t('form.password.errors.required')),
                rememberMe: Yup.boolean()
            })
        }
        onSubmit={onSubmit}
        initialValues={
            {
                username: '',
                password: '',
                globalError: '',
                rememberMe: false
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

            <p className={"error-message"}><ErrorMessage name={"globalError"}/></p>

            <FormItem name={"rememberMe"}>
                <Checkbox name={"rememberMe"}>{t('form.rememberMe.label')}</Checkbox>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('form.submit')}
                </Button>
            </FormItem>

            <Link to={FORGOT_PASSWORD}>{t('form.forgotPassword')}</Link>

            <p>{t('form.dontHaveAnAccount')} <Link to={REGISTER}>{t('form.registerButton')}</Link></p>
        </Form>
    </Formik>
}

export default RegisterForm;