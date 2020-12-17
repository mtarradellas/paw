import React from 'react';
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';

const FormItem = Form.Item;

function ResetPasswordForm({onSubmit, submitting}){
    const {t} = useTranslation("forgotPassword");

    return <Formik
        validationSchema={
            Yup.object().shape({
                password: Yup.string()
                    .min(4, ({min}) => (t('form.password.errors.tooShort', {min})))
                    .max(254, ({max}) => (t('form.password.errors.tooLong', {max})))
                    .required(t('form.password.errors.required')),
                repeatPassword: Yup.string()
                    .oneOf([Yup.ref('password'), null], t('form.repeatPassword.errors.notEqual'))
                    .required(t('form.repeatPassword.errors.required'))
            })
        }
        onSubmit={onSubmit}
        initialValues={
            {
                password: '',
                repeatPassword: ''
            }
        }
    >
        <Form
            layout={"vertical"}
        >

            <FormItem name={"password"} label={t('form.password.label')}>
                <Input.Password name={"password"} placeholder={t('form.password.placeholder')}/>
            </FormItem>

            <FormItem name={"repeatPassword"} label={t('form.repeatPassword.label')}>
                <Input.Password name={"repeatPassword"} placeholder={t('form.repeatPassword.placeholder')}/>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('form.submit')}
                </Button>
            </FormItem>

        </Form>
    </Formik>
}

export default ResetPasswordForm;