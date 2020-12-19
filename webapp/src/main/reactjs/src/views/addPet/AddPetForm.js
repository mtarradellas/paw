import React, {useContext} from 'react';
import {useTranslation} from "react-i18next";
import {ErrorMessage, Formik} from "formik";
import {Form, Input, InputNumber, Checkbox, Select, DatePicker} from "formik-antd";
import {Button, Upload} from 'antd';
import { PlusOutlined } from '@ant-design/icons';
import * as Yup from 'yup';
import ConstantsContext from '../../constants/constantsContext';

const FormItem = Form.Item;

const defaultTestValues = {
    petName: 'carlos',
    price: 100,
    isAdoption: false,
    description: 'fdsafdsafd',
    province: 1,
    department: 1,
    specie: '',
    breed: '',
    dateOfBirth: '',
    isVaccinated: '',
    gender: '',
    files: []
}

function AddPetForm({submitting, onSubmit}){
    const {species, breeds, provinces, departments} = useContext(ConstantsContext);
    const {t} = useTranslation('addPet');

    const _onSubmit = values => {
        onSubmit(values);
    };

    return <Formik
        validationSchema={
            Yup.object().shape({
                petName: Yup.string()
                    .required(t('form.petName.required')),
                price: Yup.number()
                    .when('isAdoption', {
                        is: true,
                        otherwise: Yup.number()
                            .typeError(t('form.price.required'))
                            .required(t('form.price.required')),
                        then: Yup.number().typeError(t('form.price.required'))
                    }),
                isAdoption: Yup.boolean(),
                description: Yup.string()
                    .required(t('form.description.required')),
                province: Yup.number()
                    .required(t('form.province.required')),
                department: Yup.number()
                    .required(t('form.department.required')),
                specie: Yup.number()
                    .required(t('form.specie.required')),
                breed: Yup.number()
                    .required(t('form.breed.required')),
                dateOfBirth: Yup.date()
                    .required(t('form.dateOfBirth.required')),
                isVaccinated: Yup.boolean(),
                gender: Yup.string()
                    .required(t('form.gender.required')),
                files: Yup.array()
                    .min(1, min => t('form.images.min', {min}))
            })
        }
        onSubmit={_onSubmit}
        initialValues={
            {
                petName: '',
                price: '',
                isAdoption: '',
                description: '',
                province: '',
                department: '',
                specie: '',
                breed: '',
                dateOfBirth: '',
                isVaccinated: '',
                gender: '',
                files: []
            }
        }
        render={
            ({setFieldValue, values}) => {

                return <Form>
                    <FormItem name={"petName"} label={t('form.petName.label')}>
                        <Input name={"petName"} placeholder={t('form.petName.placeholder')}/>
                    </FormItem>

                    <FormItem name={"price"} label={t('form.price.label')}>
                        <InputNumber
                            name={"price"}
                            placeholder={t('form.price.placeholder')}
                            disabled={values.isAdoption}
                            min={0}
                        />
                    </FormItem>

                    <FormItem name={"isAdoption"} label={t('form.isAdoption.label')}>
                        <Checkbox name={"isAdoption"} onChange={()=>setFieldValue('price', 0)}/>
                    </FormItem>

                    <FormItem name={"description"} label={t('form.description.label')}>
                        <Input name={"description"} placeholder={t('form.description.placeholder')}/>
                    </FormItem>

                    <FormItem name={"province"} label={t('form.province.label')}>
                        <Select name={"province"} placeholder={t('form.province.placeholder')}
                                onChange={() => setFieldValue('department', '')}
                        >
                            {
                                Object.values(provinces).map(({id, name}) => {
                                    return <Select.Option value={id}>{name}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"department"} label={t('form.department.label')}>
                        <Select name={"department"} placeholder={t('form.department.placeholder')} disabled={!values.province}>
                            {
                                values.province && provinces[values.province].departmentIds.map(departmentId => {
                                    const {id, name} = departments[departmentId];

                                    return <Select.Option value={id}>{name}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"specie"} label={t('form.specie.label')}>
                        <Select name={"specie"} placeholder={t('form.specie.placeholder')}
                            onChange={() => setFieldValue('breed', '')}
                        >
                            {
                                Object.values(species).map(({id, name}) => {
                                    return <Select.Option value={id}>{name}</Select.Option>
                                })
                            }
                        </Select>
                    </FormItem>

                    <FormItem name={"breed"} label={t('form.breed.label')}>
                        <Select name={"breed"} placeholder={t('form.breed.placeholder')} disabled={!values.specie}>
                            {
                                values.specie && species[values.specie].breedIds.map(breedId => {
                                    const {id, name} = breeds[breedId];

                                    return <Select.Option value={id}>{name}</Select.Option>
                                })
                            }
                            <Select.Option value={"breed"}>breed</Select.Option>
                        </Select>
                    </FormItem>

                    <FormItem name={"dateOfBirth"} label={t('form.dateOfBirth.label')}>
                        {/*TODO: localizar*/}
                        <DatePicker name={"dateOfBirth"}/>
                    </FormItem>

                    <FormItem name={"isVaccinated"} label={t('form.isVaccinated.label')}>
                        <Checkbox name={"isVaccinated"}/>
                    </FormItem>

                    <FormItem name={"gender"} label={t('form.gender.label')}>
                        <Select name={"gender"} placeholder={t('form.gender.placeholder')}>
                            <Select.Option value={'male'}>{t('form.gender.male')}</Select.Option>
                            <Select.Option value={'female'}>{t('form.gender.female')}</Select.Option>
                        </Select>
                    </FormItem>

                    <Upload
                        beforeUpload={()=>false}
                        listType="picture-card"
                        onChange={({fileList}) => {
                            setFieldValue("files", [...fileList])
                        }}
                    >
                        <div>
                            <PlusOutlined />
                            <div style={{ marginTop: 8 }}>{t('form.upload')}</div>
                        </div>

                    </Upload>
                    <p className={"error-message"}><ErrorMessage name={"files"}/></p>

                    <FormItem name>
                        <Button type="primary" htmlType="submit" loading={submitting}>
                            {t('form.addPet')}
                        </Button>
                    </FormItem>
                </Form>;
            }
        }
        />
}

export default AddPetForm;