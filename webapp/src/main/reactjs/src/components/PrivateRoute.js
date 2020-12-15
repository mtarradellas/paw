import React, {useContext} from 'react';
import {Redirect, Route} from "react-router-dom";
import {LOGIN} from "../constants/routes";
import LoginContext from "../constants/loginContext";


const PrivateRoute = ({component: Component, ...rest}) => {
    const {state} = useContext(LoginContext);
    const {isLoggedIn} = state;

    return <Route {...rest} render={
        props => (
            isLoggedIn ?
                <Component {...props}/>
                :
                <Redirect to={LOGIN}/>
        )
    }/>

};

export default PrivateRoute;