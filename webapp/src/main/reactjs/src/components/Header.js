import React, {useContext} from 'react';
import loginContext from "../constants/loginContext";

import '../css/header.css';
import {Link, useHistory} from "react-router-dom";
import {LOGIN, REGISTER, HOME, REQUESTS, INTERESTS} from "../constants/routes";
import {useTranslation} from "react-i18next";
import {Button} from "antd";


function LoggedIn(){
    const {t} = useTranslation('header');
    const {state, logout} = useContext(loginContext);
    const history = useHistory();

    const {username} = state;

    const _onLogout = () => {
        logout();

        history.push(HOME);
    };

    return <>
            <Link className={"header__subtitle"} to={REQUESTS}>
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
            <Link className={"header__subtitle"} to={REQUESTS}>
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
    const {state} = useContext(loginContext);

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