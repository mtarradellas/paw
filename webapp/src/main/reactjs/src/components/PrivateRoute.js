import React, {useEffect} from 'react';
import {Route} from "react-router-dom";
import useLogin from "../hooks/useLogin";

const RedirectPromptLogin = () => {
    const {promptLogin} = useLogin();

    useEffect(()=>{
        promptLogin();
    }, []);

    return <></>;
};

const PrivateRoute = ({component: Component, ...rest}) => {
    const {state} = useLogin();
    const {isLoggedIn} = state;

    return <Route {...rest}>
        {
            isLoggedIn ?
                <Component/>
                :
                <RedirectPromptLogin/>
        }
    </Route>;

};

export default PrivateRoute;