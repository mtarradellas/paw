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

import RequestsView from "./views/requests&interests/RequestsView";
import InterestsView from "./views/requests&interests/InterestsView";

import LoginContext from './constants/loginContext';
import ConstantsContext from './constants/constantsContext';


import {HOME, LOGIN, PET, REGISTER, USER, REQUESTS, INTERESTS} from "./constants/routes";
import useLogin from "./hooks/useLogin";
import UserView from "./views/user/UserView";
import RegisterView from "./views/register/RegisterView";
import LoginView from "./views/login/LoginView";
import PetView from "./views/pet/PetView";
import useConstants from "./hooks/useConstants";
import {Spin} from "antd";

function App() {
    const login = useLogin();
    const constants = useConstants();

    const {loaded} = constants;

    return (
        <LoginContext.Provider value={login}>
            <ConstantsContext.Provider value={constants}>
                <Router>
                    {
                        !loaded ?
                            <Spin/>
                            :
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

                                <Route path={USER + ':id'}>
                                    <BasicLayout>
                                        <UserView/>
                                    </BasicLayout>
                                </Route>

                                <Route exact path={REQUESTS}>
                                    <BasicLayout>
                                        <RequestsView/>
                                    </BasicLayout>
                                </Route>
                                <Route exact path={INTERESTS}>
                                    <BasicLayout>
                                        <InterestsView/>
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
                    }
                </Router>
            </ConstantsContext.Provider>
        </LoginContext.Provider>
    );
}

export default App;
