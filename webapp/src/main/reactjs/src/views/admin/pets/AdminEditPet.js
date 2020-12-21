import React, {useEffect, useState} from 'react';
import {useHistory, useParams} from "react-router-dom";
import {useTranslation} from "react-i18next";
import useLogin from "../../../hooks/useLogin";
import {editPetAdmin, getPetAdmin} from "../../../api/admin/pets";
import {EDIT_PET_ERRORS, editPet} from "../../../api/pets";
import {ADMIN_PET, PET} from "../../../constants/routes";
import {message} from "antd";
import AddPetForm from "../../addPet/AddPetForm";
import BigCenteredContent from "../../../components/BigCenteredContent";


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


function AdminEditPets(){
    const {id} = useParams();
    const [pet, setPet] = useState(initialStatePet);
    const history = useHistory();

    const {t} = useTranslation('editPet');

    const {jwt} = useLogin().state;

    const fetchPet = async () => {
        try{
            const result = await getPetAdmin(id,jwt);

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
            await editPetAdmin(values, id, jwt);
            history.push(ADMIN_PET + id);
        }catch (e) {
            console.error(e)
            switch (e) {
                case EDIT_PET_ERRORS.FORBIDDEN:
                    break;
                case EDIT_PET_ERRORS.IMAGE_QUANTITY_ERROR:
                    message.error(t('form.imageError'));
                    break;
                case EDIT_PET_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error message
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

export default AdminEditPets;