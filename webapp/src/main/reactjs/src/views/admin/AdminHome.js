import React from 'react';

import {useTranslation} from "react-i18next";

import {ADMIN_USERS, ADMIN_PETS, ADMIN_REQUESTS, ADMIN_ADD_PET, ADMIN_ADD_REQUEST, ADMIN_ADD_USER} from '../../constants/routes'

import SmallCenteredContent from "../../components/SmallCenteredContent";
import {Button} from "antd";

function AdminHome() {
    const {t} = useTranslation('admin');

    return (
        <SmallCenteredContent>
            <h1><b>{t('home.welcome')}</b></h1>
            <p>{t('home.listsTitle')}</p>
            <Button href={ADMIN_REQUESTS} block>{t('listRequests')}</Button>
            &nbsp;
            <Button href={ADMIN_USERS} block>{t('listUsers')}</Button>
            &nbsp;
            <Button href={ADMIN_PETS} block>{t('listPets')}</Button>
            &nbsp;&nbsp;
            <p>{t('home.addTitle')}</p>
            <Button href={ADMIN_ADD_REQUEST} type={"primary"} block >{t('addRequest')}</Button>
            &nbsp;
            <Button href={ADMIN_ADD_USER} type={"primary"} block>{t('addUser')}</Button>
            &nbsp;
            <Button href={ADMIN_ADD_PET} type={"primary"} block>{t('addPet')}</Button>

        </SmallCenteredContent>
    )
}

export default AdminHome;