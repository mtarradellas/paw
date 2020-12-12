import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import 'antd/dist/antd.css';
import './css/html.css';

import HomeView from "./views/home/HomeView";
import BasicLayout from "./components/BasicLayout";

import RequestsView from "./views/requests/RequestsView";

import LoginContext from './constants/loginContext';


import {HOME, LOGIN, PET, REGISTER, USER, REQUESTS} from "./constants/routes";
import useLogin from "./hooks/useLogin";
import UserView from "./views/user/UserView";
import RegisterView from "./views/register/RegisterView";
import LoginView from "./views/login/LoginView";
import PetView from "./views/pet/PetView";

function App() {
    const login = useLogin();

    return (
        <LoginContext.Provider value={login}>
            <Router>
                <Switch>
                    <Route exact path={HOME}>
                        <BasicLayout>
                            <HomeView/>
                        </BasicLayout>
                    </Route>
                    <Route exact path={REGISTER}>
                        <BasicLayout>
                            <RegisterView/>
                        </BasicLayout>
                    </Route>
                    <Route exact path={LOGIN}>
                        <BasicLayout>
                            <LoginView/>
                        </BasicLayout>
                    </Route>
                    <Route exact path={USER + ':id'}
                        render={
                            ({id}) => (
                                <BasicLayout>
                                    <UserView id={id}/>
                                </BasicLayout>
                            )
                        }
                    />
                    <Route exact path={REQUESTS}>
                        <BasicLayout>
                            <RequestsView/>
                        </BasicLayout>
                    </Route>
                    <Route exact path={PET + ':id'}
                           render={
                               ({id}) => (
                                   <BasicLayout>
                                       <PetView id={id}/>
                                   </BasicLayout>
                               )
                           }
                    />
                </Switch>
            </Router>
        </LoginContext.Provider>
    );
}

export default App;
