import React, {useContext, useEffect} from 'react';
import LoginContext from "../constants/loginContext";
import { useHistory } from 'react-router-dom'
import {HOME, LOGIN} from "../constants/routes";
import _ from 'lodash';

const LOCAL_STORAGE_AUTH_KEY = "LOCAL_STORAGE_AUTH_KEY";

function useLogin(){
    const {state, login: cLogin, logout: cLogout, promptLogin: cPromptLogin, setUserInfo} = useContext(LoginContext);
    const history = useHistory();

    const login = ({username, jwt, id, mail, isAdmin, status}, rememberMe) => {
        cLogin({username, jwt});

        setUserInfo({id, mail, isAdmin, status});

        if(rememberMe){
            localStorage.setItem(LOCAL_STORAGE_AUTH_KEY, JSON.stringify({username, jwt, id, mail, isAdmin, status}))
        }

        const {promptLogin} = state;
        const {path} = promptLogin;

        if(!_.isNil(path)){
            history.push(path);
        }else{
            history.push(HOME);
        }
    };

    const logout = () => {
        cLogout();

        history.push(HOME);

        localStorage.removeItem(LOCAL_STORAGE_AUTH_KEY);
    };

    const promptLogin = () => {
        const path = history.location.pathname;

        cPromptLogin({path});

        history.push(LOGIN);
    };

    const relog = () => {
        cLogout();
        localStorage.removeItem(LOCAL_STORAGE_AUTH_KEY);
        
        const path = history.location.pathname;
        cPromptLogin({path});
        history.push(LOGIN);
    };

    useEffect(()=>{
        return history.listen((location, action) => {
            if(location.pathname === LOGIN || action !== 'PUSH')
                return;

            cPromptLogin({path: null});
        });
    }, [history.listen, history.unlisten]);

    return {state, login, logout, promptLogin, relog}
}

export default useLogin;