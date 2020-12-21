import React from 'react'
import {Form, Input} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from 'yup';

const FormItem = Form.Item;
const VALID_CHARACTERS = "^[a-zA-Z0-9\u00C1\u00C9\u00CD\u00D3\u00DA\u00D1\u00DC\u00E1\u00E9\u00ED" +
                        "\u00F3\u00FA\u00F1\u00FC]*$";

const EditUsernameForm = ({oldname,  onSubmit, submitting}) => {
    const {t} = useTranslation("editUser");
    return (
        <Formik 
            validationSchema={
                Yup.object().shape({
                    username: Yup.string()
                    .min(4, ({min}) => (t('usernameForm.username.errors.tooShort', {min})))
                    .max(255, ({max}) => (t('usernameForm.username.errors.tooLong', {max})))
                    .matches(VALID_CHARACTERS, t('usernameForm.username.errors.validCharacters'))
                    .required(t('usernameForm.username.errors.required')),
                })
            }
            onSubmit={onSubmit}
            initialValues={{
                username: ''
            }}
        >
            <Form layout={"vertical"}>
                <FormItem name={"username"} label={t('usernameForm.username.label')}>
                    <Input name={"username"} placeholder={oldname}/>
                </FormItem>
                <FormItem name>

                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('usernameForm.submit')}
                </Button>
            </FormItem>
            </Form>
        </Formik>
    )
}

export default EditUsernameForm
