import React from 'react';
import {Form, Select} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";


const FormItem = Form.Item;

const AdminFilterUsersForm = ({filters, changeFilters, setCurrentPage, fetchAdminUsers}) => {
    const {t} = useTranslation('admin');

    const statusLocale = [
        t("status.active"),
        t("status.inactive"),
        t("status.deleted")
    ]

    const _onSubmit = (values) => {
        fetchAdminUsers({...values, page:1})
        setCurrentPage(1)
        changeFilters(values)
    }

    return <Formik
        initialValues={{status: -1, searchCriteria: "any", searchOrder: "asc"}}
        onSubmit={_onSubmit}
        validationSchema={
            Yup.object().shape({
                status: Yup.number(),
                searchOrder: Yup.string(),
                searchCriteria: Yup.string()
            })
        }
        render={({values, setFieldValue}) => {
            const resetFields = () => {
                setFieldValue("status", -1);
                setFieldValue("searchCriteria", "any");
                setFieldValue("searchOrder", "asc");
            }

            return <Form layout={"vertical"} className={"admin__container"}>
                <div className={"form-content"}>
                    <FormItem name={"status"} label={t("usersFilterForm.labels.status")}>
                        <Select name={"status"}>
                            <Select.Option value={-1}>{t("values.any")}</Select.Option>
                            {
                                filters && filters.map((status) => {
                                    return <Select.Option value={status}
                                                          key={status}>{statusLocale[status]}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>


                    <FormItem name={"searchCriteria"} label={t("requestsFilterForm.labels.criteria")}>
                        <Select name={"searchCriteria"}>
                            <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                            <Select.Option value={"username"}>{t("values.username")}</Select.Option>
                            <Select.Option value={"mail"}>{t("values.mail")}</Select.Option>
                        </Select>
                    </FormItem>

                    <FormItem name={"searchOrder"} label={t("requestsFilterForm.labels.order")}>
                        <Select name={"searchOrder"} disabled={values.searchCriteria === "any"}>
                            <Select.Option value={"asc"}>{t("values.asc")}</Select.Option>
                            <Select.Option value={"desc"}>{t("values.desc")}</Select.Option>
                        </Select>
                    </FormItem>
                </div>

                <div className={"form-buttons"}>
                    <Button type={"primary"} htmlType={"submit"}>{t('buttons.filter')}</Button>
                    <Button type={"secondary"} onClick={resetFields}>{t('buttons.clear')}</Button>
                </div>
            </Form>
        }}/>
}

export default AdminFilterUsersForm;