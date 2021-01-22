import React from 'react';

import {useTranslation} from "react-i18next";

import {
    ADMIN_USERS,
    ADMIN_PETS,
    ADMIN_REQUESTS,
    ADMIN_ADD_PET,
    ADMIN_ADD_REQUEST,
    ADMIN_ADD_USER
} from '../../constants/routes'

import SmallCenteredContent from "../../components/SmallCenteredContent";
import {Button} from "antd";
import {Link} from "react-router-dom";

function AdminHome() {
    const {t} = useTranslation('admin');

    return (
        <SmallCenteredContent>
            <h1><b>{t('home.welcome')}</b></h1>
            <p>{t('home.listsTitle')}</p>
            <Link to={ADMIN_REQUESTS}>
                <Button block>{t('listRequests')}</Button>
            </Link>
            &nbsp;
            <Link to={ADMIN_USERS}>
                <Button block>{t('listUsers')}</Button>
            </Link>
            &nbsp;
            <Link to={ADMIN_PETS}>
                <Button block>{t('listPets')}</Button>
            </Link>
            &nbsp;&nbsp;
            <p>{t('home.addTitle')}</p>
            <Link to={ADMIN_ADD_REQUEST}>
                <Button type={"primary"} block>{t('addRequest')}</Button>
            </Link>
            &nbsp;
            <Link to={ADMIN_ADD_USER}>
                <Button type={"primary"} block>{t('addUser')}</Button>
            </Link>
            &nbsp;
            <Link to={ADMIN_ADD_PET}>
                <Button type={"primary"} block>{t('addPet')}</Button>
            </Link>
        </SmallCenteredContent>
    )
}

export default AdminHome;