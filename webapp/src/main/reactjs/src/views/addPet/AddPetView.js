import React from 'react';
import BigCenteredContent from "../../components/BigCenteredContent";
import {useTranslation} from "react-i18next";
import AddPetForm from "./AddPetForm";
import {CREATE_PET_ERRORS, createPet} from "../../api/pets";
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';
import {PET} from "../../constants/routes";

function AddPetView(){
    const history = useHistory();

    const {t} = useTranslation('addPet');

    const {state, promptLogin} = useLogin();
    const {jwt} = state;

    const _onSubmit = async (values) => {
        try {
            const id = await createPet(values, jwt);

            history.push(PET + id);
        }catch (e) {
            console.error(e)
            switch (e) {
                case CREATE_PET_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case CREATE_PET_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error message
                    break;
            }
        }
    };

    return <BigCenteredContent>
        <h1>{t('title')}</h1>

        <AddPetForm onSubmit={_onSubmit}/>
    </BigCenteredContent>
}

export default AddPetView;