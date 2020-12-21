import React, {useState} from 'react';
import {Form, Select} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

import * as Yup from 'yup';

const FormItem = Form.Item;

const FilterRequestsForm = ({filters, fetchRequests, changeFilters, setCurrentPage}) => {

    const {t} = useTranslation('requests');

    const statusLocale = [
        t("status.pending"),
        t("status.accepted"),
        t("status.rejected"),
        t("status.canceled"),
        t("status.sold")
    ]

    const _onSubmit = (values) => {
        fetchRequests({...values, page: 1})
        setCurrentPage(1)
        changeFilters(values)
    }

    return <Formik
        initialValues={{status: -1, searchCriteria: "date", searchOrder: "desc"}}
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
                setFieldValue("searchCriteria", "date");
                setFieldValue("searchOrder", "desc");
            }

            return <Form layout={"vertical"} className={"requests-interests__container"}>
                <div className={"form-content"}>
                    <FormItem name={"status"} label={t("filterForm.labels.status")}>
                        <Select name={"status"}>
                            <Select.Option value={-1}>{t("filterForm.values.any")}</Select.Option>
                            {
                                filters && filters.map((status) => {
                                    return <Select.Option value={status}
                                                          key={status}>{statusLocale[status]}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"searchCriteria"} label={t("filterForm.labels.criteria")}>
                        <Select name={"searchCriteria"}>
                            <Select.Option value={"any"}>{t("filterForm.values.any")}</Select.Option>
                            <Select.Option value={"date"}>{t("filterForm.values.date")}</Select.Option>
                            <Select.Option value={"petName"}>{t("filterForm.values.petName")}</Select.Option>
                        </Select>
                    </FormItem>

                    <FormItem name={"searchOrder"} label={t("filterForm.labels.order")}>
                        <Select name={"searchOrder"} disabled={values.searchCriteria === "any"}>
                            <Select.Option value={"asc"}>{t("filterForm.values.asc")}</Select.Option>
                            <Select.Option value={"desc"}>{t("filterForm.values.desc")}</Select.Option>

                        </Select>
                    </FormItem>
                </div>

                <div className={"form-buttons"}>
                    <Button type={"primary"} htmlType={"submit"}>{t('filterForm.filterButtons.filter')}</Button>
                    <Button type={"secondary"} onClick={resetFields}>{t('filterForm.filterButtons.clear')}</Button>
                </div>
            </Form>
        }
        }/>
}

export default FilterRequestsForm;