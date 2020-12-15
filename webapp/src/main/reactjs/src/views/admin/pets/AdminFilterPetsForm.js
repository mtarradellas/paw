import React from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

const FormItem = Form.Item;

const AdminFilterPetsForm = () => {
    const {t} = useTranslation('admin');

    //TODO: hay que solo mostrar los que sean correspondientes a lo que existe
    //TODO: hay que hacer que se desbloquee el order cuando selecciono un criteria de request

    return <Form layout={"vertical"} className={"admin__container"}>
        <div className={"form-content"}>
            <FormItem name={"species"} label={t("petsFilterForm.labels.species")}>
                <Select name={"species"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"dog"}>Dog</Select.Option>
                    <Select.Option value={"cat"}>Cat</Select.Option>

                </Select>
            </FormItem>

            <FormItem name={"breed"} label={t("petsFilterForm.labels.breed")}>
                <Select name={"breed"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"dog"}>Dog</Select.Option>
                    <Select.Option value={"cat"}>Cat</Select.Option>
                </Select>
            </FormItem>

            <FormItem name={"gender"} label={t("petsFilterForm.labels.gender")}>
                <Select name={"gender"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"dog"}>Male</Select.Option>
                    <Select.Option value={"cat"}>Female</Select.Option>

                </Select>
            </FormItem>

            <FormItem name={"status"} label={t("petsFilterForm.labels.status")}>
                <Select name={"status"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"available"}>{t("status.available")}</Select.Option>
                    <Select.Option value={"removed"}>{t("status.removed")}</Select.Option>
                    <Select.Option value={"sold"}>{t("status.sold")}</Select.Option>
                    <Select.Option value={"unavailable"}>{t("status.unavailable")}</Select.Option>
                </Select>
            </FormItem>


            <FormItem name={"criteria"} label={t("petsFilterForm.labels.criteria")}>
                <Select name={"criteria"} defaultValue={"any"}>
                    <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                    <Select.Option value={"date"}>{t("values.species")}</Select.Option>
                    <Select.Option value={"petName"}>{t("values.gender")}</Select.Option>
                    <Select.Option value={"petName"}>{t("values.breed")}</Select.Option>
                    <Select.Option value={"petName"}>{t("values.price")}</Select.Option>
                    <Select.Option value={"petName"}>{t("values.uploadDate")}</Select.Option>

                </Select>
            </FormItem>

            <FormItem name={"order"} label={t("petsFilterForm.labels.order")}>
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
})(AdminFilterPetsForm);