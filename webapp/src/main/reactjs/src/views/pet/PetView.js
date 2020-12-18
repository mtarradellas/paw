import React, {useEffect, useState, useContext} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {useTranslation} from "react-i18next";
import {Button, Divider, List, Spin} from "antd";
import Questions from "./Questions";
import {Link} from "react-router-dom";
import {HOME, USER} from "../../constants/routes";
import _ from 'lodash';
import {getPet} from "../../api/pets";
import {useParams} from 'react-router-dom';
import {petImageSrc} from "../../api/images";
import ConstantsContext from '../../constants/constantsContext';
import {CloseOutlined, CheckOutlined} from '@ant-design/icons';

const ListItem = List.Item;

function isMale(sex){
    return _.toLower(sex) === 'male';
}

function ListItemRow({name, value}){
    return <ListItem>
        <b>{name}</b>: {_.isNil(value) ? <Spin/> : value}
    </ListItem>;
}

function Content({pet}){
    const {t} = useTranslation('petView');

    const {breeds, species, provinces, departments} = useContext(ConstantsContext);

    const {
        petName,
        birthDate,
        gender,
        vaccinated,
        price,
        uploadDate,
        description,
        username,
        userId,
        speciesId,
        breedId,
        provinceId,
        departmentId,
        images
    } = pet;

    console.log(species);

    return <>
            {
                _.isNil(description) ?
                    <Spin/>
                    :
                    <>
                        <img alt="example" src={petImageSrc(images[0])}/>

                        <p>{description}</p>
                    </>
            }


            <Divider/>

            {
                price === 0 ?
                    <h2>{t('status.onAdoption')}</h2>
                    :
                    <h2>{t('status.onSale')}: ${price}</h2>
            }

            <Divider/>


            <h2>
                { isMale(gender) ? t('someInformationAboutHim') : t('someInformationAboutHer') }:
            </h2>

            <List bordered={true}>
                <ListItemRow name={t('details.name')} value={petName}/>
                <ListItemRow name={t('details.dateOfBirth')} value={birthDate}/>
                <ListItemRow name={t('details.specie')} value={speciesId && species[speciesId].name}/>
                <ListItemRow name={t('details.breed')} value={breedId && breeds[breedId].name}/>
                <ListItemRow name={t('details.sex')} value={t('details.' + _.toLower(gender))}/>
                <ListItemRow name={t('details.province')} value={provinceId && provinces[provinceId].name}/>
                <ListItemRow name={t('details.department')} value={departmentId && departments[departmentId].name}/>
                <ListItemRow name={t('details.uploadDate')} value={uploadDate}/>
                <ListItemRow name={t('details.owner')} value={<Link to={USER + userId}>{username}</Link>}/>
                <ListItemRow name={t('status.vaccinated')}
                             value={
                                 vaccinated ?
                                     <CheckOutlined />
                                     :
                                     <CloseOutlined />
                             }
                />
            </List>

            <Divider/>

            <h2>{t('questions.header')}:</h2>

            <Questions/>

            <Divider/>

            <div>
                <Link to={USER + userId}>
                    <Button>{t('goToOwnersPage')}</Button>
                </Link>
            </div>

            <div>
                <Link to={HOME}>
                    {t('backToHome')}
                </Link>
            </div>
        </>;
}

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

function PetView(){
    const {id} = useParams();

    const [pet, setPet] = useState(initialStatePet);

    const {t} = useTranslation('petView');

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

    const {petName} = pet;

    return <ContentWithHeader
        content={
            <Content pet={pet}/>
        }
        actionComponents={
            [
                <Button key={0}>Mascota Ya Solicitada</Button>
            ]
        }
        title={petName ? t('title', {name: petName}) : <Spin/>}
    />
}

export default PetView;