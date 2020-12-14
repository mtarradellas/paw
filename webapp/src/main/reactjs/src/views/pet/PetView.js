import React from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {useTranslation} from "react-i18next";
import {Button, Divider, List} from "antd";
import Questions from "./Questions";
import {Link} from "react-router-dom";
import {HOME, USER} from "../../constants/routes";


const ListItem = List.Item;

const nairobi =  {
    name: "Nairobi",
    specie: "Perro",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    uploadDate: "05-05-2019",
    description: "Es muy gentil con los chicos, muy pacifica.",
    dateOfBirth: "05-05-2019",
    province: 'Buenos Aires',
    department: 'Departamento',
    vaccinated: true,
    onAdoption: false,
    onSale: true,
    ownerId: 16
};

function isMale(sex){
    return sex === 'Male';
}

function ListItemRow({name, value}){
    return <ListItem>
        <b>{name}</b>: {value}
    </ListItem>;
}

function Content({name, specie, breed, price, sex, owner, uploadDate, img, onAdoption, onSale,
                     description, dateOfBirth, province, department, vaccinated, ownerId}){
    const {t} = useTranslation('petView');

    return <>
            <img alt="example" src="http://pawserver.it.itba.edu.ar/paw-2020a-7/img/1" />

            <p>{description}</p>

            <Divider/>

            <h2>
                { isMale(sex) ? t('someInformationAboutHim') : t('someInformationAboutHer') }:
            </h2>

            <List bordered={true}>
                <ListItemRow name={t('details.name')} value={name}/>
                <ListItemRow name={t('details.dateOfBirth')} value={dateOfBirth}/>
                <ListItemRow name={t('details.specie')} value={specie}/>
                <ListItemRow name={t('details.breed')} value={breed}/>
                <ListItemRow name={t('details.sex')} value={sex}/>
                <ListItemRow name={t('details.province')} value={province}/>
                <ListItemRow name={t('details.department')} value={department}/>
                <ListItemRow name={t('details.uploadDate')} value={uploadDate}/>
                <ListItemRow name={t('details.owner')} value={owner}/>
            </List>

            <Divider/>

            <h2>{t('status.header')}:</h2>

            <List bordered={true}>
                <ListItemRow name={t('status.vaccinated')} value={'' + vaccinated}/>
                <ListItemRow name={t('status.onAdoption')} value={'' + onAdoption}/>
                <ListItemRow name={t('status.onSale')} value={'' + onSale}/>
            </List>

            <Divider/>

            <h2>{t('questions.header')}:</h2>

            <Questions/>

            <Divider/>

            <div>
                <Link to={USER + ownerId}>
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

function PetView({id}){
    const {t} = useTranslation('petView');

    const {name} = nairobi;

    return <ContentWithHeader
        content={
            <Content {...nairobi}/>
        }
        actionComponents={
            [
                <Button key={0}>Mascota Ya Solicitada</Button>
            ]
        }
        title={t('title', {name})}
    />
}

export default PetView;