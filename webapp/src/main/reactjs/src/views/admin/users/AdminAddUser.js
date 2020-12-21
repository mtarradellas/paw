import React from 'react';
import { useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import useLogin from "../../../hooks/useLogin";
import {CREATE_REQUEST_ERRORS} from "../../../api/admin/requests";
import {ADMIN_USERS} from "../../../constants/routes";
import {createUserAdmin} from "../../../api/admin/users";
import BigCenteredContent from "../../../components/BigCenteredContent";
import {Formik} from "formik";
import * as Yup from "yup";
import {Form, Input} from "formik-antd";
import {Button} from "antd";

const FormItem = Form.Item

const VALID_CHARACTERS = "^[a-zA-Z0-9\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
    "\u00F3\u00FA\u00F1\u00FC]*$";

function AddUserForm({_onSubmit}) {
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

        </Form>
    </Formik>
}

function AdminAddUser(){
    const history = useHistory();

    const {t} = useTranslation('admin');

    const {state} = useLogin();
    const {jwt} = state;

    const _onSubmit = async (values, {setErrors}) => {
        try {
            await createUserAdmin(values.username,values.email,values.password, jwt);

            history.push(ADMIN_USERS);
        } catch (e) {
            switch (e) {
                case CREATE_REQUEST_ERRORS.CONN_ERROR:
                default:
                    setErrors({userId: t('errors.default')});
                    //TODO: conn error message
                    break;
            }
        }
    }

    return <BigCenteredContent>
        <h1>{t("addUserView.title")}</h1>

        <AddUserForm _onSubmit={_onSubmit}/>
    </BigCenteredContent>
}


export default AdminAddUser;