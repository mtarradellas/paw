import React from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {Table, Button, Divider, List, Rate} from "antd";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";

import '../../css/user/userView.css';
import {Link} from "react-router-dom";
import {HOME} from "../../constants/routes";

const ListItem = List.Item;

const nairobi =  {
    id: 1,
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
    ownerId: 1
};

const sampleUser = {
    username: 'fastiz',
    rating: 4.5,
    email: 'facuastiz@gmail.com',
    pets: [
        nairobi, nairobi
    ],
    reviews: [
        {username: 'jason', rating: 5, description: 'Excelente!', userId: 1},
        {username: 'juan', rating: 3.3, description: 'No tan excelente!', userId: 1}
    ]
}

function Content({user}){
    const {rating, email, pets, reviews} = user;

    const {t} = useTranslation('userView');

    const reviewColumns = [
        {
            title: t('reviews.user'),
            dataIndex: 'username'
        },
        {
            title: t('reviews.rating'),
            dataIndex: 'rating',
            render: rating => (
                <Rate allowHalf disabled defaultValue={rating}/>
            )
        },
        {
            title: t('reviews.description'),
            dataIndex: 'description'
        }
    ]

    console.log(rating);

    return <>
        <h1><b>{t('rating')}: <Rate allowHalf disabled defaultValue={rating}/></b> </h1>

        <p>({t('average', {rating, reviewCount: reviews.length})})   {t('averageClarification')}</p>

        <Divider/>

        <List bordered={true}>
            <ListItem>
                <b>{t('email')}:</b> {email}
            </ListItem>
        </List>

        <Divider/>

        <h1><b>{t('petsTitle')}</b> ({t('totalResults', {count: pets.length})})</h1>

        <div className={"user-view--pets-container"}>
            {
                pets.map(pet => (<PetCard pet={pet}/>))
            }
        </div>

        <Divider/>

        <h1><b>{t('reviewsTitle')}:</b></h1>

        <Table
            bordered={false}
            columns={reviewColumns}
            dataSource={reviews} size="small"
        />

        <Divider/>

        <div>
            <Link to={HOME}>
                {t('backToHome')}
            </Link>
        </div>

    </>
}

function UserView({id}){
    const user = sampleUser;
    const {username} = user;

    return <ContentWithHeader
            title={username}
            actionComponents={[
                <Button>Remover</Button>,
                <Button>Editar perfil</Button>
            ]}
            content={<Content user={user}/>}
        />
}

export default UserView;