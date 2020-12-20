import React from 'react';
import {useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import useLogin from "../../../hooks/useLogin";
import {PET} from "../../../constants/routes";
import {CREATE_REQUEST_ERRORS, createRequestAdmin} from "../../../api/admin/requests";
import BigCenteredContent from "../../../components/BigCenteredContent";

function AddRequestForm(){
    return <div>dfkanm</div>
}

function AddRequest() {
    const history = useHistory();

    const {t} = useTranslation('admin');

    const {state, promptLogin} = useLogin();
    const {jwt} = state;

    const _onSubmit = async (values) => {
        try {
            const id = await createRequestAdmin(values, jwt);

            history.push(PET + id);
        } catch (e) {
            console.error(e)
            switch (e) {
                case CREATE_REQUEST_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error message
                    break;
            }
        }
    }
    return <BigCenteredContent>
        <h1>{t("addRequestView.title")}</h1>

        <AddRequestForm/>
    </BigCenteredContent>
}

export default AddRequest;