import React from 'react';
import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER, HOME, REQUESTS, INTERESTS, ADD_PET} from "../constants/routes";
import {useTranslation} from "react-i18next";
import {Button} from "antd";
import useLogin from "../hooks/useLogin";


function LoggedIn(){
    const {t} = useTranslation('header');
    const {state, logout} = useLogin();

    const {username} = state;

    const _onLogout = () => {
        logout();
    };

    return <>
            <Link className={"header__subtitle"} to={ADD_PET}>
                {t('addPet')}
            </Link>

            <Link className={"header__subtitle"} to={REQUESTS}>
                {t('requests')}
            </Link>

            <Link className={"header__subtitle"} to={INTERESTS}>
                {t('interests')}
            </Link>

            <div className={"header__right header--session"}>
                <p className={"header--session--username"}>
                    {username}
                </p>

                <Button className={"header--session--logout"} onClick={_onLogout}>
                    {t('logout')}
                </Button>

            </div>
        </>
}

function NotLoggedIn(){
    const {t} = useTranslation('header');

    return <>
            <Link className={"header__subtitle"} to={ADD_PET}>
                {t('addPet')}
            </Link>

            <div className={"header__right"}>
                <Link className={"header__register"} to={REGISTER}>
                    {t('register')}
                </Link>

                <Link className={"header__login"} to={LOGIN}>
                    {t('login')}
                </Link>
            </div>
        </>
}


function Header() {
    const {state} = useLogin();

    const {isLoggedIn} = state;

    return <header>
        <Link to={HOME}>
            <img className={"header__logo"} src={"/logo.png"} alt={"logo"} width={70} height={70}/>
        </Link>

        <Link to={HOME}>
            <span className={"header__title"}>
                PET SOCIETY
            </span>
        </Link>


        {isLoggedIn ? <LoggedIn/> : <NotLoggedIn/>}

    </header>;
}

export default Header;