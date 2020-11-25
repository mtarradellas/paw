import React/*, {useContext}*/ from 'react';
// import loginContext from "../constants/loginContext";

import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER} from "../constants/routes";
import {useTranslation} from "react-i18next";

function Header(){
    const {t} = useTranslation('header');
    // const {state} = useContext(loginContext);

    // const {username} = state;
    
    return <header>
        <img className={"header__logo"} src={"/logo.png"} alt={"logo"} width={70} height={70}/>

        <span className={"header__title"}>
            PET SOCIETY
        </span>

        <div className={"header__right"}>
            <Link className={"header__register"} to={REGISTER}>
                {t('register')}
            </Link>

            <Link className={"header__login"} to={LOGIN}>
                {t('login')}
            </Link>
        </div>

    </header>;
}

export default Header;