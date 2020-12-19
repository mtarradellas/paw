import React, {useEffect, useState} from 'react';
import BigCenteredContent from "../../components/BigCenteredContent";
import {useTranslation} from "react-i18next";
import AddPetForm from "../addPet/AddPetForm";
import {useParams} from 'react-router-dom';
import {getPet} from "../../api/pets";


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
        specie: speciesId,
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

    const {t} = useTranslation('editPet');

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
        console.log(values);
    };

    const {petName} = pet;

    return <BigCenteredContent>
        <h1>{t('title', {name: petName})}</h1>

        <AddPetForm onSubmit={_onSubmit} editing initialValues={mapPetToForm(pet)}/>
    </BigCenteredContent>
}

export default EditPetView;