import React from 'react';

import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {ADMIN_HOME, LOGIN, REGISTER, ADMIN_REQUESTS, ADMIN_PETS, ADMIN_USERS} from "../constants/routes";
import {Button} from "antd";
import useLogin from "../hooks/useLogin";


function AdminHeader() {
    const {t} = useTranslation('admin');

    const {state, logout} = useLogin();

    const {username, id} = state;

    const _onLogout = () => {
        logout();
    };

    return (
        <header className={"admin-header"}>
            <Link to={ADMIN_HOME}>
                <img className={"header__logo"} src={"/logo.png"} alt={"logo"} width={70} height={70}/>
            </Link>

            <Link to={ADMIN_HOME}>
            <span className={"header__title"}>
                PET SOCIETY ADMIN
            </span>
            </Link>

            <Link className={"header__subtitle"} to={ADMIN_REQUESTS}>
                {t('listRequests')}
            </Link>

            <Link className={"header__subtitle"} to={ADMIN_USERS}>
                {t('listUsers')}
            </Link>

            <Link className={"header__subtitle"} to={ADMIN_PETS}>
                {t('listPets')}
            </Link>

            <div className={"header__right header--session"}>
                <p className={"header--session--username"}>
                    {username}
                </p>

                <Button className={"header--session--logout"} onClick={_onLogout}>
                    {t('logout')}
                </Button>

            </div>
        </header>
    )
}

export default AdminHeader;