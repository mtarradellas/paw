import React, {useContext} from 'react';
import {Form, Select} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import * as Yup from "yup";


import ConstantsContext from '../../../constants/constantsContext';


const FormItem = Form.Item;

const AdminFilterPetsForm = ({filters, changeFilters, setCurrentPage, fetchAdminPets, fetchFilters}) => {
    const {t} = useTranslation('admin');

    const {species, breeds} = useContext(ConstantsContext);

    const _onSubmit = (values) => {
        fetchAdminPets({...values, page: 1})
        setCurrentPage(1)
        changeFilters(values)
    }

    return <Formik
        initialValues={{status: -1, species: 0, breed: 0, gender: "any", searchCriteria: "any", searchOrder: "asc"}}
        onSubmit={_onSubmit}
        validationSchema={
            Yup.object().shape({
                status: Yup.number(),
                species: Yup.number(),
                breed: Yup.number(),
                gender: Yup.string(),
                searchOrder: Yup.string(),
                searchCriteria: Yup.string()
            })
        }
        render={({values, setFieldValue}) => {
            const resetFields = () => {
                setFieldValue("status", -1);
                setFieldValue("species", 0);
                setFieldValue("breed", 0);
                setFieldValue("gender", "any");
                setFieldValue("searchCriteria", "any");
                setFieldValue("searchOrder", "asc");
            } 

            const updateFilters = (value, param) => {
                console.log(value)
                if (param === "breed") {
                    fetchFilters({breed:value})
                } else if (param === "species") {
                    fetchFilters({species: value})
                } else {
                    fetchFilters({gender: value})
                }
            }

            return <Form layout={"vertical"} className={"admin__container"}>
                <div className={"form-content"}>
                    <FormItem name={"species"} label={t("petsFilterForm.labels.species")}>
                        <Select name={"species"} onSelect={(value) => updateFilters(value,"species")}>
                            <Select.Option value={0}>{t("values.any")}</Select.Option>
                            {
                                filters && filters.speciesList.map((specie) => {
                                    return <Select.Option value={specie.id}
                                                          key={specie.id}>{species[specie.id].name}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"breed"} label={t("petsFilterForm.labels.breed")}>
                        <Select name={"breed"}onSelect={(value) => updateFilters(value,"breed")}>
                            <Select.Option value={0}>{t("values.any")}</Select.Option>
                            {
                                filters && filters.breedList.map((breed) => {
                                    return <Select.Option value={breed.id}
                                                          key={breed.id}>{breeds[breed.id].name}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"gender"} label={t("petsFilterForm.labels.gender")}>
                        <Select name={"gender"} onSelect={(value) => updateFilters(value,"gender")}>
                            <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                            <Select.Option value={"male"}>Male</Select.Option>
                            <Select.Option value={"female"}>Female</Select.Option>
                        </Select>
                    </FormItem>

                    <FormItem name={"status"} label={t("petsFilterForm.labels.status")}>
                        <Select name={"status"}>
                            <Select.Option value={-1}>{t("values.any")}</Select.Option>
                            <Select.Option value={0}>{t("status.available")}</Select.Option>
                            <Select.Option value={1}>{t("status.removed")}</Select.Option>
                            <Select.Option value={2}>{t("status.sold")}</Select.Option>
                            <Select.Option value={3}>{t("status.unavailable")}</Select.Option>
                        </Select>
                    </FormItem>


                    <FormItem name={"searchCriteria"} label={t("petsFilterForm.labels.criteria")}>
                        <Select name={"searchCriteria"}>
                            <Select.Option value={"any"}>{t("values.any")}</Select.Option>
                            <Select.Option value={"species"}>{t("values.species")}</Select.Option>
                            <Select.Option value={"gender"}>{t("values.gender")}</Select.Option>
                            <Select.Option value={"breed"}>{t("values.breed")}</Select.Option>
                            <Select.Option value={"price"}>{t("values.price")}</Select.Option>
                            <Select.Option value={"uploadDate"}>{t("values.uploadDate")}</Select.Option>

                        </Select>
                    </FormItem>

                    <FormItem name={"searchOrder"} label={t("petsFilterForm.labels.order")}>
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
        }
        }/>
}

export default AdminFilterPetsForm;