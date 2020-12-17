import React, {useState} from 'react';
import {Form, Select} from "formik-antd";
import {withFormik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";

const FormItem = Form.Item;

const FilterInterestsForm = ( {filters, fetchInterests, changeFilters, setCurrentPage, fetchFilters}) => {
    const {t} = useTranslation('interests');

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

    const [selectedValues, changeSelectedValues] = useState(
        {status:-1, petId:0, searchCriteria:"any", searchOrder:"asc"});

    const refreshStatus = (value) => {
        changeSelectedValues(Object.assign(selectedValues, {status:value}));
    }

    const refreshPet = (value) => {
        changeSelectedValues(Object.assign(selectedValues, {petId:value}));
    }

    const refreshCriteria = (value) => {
        changeSelectedValues(Object.assign(selectedValues, {searchCriteria:value}));
    }
    const refreshOrder = (value) => {
        changeSelectedValues(Object.assign(selectedValues, {searchOrder:value}));
    }

    const clearFilters = () => {
        changeSelectedValues({status:-1, petId:0, searchCriteria:"any", searchOrder:"asc"});
        toggleDisabled(true);
        console.log(selectedValues)
    }

    return <Form layout={"vertical"} className={"requests-interests__container"}>
        <div className={"form-content"}>
            <FormItem name={"status"} label={t("filterForm.labels.status")}>
                <Select name={"status"} value={selectedValues.status} onSelect={refreshStatus}>
                    <Select.Option value={-1}>{t("filterForm.values.any")}</Select.Option>
                    {
                        filters && filters.statusList.map((status) =>{
                            return <Select.Option value={status} key={status} >{statusLocale[status]}</Select.Option>
                        })
                    }
                </Select>
            </FormItem>

            <FormItem name={"petId"} label={t("filterForm.labels.pet")}>
                <Select name={"petId"} value={selectedValues.petId} onSelect={refreshPet}>
                    <Select.Option value={0}>{t("filterForm.values.any")}</Select.Option>
                    {
                        filters && filters.petList.map((pet) =>{
                            return <Select.Option value={pet.id} key={pet.id} >{pet.petName}</Select.Option>
                        })
                    }
                </Select>
            </FormItem>

            <FormItem name={"criteria"} label={t("filterForm.labels.criteria")}>
                <Select name={"criteria"} value={selectedValues.searchCriteria} onChange={enableOrder} onSelect={refreshCriteria}>
                    <Select.Option value={"any"}>{t("filterForm.values.any")}</Select.Option>
                    <Select.Option value={"date"}>{t("filterForm.values.date")}</Select.Option>
                    <Select.Option value={"petName"}>{t("filterForm.values.petName")}</Select.Option>
                </Select>
            </FormItem>

            <FormItem name={"order"} label={t("filterForm.labels.order")} >
                <Select name={"order"} value={selectedValues.searchOrder} disabled={isDisabled} onSelect={refreshOrder}>
                    <Select.Option value={"asc"}>{t("filterForm.values.asc")}</Select.Option>
                    <Select.Option value={"desc"}>{t("filterForm.values.desc")}</Select.Option>

                </Select>
            </FormItem>
        </div>

        <div className={"form-buttons"}>
            <Button type={"primary"} htmlType={"submit"} onClick={() => changeFilters(selectedValues)}>{t('filterForm.filterButtons.filter')}</Button>
            <Button type={"secondary"} onClick={clearFilters} >{t('filterForm.filterButtons.clear')}</Button>
        </div>
    </Form>
}

export default withFormik({
    mapPropsToValues: () => ({}),
    handleSubmit: (values,formikBag) => {
        console.log(values)
        formikBag.props.fetchInterests({...values, page:1})
        formikBag.props.setCurrentPage(1)
    }
})(FilterInterestsForm);