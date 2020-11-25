import React from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

const FormItem = Form.Item;

const FilterOptionsForm = () => {
    const {t} = useTranslation('home');

    return <Form layout={"vertical"}>
            <div className={"form-content"}>
                <FormItem name={"specie"} label={t("filterForm.labels.specie")}>
                    <Select name={"specie"}>
                        <Select.Option value={"cat"}>Cat</Select.Option>
                        <Select.Option value={"dog"}>Dog</Select.Option>
                    </Select>
                </FormItem>

                <FormItem name={"breed"} label={t("filterForm.labels.breed")}>
                    <Select name={"breed"}>

                    </Select>
                </FormItem>

                <FormItem name={"price"} label={t("filterForm.labels.price")}>
                    <Select name={"price"}>

                    </Select>
                </FormItem>

                <FormItem name={"sex"} label={t("filterForm.labels.sex")}>
                    <Select name={"sex"}>

                    </Select>
                </FormItem>

                <FormItem name={"state"} label={t("filterForm.labels.state")}>
                    <Select name={"state"}>

                    </Select>
                </FormItem>

                <FormItem name={"state"} label={t("filterForm.labels.department")}>
                    <Select name={"state"}>

                    </Select>
                </FormItem>

                <FormItem name={"criteria"} label={t("filterForm.labels.criteria")}>
                    <Select name={"criteria"}>

                    </Select>
                </FormItem>

                <FormItem name={"order"} label={t("filterForm.labels.order")}>
                    <Select name={"order"}>

                    </Select>
                </FormItem>
            </div>

            <div className={"form-buttons"}>
                <Button type={"primary"} htmlType={"submit"}>{t('filterForm.filterButtons.filter')}</Button>

                <Button type={"secondary"}>{t('filterForm.filterButtons.clear')}</Button>
            </div>
        </Form>
}

export default withFormik({
    mapPropsToValues: () => ({}),
    handleSubmit: (values) => {
        console.log(values);
    }
})(FilterOptionsForm);