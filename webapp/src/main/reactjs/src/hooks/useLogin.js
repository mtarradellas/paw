import React, {useContext} from 'react';
import LoginContext from "../constants/loginContext";
import { useHistory } from 'react-router-dom'
import {HOME, LOGIN} from "../constants/routes";


function useLogin(){
    const {state, login: cLogin, logout: cLogout, promptLogin: cPromptLogin} = useContext(LoginContext);
    const history = useHistory();

    const login = ({username, jwt}) => {
        cLogin({username, jwt});

        const {promptLogin} = state;
        const {path, index} = promptLogin;

        console.log(path, index);

        console.log(history);

        if(index === history.length - 1){
            history.push(path);
        }else{
            history.push(HOME);
        }
    };

    const logout = () => {
        cLogout();

        history.push(HOME);
    };

    const promptLogin = () => {
        const index = history.length;
        const path = history.location.pathname;

        cPromptLogin({index, path});

        history.push(LOGIN);
    };

    return {state, login, logout, promptLogin}
}

export default useLogin;