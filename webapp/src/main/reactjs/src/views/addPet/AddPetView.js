import React, {useState} from 'react';
import BigCenteredContent from "../../components/BigCenteredContent";
import {useTranslation} from "react-i18next";
import AddPetForm from "./AddPetForm";
import {CREATE_PET_ERRORS, createPet} from "../../api/pets";
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';
import {PET} from "../../constants/routes";
import {message} from 'antd';

function AddPetView(){
    const history = useHistory();
    const [submittingPet, setSubmittingPet] = useState(false);
    const {t} = useTranslation(['addPet', 'common']);

    const {state, promptLogin} = useLogin();
    const {jwt} = state;


    const _onSubmit = async (values) => {

        setSubmittingPet(true);
        try {
            const {id} = await createPet(values, jwt);
            history.push(PET + id);
        }catch (e) {

            switch (e) {
                case CREATE_PET_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case CREATE_PET_ERRORS.CONN_ERROR:
                default:
                    message.error(t('common:connError'));
                    break;
            }
        }
        setSubmittingPet(false);
    };

    return <BigCenteredContent>
        <h1>{t('title')}</h1>

        <AddPetForm onSubmit={_onSubmit} submitting={submittingPet}/>
    </BigCenteredContent>
}

export default AddPetView;