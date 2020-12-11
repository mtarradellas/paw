import React from 'react';
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';
import {Link} from "react-router-dom";
import {FORGOT_PASSWORD, LOGIN, REGISTER} from "../../constants/routes";

const FormItem = Form.Item;

function RegisterForm({onSubmit}){
    const {t} = useTranslation("login");

    const _onSubmit = (values) => {
        onSubmit(values);
    }

    return <Formik
        validationSchema={
            Yup.object().shape({
                username: Yup.string()
                    .required(t('form.username.errors.required')),
                password: Yup.string()
                    .required(t('form.password.errors.required')),
            })
        }
        onSubmit={_onSubmit}
        initialValues={
            {
                username: '',
                password: '',
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

            <FormItem name>
                <Button type="primary" htmlType="submit">
                    {t('form.submit')}
                </Button>
            </FormItem>

            <Link to={FORGOT_PASSWORD}>{t('form.forgotPassword')}</Link>

            <p>{t('form.dontHaveAnAccount')} <Link to={REGISTER}>{t('form.registerButton')}</Link></p>
        </Form>
    </Formik>
}

export default RegisterForm;