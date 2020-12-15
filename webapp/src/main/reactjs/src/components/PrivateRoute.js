import React, {useContext} from 'react';
import {Redirect, Route} from "react-router-dom";
import {LOGIN} from "../constants/routes";
import LoginContext from "../constants/loginContext";


const PrivateRoute = ({children, ...rest}) => {
    const {state} = useContext(LoginContext);
    const {isLoggedIn} = state;

    return <Route {...rest}>
        {
            isLoggedIn ?
                {children}
                :
                <Redirect to={LOGIN}/>
        }
    </Route>;

};

export default PrivateRoute;