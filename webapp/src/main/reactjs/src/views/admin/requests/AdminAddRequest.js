import React from 'react';
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import useLogin from "../../../hooks/useLogin";
import {ADMIN_REQUESTS, PET} from "../../../constants/routes";
import {CREATE_REQUEST_ERRORS, createRequestAdmin} from "../../../api/admin/requests";
import BigCenteredContent from "../../../components/BigCenteredContent";
import useAdminPets from "../../../hooks/admin/usePets";
import useAdminUsers from "../../../hooks/admin/useUsers";
import { Form, Select} from "formik-antd";
import {Button} from "antd";
import {Formik} from "formik";

const FormItem = Form.Item;

function AddRequestForm({users, pets, _onSubmit}) {
    const {t} = useTranslation('admin');
    return <Formik
        onSubmit={_onSubmit}
        initialValues={{}}
    >
        <Form>
            <FormItem name={"petId"} label={t('addRequestView.petName')}>
                <Select name={"petId"} placeholder={t('addRequestView.petName')}>
                    {
                        pets && pets.map((pet) => {
                            return <Select.Option value={pet.id}
                                                  key={pet.id}>{pet.petName}</Select.Option>
                        })
                    }
                </Select>
            </FormItem>
            <FormItem name={"userId"} label={t('addRequestView.username')}>
                <Select name={"userId"} placeholder={t('addRequestView.username')}>
                    {
                        users && users.map((user) => {
                            return <Select.Option value={user.id}
                                                  key={user.id}>{user.username}</Select.Option>
                        })
                    }
                </Select>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit">
                    {t('addRequestView.addRequest')}
                </Button>
            </FormItem>
        </Form>
    </Formik>
}

function AdminAddRequest() {
    const history = useHistory();

    const {t} = useTranslation('admin');

    const {state} = useLogin();
    const {jwt} = state;

    const pets = useAdminPets();
    const users = useAdminUsers();


    const _onSubmit = async (values, {setErrors}) => {
        try {
            await createRequestAdmin(values.userId,values.petId, jwt);

            history.push(ADMIN_REQUESTS);
        } catch (e) {
            switch (e) {
                case CREATE_REQUEST_ERRORS.DUPLICATE:
                    setErrors({userId: t('errors.duplicatedRequest')});
                    break;
                case CREATE_REQUEST_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error message
                    break;
            }
        }
    }
    return <BigCenteredContent>
        <h1>{t("addRequestView.title")}</h1>

        <AddRequestForm pets={pets.adminPets} users={users.adminUsers} _onSubmit={_onSubmit}/>
    </BigCenteredContent>
}

export default AdminAddRequest;