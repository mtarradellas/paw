import React from 'react';
import {Form, Select} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";
import _ from 'lodash';

const FormItem = Form.Item;

const FilterInterestsForm = ({
                                 filters,
                                 fetchInterests,
                                 changeFilters,
                                 setCurrentPage,
                                 fetchFilters,
                                 initialFilters
                             }) => {
    const {t} = useTranslation('interests');

    const statusLocale = [
        t("status.pending"),
        t("status.accepted"),
        t("status.rejected"),
        t("status.canceled"),
        t("status.sold")
    ]

    const _onSubmit = (values) => {
        fetchInterests({...values, page: 1})
        setCurrentPage(1)
        changeFilters(values)
    }

    console.log(filters)

    if (!_.isNil(filters) && filters.statusList.length === 0) {
        filters.statusList = [0, 2, 3, 4]
    }

    return <Formik
        initialValues={Object.assign({status: "-1", petId: "0"}, initialFilters)}
        onSubmit={_onSubmit}
        validationSchema={
            Yup.object().shape({
                status: Yup.string(),
                petId: Yup.string(),
                searchOrder: Yup.string(),
                searchCriteria: Yup.string()
            })
        }
        render={({values, setFieldValue}) => {
            const resetFields = () => {
                setFieldValue("status", "-1");
                setFieldValue("petId", "0");
                setFieldValue("searchCriteria", "date");
                setFieldValue("searchOrder", "desc");
                fetchFilters({petId: 0, status: -1})

                _onSubmit({status: '-1', petId: '0', searchCriteria: 'date', searchOrder: 'desc'});
            }

            const filterOtherValue = (value, filter) => {
                let params;

                if (filter === "petId") {
                    params = {
                        petId: value
                    }
                } else {
                    params = {
                        status: value
                    }
                }

                fetchFilters(params);
            }

            return <Form layout={"vertical"} className={"requests-interests__container"}>
                <div className={"form-content"}>
                    <FormItem name={"status"} label={t("filterForm.labels.status")}>
                        <Select name={"status"}>
                            <Select.Option value={'' + -1}>{t("filterForm.values.any")}</Select.Option>
                            {
                                filters && filters.statusList.map((status) => {
                                    return <Select.Option value={'' + status}
                                                          key={status}>{statusLocale[status]}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"petId"} label={t("filterForm.labels.pet")}>
                        <Select name={"petId"} onSelect={(value) => filterOtherValue(value, "petId")}>
                            <Select.Option value={'' + 0}>{t("filterForm.values.any")}</Select.Option>
                            {
                                filters && filters.petList.map((pet) => {
                                    return <Select.Option value={'' + pet.id} key={pet.id}>{pet.petName}</Select.Option>
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

export default FilterInterestsForm;