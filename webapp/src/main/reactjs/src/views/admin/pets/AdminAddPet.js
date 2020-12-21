import React, {useContext} from 'react';
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import useLogin from "../../../hooks/useLogin";
import {CREATE_PET_ERRORS, createPet} from "../../../api/pets";
import {ADMIN_PET} from "../../../constants/routes";
import {createAdminPet} from "../../../api/admin/pets";
import BigCenteredContent from "../../../components/BigCenteredContent";
import {Checkbox, DatePicker, Form, Input, InputNumber, Select} from "formik-antd";
import ConstantsContext from "../../../constants/constantsContext";
import {Button, Spin, Upload} from "antd";
import {ErrorMessage, Formik} from "formik";
import * as Yup from "yup";
import DeleteImagesInput from "../../editPet/DeleteImagesInput";
import {PlusOutlined} from "@ant-design/icons";
import useAdminUsers from "../../../hooks/admin/useUsers";

const FormItem = Form.Item;

const defaultValues = {
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
    filesToDelete: [],
    files: []
};

function AddPetForm({ onSubmit, editing, initialValues, users}){
    const {species, breeds, provinces, departments} = useContext(ConstantsContext);
    const {t} = useTranslation('addPet');

    const _onSubmit = values => {
        onSubmit(values);
    };

    if(editing && !initialValues.petName)
        return <Spin/>;

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
                userId: Yup.number().required(t('form.userId.required')),
                description: Yup.string()
                    .required(t('form.description.required')),
                province: Yup.number()
                    .required(t('form.province.required')),
                department: Yup.number()
                    .required(t('form.department.required')),
                species: Yup.number()
                    .required(t('form.specie.required')),
                breed: Yup.number()
                    .required(t('form.breed.required')),
                dateOfBirth: Yup.date()
                    .required(t('form.dateOfBirth.required')),
                isVaccinated: Yup.boolean(),
                gender: Yup.string()
                    .required(t('form.gender.required')),
                filesToDelete: Yup.array(),
                files: editing ? Yup.array() : Yup.array()
                    .min(1, min => t('form.images.min', {min}))
            })
        }
        onSubmit={_onSubmit}
        initialValues={Object.assign({}, defaultValues, initialValues)}
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

                    <FormItem name={"userId"} label={t('form.userId.label')}>
                        <Select name={"userId"} placeholder={t('form.userId.label')}>
                            {
                                users && users.map((user) => {
                                    return <Select.Option value={user.id}
                                                          key={user.id}>{user.username}</Select.Option>
                                })
                            }
                        </Select>
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

                    <FormItem name={"species"} label={t('form.species.label')}>
                        <Select name={"species"} placeholder={t('form.species.placeholder')}
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
                        <Select name={"breed"} placeholder={t('form.breed.placeholder')} disabled={!values.species}>
                            {
                                values.species && species[values.species].breedIds.map(breedId => {
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

                    {
                        editing && <DeleteImagesInput setFieldValue={setFieldValue} values={values}/>
                    }

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
                        <Button type="primary" htmlType="submit" >
                            {t('form.send')}
                        </Button>
                    </FormItem>
                </Form>;
            }
        }
    />
}

function AdminAddPets(){
    const history = useHistory();
    const {t} = useTranslation('admin');

    const {state} = useLogin();
    const {jwt} = state;

    const _onSubmit = async (values) => {
        try {
            const {id} = await createAdminPet(values, jwt);
            history.push(ADMIN_PET + id);
        }catch (e) {
            console.error(e)
            switch (e) {
                case CREATE_PET_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error message
                    break;
            }
        }
    };

    const users = useAdminUsers();

    return<BigCenteredContent>
        <h1>{t('addPetView.title')}</h1>

        <AddPetForm onSubmit={_onSubmit} users={users.adminUsers} />
    </BigCenteredContent>
}

export default AdminAddPets;