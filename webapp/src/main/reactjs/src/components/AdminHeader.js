import React from 'react';

import {Link} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {ADMIN_HOME, LOGIN, REGISTER, ADMIN_REQUESTS, ADMIN_PETS, ADMIN_USERS} from "../constants/routes";
import {Button} from "antd";
import useLogin from "../hooks/useLogin";

import '../css/admin/admin.css'


function AdminHeader() {
    const {t} = useTranslation('admin');

    const {state, logout} = useLogin();

    const {username, id} = state;

    const _onLogout = () => {
        logout();
    };

    return (
        <header className={"admin-header"}>
            <Link to={ADMIN_HOME} className={"header__logo"} >
                <img src={"/logo.png"} alt={"logo"}/>
            </Link>

            <Link to={ADMIN_HOME} className={"header__title"}>
            <span>
                PET SOCIETY
            </span>
            </Link>

            <div className={"header__menu-items"}>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_REQUESTS}>
                        {t('listRequests')}
                    </Link>
                </div>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_USERS}>
                        {t('listUsers')}
                    </Link>
                </div>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_PETS}>
                        {t('listPets')}
                    </Link>
                </div>

            </div>


            <div className={"header__username-and-logout"}>
                <p className={"header__username-and-logout__username"}>
                    {username}(Admin)
                </p>

                <Button className={"header__username-and-logout__logout"} onClick={_onLogout}>
                    {t('logout')}
                </Button>

            </div>
        </header>
    )
}

export default AdminHeader;