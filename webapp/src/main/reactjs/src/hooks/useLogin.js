import React, {useContext, useEffect} from 'react';
import LoginContext from "../constants/loginContext";
import { useHistory } from 'react-router-dom'
import {HOME, LOGIN} from "../constants/routes";
import _ from 'lodash';


function useLogin(){
    const {state, login: cLogin, logout: cLogout, promptLogin: cPromptLogin, setUserInfo} = useContext(LoginContext);
    const history = useHistory();

    const login = ({username, jwt, id, mail, isAdmin, status}) => {
        cLogin({username, jwt});
        console.log("sadsa")
        setUserInfo({id, mail, isAdmin, status});
        console.log("fdAAAAAAsa")
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
    };

    const promptLogin = () => {
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

    return {state, login, logout, promptLogin}
}

export default useLogin;