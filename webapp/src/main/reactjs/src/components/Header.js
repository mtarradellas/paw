import React/*, {useContext}*/ from 'react';
// import loginContext from "../constants/loginContext";

import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER} from "../constants/routes";

function Header(){
    // const {state} = useContext(loginContext);

    // const {username} = state;
    
    return <header>
        <img className={"header__logo"} src={"/logo.png"} alt={"logo"} width={70} height={70}/>

        <span className={"header__title"}>
            PET SOCIETY
        </span>

        <div className={"header__right"}>
            <Link className={"header__register"} to={REGISTER}>
                Register
            </Link>

            <Link className={"header__login"} to={LOGIN}>
                Login
            </Link>
        </div>

    </header>;
}

export default Header;