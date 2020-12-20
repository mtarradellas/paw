import React, {useState, useEffect} from 'react';
import useLogin from "../../../hooks/useLogin";
import {useParams} from "react-router-dom";
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {
    EDIT_REQUESTS_ERROR,
    editAdminRequest,
    GET_REQUESTS_ERROR,
    getRequestAdmin
} from "../../../api/admin/requests";
import {ADMIN_REQUESTS} from "../../../constants/routes";
import BigCenteredContent from "../../../components/BigCenteredContent";
import {Formik} from "formik";
import {Form, Select} from "formik-antd";
import {Button, Spin} from "antd";


const FormItem = Form.Item;

function EditRequestForm({status,_onSubmit}){
    const {t} = useTranslation('admin');

    const statusLocale = [
        t('status.pending'),
        t('status.accepted'),
        t('status.rejected'),
        t('status.canceled'),
        t('status.sold')
    ]

    return <Formik
        onSubmit={_onSubmit}
        initialValues={{}}
    >
        <Form>
            <FormItem name={"status"} label={t('editRequestView.status')}>
                <Select name={"status"} placeholder={t("editRequestView.status")}>
                    <Select.Option value={0} >{statusLocale[0]}</Select.Option>
                    <Select.Option value={1} >{statusLocale[1]}</Select.Option>
                    <Select.Option value={2} >{statusLocale[2]}</Select.Option>
                    <Select.Option value={3} >{statusLocale[3]}</Select.Option>
                    <Select.Option value={4} >{statusLocale[4]}</Select.Option>
                </Select>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit">
                    {t('addRequestView.addRequest')}
                </Button>
            </FormItem>
        </Form>
    </Formik>}

function AdminEditRequest() {
    const {jwt} = useLogin().state;
    const {id} = useParams();

    const history = useHistory();

    const [status, setStatus] = useState(null)

    const {t} = useTranslation('admin');

    const fetchRequest = async () => {
        try {
            const request = await getRequestAdmin(id, jwt);
            setStatus(request.status);
        } catch (e) {
            throw GET_REQUESTS_ERROR.CON_ERROR;
        }
    }

    useEffect(() => {
        fetchRequest();
    }, []);

    const _onSubmit = async (values, {setErrors}) => {
        try {
            if(values.status === status){
                setErrors({status: t('errors.unchanged')});
            }else{
                await editAdminRequest(values.status, id, jwt);
                history.push(ADMIN_REQUESTS);
            }
        } catch (e) {
            switch (e) {
                case EDIT_REQUESTS_ERROR.CONN_ERROR:
                default:
                    setErrors({status: t('errors.default')});
                    break;
            }
        }
    }

    return <BigCenteredContent>
        <h1>{t("editRequestView.title")} </h1>
        <EditRequestForm status={status} _onSubmit={_onSubmit} />

    </BigCenteredContent>
}

export default AdminEditRequest;