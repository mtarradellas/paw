import React from 'react';
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';

const FormItem = Form.Item;

function ForgotPasswordForm({onSubmit, submitting}){
    const {t} = useTranslation("forgotPassword");

    return <Formik
        validationSchema={
            Yup.object().shape({
                email: Yup.string()
                    .email(t('form.email.errors.invalidEmail'))
                    .required(t('form.email.errors.required'))
            })
        }
        onSubmit={onSubmit}
        initialValues={
            {
                email: ''
            }
        }
    >
        <Form
            layout={"vertical"}
        >
            <FormItem name={"email"} label={t('form.email.label')}>
                <Input name={"email"} placeholder={t('form.email.placeholder')}/>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('form.submit')}
                </Button>
            </FormItem>
        </Form>
    </Formik>
}

export default ForgotPasswordForm;