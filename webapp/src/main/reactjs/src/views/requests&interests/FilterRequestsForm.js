import React, {useState} from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

const FormItem = Form.Item;

const FilterRequestsForm = ({filters,fetchRequests}) => {

    const {t} = useTranslation('requests');

    const [isDisabled, toggleDisabled] = useState(true);

    const enableOrder = (value) => {
        if(value !== "any"){
            toggleDisabled(false);
        }else{
            toggleDisabled(true);
        }
    }

    const statusLocale = [
        t("status.pending"),
        t("status.accepted"),
        t("status.rejected"),
        t("status.canceled"),
        t("status.sold")
    ]

    return <Form layout={"vertical"} className={"requests-interests__container"}>
        <div className={"form-content"}>
            <FormItem name={"status"} label={t("filterForm.labels.status")}>
                <Select name={"status"} defaultValue={-1} >
                    <Select.Option value={-1}>{t("filterForm.values.any")}</Select.Option>
                    {
                        filters && filters.map((status) =>{
                            return <Select.Option value={status} key={status} >{statusLocale[status]}</Select.Option>
                        })
                    }
                </Select>
            </FormItem>

            <FormItem name={"searchCriteria"} label={t("filterForm.labels.criteria")}>
                <Select name={"searchCriteria"} defaultValue={"any"} onChange={enableOrder}>
                    <Select.Option value={"any"}>{t("filterForm.values.any")}</Select.Option>
                    <Select.Option value={"date"}>{t("filterForm.values.date")}</Select.Option>
                    <Select.Option value={"petName"}>{t("filterForm.values.petName")}</Select.Option>
                </Select>
            </FormItem>

            <FormItem name={"searchOrder"} label={t("filterForm.labels.order")}>
                <Select name={"searchOrder"} defaultValue={"asc"} disabled={isDisabled}>
                    <Select.Option value={"asc"}>{t("filterForm.values.asc")}</Select.Option>
                    <Select.Option value={"desc"}>{t("filterForm.values.desc")}</Select.Option>

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
    handleSubmit: (values,formikBag) => {
        formikBag.props.fetchRequests(values)
    }
})(FilterRequestsForm);