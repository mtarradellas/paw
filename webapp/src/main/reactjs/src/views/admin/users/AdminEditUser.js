import React, {useState, useEffect} from 'react';
import useLogin from "../../../hooks/useLogin";
import {useHistory, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {editPasswordAdmin, editUsernameAdmin, GET_USERS_ERRORS, getUserAdmin} from "../../../api/admin/users";
import BigCenteredContent from "../../../components/BigCenteredContent";
import {EDIT_USERNAME_ERRORS} from "../../../api/users";
import {ADMIN_USERS} from "../../../constants/routes";
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import * as Yup from "yup";
import {Button, Divider} from "antd";

const FormItem = Form.Item

const VALID_CHARACTERS = "^[a-zA-Z0-9\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
    "\u00F3\u00FA\u00F1\u00FC]*$";


function EditUserForm({user, _onSubmitUsername, _onSubmitPassword}) {
    const {t} = useTranslation("register");
    return <div>
        <Formik
            validationSchema={
                Yup.object().shape({
                    username: Yup.string()
                        .min(4, ({min}) => (t('form.username.errors.tooShort', {min})))
                        .max(255, ({max}) => (t('form.username.errors.tooLong', {max})))
                        .matches(VALID_CHARACTERS, t('form.username.errors.validCharacters'))
                        .required(t('form.username.errors.required')),
                })
            }
            onSubmit={_onSubmitUsername}
            initialValues={
                {
                    username: user
                }
            }
        >
            <Form
                layout={"vertical"}
            >
                <FormItem name={"username"} label={t('form.username.label')}>
                    <Input name={"username"} placeholder={t('form.username.placeholder')}/>
                </FormItem>

                <FormItem name>
                    <Button type="primary" htmlType="submit">
                        {t('form.changeUsername')}
                    </Button>
                </FormItem>

            </Form>
        </Formik>
        <Divider/>
        <Formik
            validationSchema={
                Yup.object().shape({
                    newPassword: Yup.string()
                        .min(4, ({min}) => (t('form.password.errors.tooShort', {min})))
                        .max(254, ({max}) => (t('form.password.errors.tooLong', {max})))
                        .required(t('form.password.errors.required')),
                    repeatPassword: Yup.string()
                        .oneOf([Yup.ref('newPassword'), null], t('form.repeatPassword.errors.notEqual'))
                        .required(t('form.repeatPassword.errors.required')),
                    oldPassword: Yup.string()
                        .min(4, ({min}) => (t('form.password.errors.tooShort', {min})))
                        .max(254, ({max}) => (t('form.password.errors.tooLong', {max})))
                        .required(t('form.password.errors.required'))
                })
            }
            onSubmit={_onSubmitPassword}
            initialValues={
                {
                }
            }
        >
            <Form
                layout={"vertical"}
            >
                <FormItem name={"oldPassword"} label={t('form.oldPassword')}>
                    <Input.Password name={"oldPassword"} placeholder={t('form.oldPassword')}/>
                </FormItem>
                <FormItem name={"newPassword"} label={t('form.password.label')}>
                    <Input.Password name={"newPassword"} placeholder={t('form.password.placeholder')}/>
                </FormItem>

                <FormItem name={"repeatPassword"} label={t('form.repeatPassword.label')}>
                    <Input.Password name={"repeatPassword"} placeholder={t('form.repeatPassword.placeholder')}/>
                </FormItem>

                <FormItem name>
                    <Button type="primary" htmlType="submit">
                        {t('form.changePassword')}
                    </Button>
                </FormItem>
            </Form>
        </Formik>
        <Divider/>
    </div>
}

function AdminEditUser() {
    const {jwt} = useLogin().state;
    const {id} = useParams();

    const history = useHistory();

    const [user, setUser] = useState("");
    const {t} = useTranslation('admin');

    const fetchUser = async () => {
        try {
            const request = await getUserAdmin(id, jwt);

            setUser(request.username);
        } catch (e) {
            throw GET_USERS_ERRORS.NOT_FOUND
        }
    }

    useEffect(() => {
        fetchUser();
    }, [])

    const _onSubmitUsername = async (values, {setErrors}) => {
        try {
            if (values.username === user) {
                setErrors({username: t('errors.unchanged')});
            } else {
                await editUsernameAdmin(values.username, id, jwt);
                history.push(ADMIN_USERS);
            }
        } catch (e) {
            switch (e) {
                case EDIT_USERNAME_ERRORS.CONN_ERROR:
                default:
                    setErrors({username: t('errors.default')});
                    break;
            }
        }
    }

    const _onSubmitPassword = async (values, {setErrors}) => {
        try {

            if (values.newPassword !== values.repeatPassword) {
                setErrors({repeatPassword: t('errors.noMatch')});
            } else {
                await editPasswordAdmin(values.oldPassword, values.newPassword, id, jwt);
                history.push(ADMIN_USERS);
            }
        } catch (e) {
            switch (e) {
                case EDIT_USERNAME_ERRORS.CONN_ERROR:
                default:
                    setErrors({newPassword: t('errors.default')});
                    break;
            }
        }
    }

    return <BigCenteredContent>
        <h1>{t("editUserView.title")}: {user} </h1>
        <EditUserForm user={user} _onSubmitPassword={_onSubmitPassword} _onSubmitUsername={_onSubmitUsername}/>
    </BigCenteredContent>

}

export default AdminEditUser;