import React, {useState, useEffect, useContext} from 'react';
import {Form, Select} from "formik-antd";
import {Formik} from "formik";
import {Button} from "antd";
import {useTranslation} from "react-i18next";
import _ from 'lodash';
import * as Yup from 'yup';
import {petFilters} from "../../api/pets";
import ConstantsContext from "../../constants/constantsContext";
import FilterAndSearchContext from "../../constants/filterAndSearchContext";

const FormItem = Form.Item;

const SelectOption = Select.Option;

const initialValues = {
        species: -1,
        breed: -1,
        priceRange: -1,
        gender: -1,
        province: -1,
        department: -1,
        searchCriteria: -1,
        searchOrder: 'asc'
    };

const FilterOptionsForm = () => {
    const {
        filters,
        onSubmitFilters,
        fetching,
        clearFilters
    } = useContext(FilterAndSearchContext);

    const {species, breeds, provinces, departments, loaded} = useContext(ConstantsContext);

    const {t} = useTranslation('home');

    const [availableFilters, setAvailableFilters] = useState({
        speciesList: null, breedList: null, departmentList: null, provinceList: null, genderList: null, rangeList: null
    });

    const fetchFilters = async values => {
        try{
            const newFilters = await petFilters(Object.assign({}, {find: filters.find}, values));

            setAvailableFilters(newFilters);

        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchFilters(filters);
    }, [filters.find]);

    const {speciesList, breedList, departmentList, provinceList, genderList, rangeList} = availableFilters;

    const _onSubmit = async values => {
        const filledFilters = _.pickBy(values, value => value !== -1);

        onSubmitFilters(filledFilters);

        await fetchFilters(filledFilters);
    };

    return <Formik
            validationSchema={
                Yup.object().shape({
                    species: Yup.number(),
                    breed: Yup.number(),
                    priceRange: Yup.number(),
                    gender: Yup.string(),
                    province: Yup.number(),
                    department: Yup.number(),
                    searchCriteria: Yup.string(),
                    searchOrder: Yup.string()
                })
            }
            onSubmit={_onSubmit}
            initialValues={Object.assign({}, initialValues, filters)}
            render={
                ({setFieldValue, values, handleSubmit, setValues}) => {
                    const breedsToShow = values.species && values.species !== -1 && _.intersection(
                        species[values.species].breedIds,
                        breedList && breedList.map(({id}) => id)
                    ).map(id => ({id, name: breeds[id].name}));

                    const departmentsToShow = values.province && values.province !== -1 && _.intersection(
                        provinces[values.province].departmentIds,
                        departmentList && departmentList.map(({id}) => id)
                    ).map(id => ({id, name: departments[id].name}));

                    return <Form layout={"vertical"}>
                        <div className={"form-content"}>
                            <FormItem name={"species"} label={t("filterForm.labels.specie")}>
                                <Select name={"species"} disabled={_.isNil(speciesList)}
                                    onChange={() => setFieldValue('breed', -1)}
                                >
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        speciesList && speciesList.map(({id, name}) => {
                                            return <SelectOption value={id}>{name}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"breed"} label={t("filterForm.labels.breed")}>
                                <Select name={"breed"} disabled={_.isNil(breedList) || values.species === -1}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        breedsToShow && breedsToShow.map(({id, name}) => {
                                            return <SelectOption value={id}>{name}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"priceRange"} label={t("filterForm.labels.price")}>
                                <Select name={"priceRange"} disabled={_.isNil(rangeList)}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        rangeList && Object.entries(rangeList).map(([id, {min, max}]) => {
                                            if(min === 0 && max === 0)
                                                return <SelectOption value={id}>{t('filterForm.labels.priceRange.free')}</SelectOption>;

                                            if(max === -1)
                                                return <SelectOption value={id}>{t('filterForm.labels.priceRange.max', {min})}</SelectOption>;

                                            return <SelectOption value={id}>{t('filterForm.labels.priceRange.range', {min, max})}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"gender"} label={t("filterForm.labels.sex")}>
                                <Select name={"gender"} disabled={_.isNil(genderList)}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        genderList && genderList.map(name => {
                                            return <SelectOption value={name}>{t('sex.' + name)}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"province"} label={t("filterForm.labels.state")}
                                      onChange={() => setFieldValue('department', -1)}
                            >
                                <Select name={"province"} disabled={_.isNil(provinceList)}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        provinceList && provinceList.map(({id, name}) => {
                                            return <SelectOption value={id}>{name}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"department"} label={t("filterForm.labels.department")}>
                                <Select name={"department"} disabled={_.isNil(departmentList) || values.province === -1}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    {
                                        departmentsToShow && departmentsToShow.map(({id, name}) => {
                                            return <SelectOption value={id}>{name}</SelectOption>;
                                        })
                                    }
                                </Select>
                            </FormItem>

                            <FormItem name={"searchCriteria"} label={t("filterForm.labels.criteria")}>
                                <Select name={"searchCriteria"}>
                                    <SelectOption value={-1}>{t('filterForm.labels.any')}</SelectOption>
                                    <SelectOption value={'uploadDate'}>{t('filterForm.criteria.uploadDate')}</SelectOption>
                                    <SelectOption value={'gender'}>{t('filterForm.criteria.gender')}</SelectOption>
                                    <SelectOption value={'species'}>{t('filterForm.criteria.species')}</SelectOption>
                                    <SelectOption value={'breed'}>{t('filterForm.criteria.breed')}</SelectOption>
                                    <SelectOption value={'price'}>{t('filterForm.criteria.price')}</SelectOption>
                                </Select>
                            </FormItem>

                            <FormItem name={"searchOrder"} label={t("filterForm.labels.order")}>
                                <Select name={"searchOrder"} disabled={values.searchCriteria === -1}>
                                    <SelectOption value={'asc'}>{t('filterForm.asc')}</SelectOption>
                                    <SelectOption value={'desc'}>{t('filterForm.desc')}</SelectOption>
                                </Select>
                            </FormItem>
                        </div>

                        <div className={"form-buttons"}>
                            <Button type={"primary"} htmlType={"submit"} loading={fetching}>{t('filterForm.filterButtons.filter')}</Button>

                            <Button type={"secondary"}
                                onClick={()=>{
                                    setValues(initialValues);
                                    clearFilters();
                                }}
                                loading={fetching}
                            >{t('filterForm.filterButtons.clear')}</Button>
                        </div>
                    </Form>;
                }
            }
        />
};

export default FilterOptionsForm;