import React from 'react'
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';

const FormItem = Form.Item;

const EditPasswordForm = ({onSubmit, submitting}) => {
    const {t} = useTranslation("editUser");
    return (
        <Formik 
            validationSchema={
                Yup.object().shape({
                    oldPassword: Yup.string()
                        .min(4, ({min}) => (t('passwordForm.password.errors.tooShort', {min})))
                        .max(254, ({max}) => (t('passwordForm.password.errors.tooLong', {max})))
                        .required(t('passwordForm.password.errors.required')),
                    newPassword: Yup.string()
                        .min(4, ({min}) => (t('passwordForm.password.errors.tooShort', {min})))
                        .max(254, ({max}) => (t('passwordForm.password.errors.tooLong', {max})))
                        .required(t('passwordForm.password.errors.required')),
                    repeatPassword: Yup.string()
                        .oneOf([Yup.ref('newPassword'), null], t('passwordForm.repeatPassword.errors.notEqual'))
                        .required(t('passwordForm.repeatPassword.errors.required')),
                })
            }
            onSubmit={onSubmit}
            initialValues={{
                oldPassword: '',
                newPassword: '',
                repeatPassword: ''
            }}
        >
            <Form layout={"vertical"}>
                <FormItem name={"oldPassword"} label={t('passwordForm.oldPassword.label')}>
                    <Input.Password name={"oldPassword"} placeholder={t('passwordForm.oldPassword.placeholder')}/>
                </FormItem>

                <FormItem name={"newPassword"} label={t('passwordForm.password.label')}>
                    <Input.Password name={"newPassword"} placeholder={t('passwordForm.password.placeholder')}/>
                </FormItem>

                <FormItem name={"repeatPassword"} label={t('passwordForm.repeatPassword.label')}>
                    <Input.Password name={"repeatPassword"} placeholder={t('passwordForm.repeatPassword.placeholder')}/>
                </FormItem>

                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('passwordForm.submit')}
                </Button>
            </Form>
        </Formik>
    )
}

export default EditPasswordForm
