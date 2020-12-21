import React, {useEffect, useState} from 'react';
import BigCenteredContent from "../../components/BigCenteredContent";
import {useTranslation} from "react-i18next";
import AddPetForm from "../addPet/AddPetForm";
import {useParams} from 'react-router-dom';
import {EDIT_PET_ERRORS, editPet, getPet} from "../../api/pets";
import {useHistory} from 'react-router-dom';
import {PET} from "../../constants/routes";
import useLogin from "../../hooks/useLogin";
import {message} from "antd";


const initialStatePet = {
    petName: null,
    birthDate: null,
    gender: null,
    vaccinated: null,
    price: null,
    uploadDate: null,
    description: null,
    status: null,
    username: null,
    userId: null,
    speciesId: null,
    breedId: null,
    provinceId: null,
    departmentId: null,
    images: null
};

const mapPetToForm = ({
                      petName,
                      birthDate,
                      gender,
                      vaccinated,
                      price,
                      uploadDate,
                      description,
                      status,
                      username,
                      userId,
                      speciesId,
                      breedId,
                      provinceId,
                      departmentId,
                      images
                      }) => {
    return {
        petName,
        price,
        isAdoption: price === 0,
        description: description,
        province: provinceId,
        department: departmentId,
        species: speciesId,
        breed: breedId,
        dateOfBirth: birthDate,
        isVaccinated: vaccinated,
        gender: gender,
        filesToDelete: [],
        files: [],
        images
    };
};

function EditPetView(){
    const {id} = useParams();
    const [pet, setPet] = useState(initialStatePet);
    const history = useHistory();

    const {t} = useTranslation(['editPet', 'common']);

    const {state, promptLogin} = useLogin();
    const {jwt} = state;

    const fetchPet = async () => {
        try{
            const result = await getPet({petId: id});

            setPet(result);
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchPet();
    }, []);

    const _onSubmit = async (values) => {
        try {
            await editPet(values, id, jwt);
            history.push(PET + id);
        }catch (e) {
            switch (e) {
                case EDIT_PET_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case EDIT_PET_ERRORS.IMAGE_QUANTITY_ERROR:
                    message.error(t('form.imageError'));
                    break;
                case EDIT_PET_ERRORS.CONN_ERROR:
                default:
                    message.error(t('common:connError'));
                    break;
            }
        }
    };

    const {petName} = pet;

    return <BigCenteredContent>
        <h1>{t('title', {name: petName})}</h1>

        <AddPetForm onSubmit={_onSubmit} editing initialValues={mapPetToForm(pet)}/>
    </BigCenteredContent>
}

export default EditPetView;