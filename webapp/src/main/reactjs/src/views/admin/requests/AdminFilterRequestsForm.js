import React from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

const FormItem = Form.Item;

const AdminFilterRequestsForm = () => {
    const {t} = useTranslation('admin');

    //TODO: hay que solo mostrar los que sean correspondientes a lo que existe
    //TODO: hay que hacer que se desbloquee el order cuando selecciono un criteria de request

    return <Form layout={"vertical"} className={"admin__container"}>
        <div className={"form-content"}>
            <FormItem name={"status"} label={t("requestsFilterForm.labels.status")}>
                <Select name={"status"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"accepted"}>{t("status.accepted")}</Select.Option>
                    <Select.Option value={"rejected"}>{t("status.rejected")}</Select.Option>
                    <Select.Option value={"pending"}>{t("status.pending")}</Select.Option>
                    <Select.Option value={"canceled"}>{t("status.canceled")}</Select.Option>
                    <Select.Option value={"sold"}>{t("status.sold")}</Select.Option>
                </Select>
            </FormItem>


            <FormItem name={"criteria"} label={t("requestsFilterForm.labels.criteria")}>
                <Select name={"criteria"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"date"}>{t("values.date")}</Select.Option>
                    <Select.Option value={"petName"}>{t("values.petName")}</Select.Option>
                </Select>
            </FormItem>

            <FormItem name={"order"} label={t("requestsFilterForm.labels.order")}>
                <Select name={"order"} defaultValue={"asc"}>
                    <Select.Option value={"asc"}>{t("values.asc")}</Select.Option>
                    <Select.Option value={"desc"}>{t("values.desc")}</Select.Option>

                </Select>
            </FormItem>
        </div>

        <div className={"form-buttons"}>
            <Button type={"primary"} htmlType={"submit"}>{t('buttons.filter')}</Button>
            <Button type={"secondary"}>{t('buttons.clear')}</Button>
        </div>
    </Form>
}

export default withFormik({
    mapPropsToValues: () => ({}),
    handleSubmit: (values) => {
        console.log(values);
    }
})(AdminFilterRequestsForm);