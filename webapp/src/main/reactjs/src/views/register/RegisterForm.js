import React from 'react';
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';
import {Link} from "react-router-dom";
import {LOGIN} from "../../constants/routes";

const FormItem = Form.Item;

//TODO: ver las validaciones para que coincidan con las del backend
function RegisterForm({onSubmit}){
    const {t} = useTranslation("register");

    const _onSubmit = (values) => {
        onSubmit(values);
    }

    return <Formik
        validationSchema={
            Yup.object().shape({
                username: Yup.string()
                    .min(2, ({min}) => (t('form.username.errors.tooShort', {min})))
                    .max(50, ({max}) => (t('form.username.errors.tooLong', {max})))
                    .required(t('form.username.errors.required')),
                password: Yup.string()
                    .min(2, ({min}) => (t('form.password.errors.tooShort', {min})))
                    .max(50, ({max}) => (t('form.password.errors.tooLong', {max})))
                    .required(t('form.password.errors.required')),
                repeatPassword: Yup.string()
                    .oneOf([Yup.ref('password'), null], t('form.repeatPassword.errors.notEqual'))
                    .required(t('form.repeatPassword.errors.required')),
                email: Yup.string()
                    .email(t('form.email.errors.invalidEmail'))
                    .required(t('form.email.errors.required'))
            })
        }
        onSubmit={_onSubmit}
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
                <Button type="primary" htmlType="submit">
                    {t('form.submit')}
                </Button>
            </FormItem>

            <p>¿Ya tiene una cuenta? <Link to={LOGIN}>Iniciar sesión</Link></p>
        </Form>
    </Formik>
}

export default RegisterForm;