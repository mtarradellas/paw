import React, {useEffect} from 'react';
import {Redirect, Route} from "react-router-dom";
import useLogin from "../hooks/useLogin";
import {Spin} from "antd";
import _ from 'lodash';
import {HOME} from "../constants/routes";

const RedirectPromptLogin = () => {
    const {promptLogin} = useLogin();

    useEffect(() => {
        promptLogin();
    }, []);

    return <></>;
};

const Admin = ({component: Component}) => {
    const {state} = useLogin();
    const {isLoggedIn, isAdmin} = state;

    if(!isLoggedIn)
        return <RedirectPromptLogin/>;

    if(_.isNil(isAdmin))
        return <Spin/>;

    return isAdmin ?
        <Component/>
        :
        <Redirect to={HOME}/>;
};

const NonAdmin = ({component: Component}) => {
    const {state} = useLogin();
    const {isLoggedIn} = state;

    return isLoggedIn ?
        <Component/>
        :
        <RedirectPromptLogin/>
};

const PrivateRoute = ({adminPage, component, ...rest}) => {
    const {state} = useLogin();
    const {isLoggedIn} = state;

    return <Route {...rest}>
        {
            _.isNil(isLoggedIn) ?
                <Spin/>
                :
                adminPage ?
                    <Admin component={component}/>
                    :
                    <NonAdmin component={component}/>
        }
    </Route>;

};

export default PrivateRoute;