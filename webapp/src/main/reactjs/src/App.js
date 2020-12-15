import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";

import {useTranslation} from "react-i18next";

import 'antd/dist/antd.css';
import './css/html.css';

import HomeView from "./views/home/HomeView";
import BasicLayout from "./components/BasicLayout";

import RequestsView from "./views/requests&interests/RequestsView";
import InterestsView from "./views/requests&interests/InterestsView";

import ErrorWithImage from "./views/errors/ErrorWithImage";

import LoginContext from './constants/loginContext';
import ConstantsContext from './constants/constantsContext';


import {
    HOME,
    LOGIN,
    PET,
    REGISTER,
    USER,
    REQUESTS,
    INTERESTS,
    ERROR_404,
    ERROR_404_PET,
    ERROR_404_USER,
    ACCESS_DENIED,
    WRONG_METHOD,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR
} from "./constants/routes";
import useLogin from "./hooks/useLogin";
import UserView from "./views/user/UserView";
import RegisterView from "./views/register/RegisterView";
import LoginView from "./views/login/LoginView";
import PetView from "./views/pet/PetView";
import useConstants from "./hooks/useConstants";
import {Spin} from "antd";
import PrivateRoute from "./components/PrivateRoute";

function App() {
    const login = useLogin();
    const constants = useConstants();

    const {loaded} = constants;
    const {t} = useTranslation('error-pages');

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

                                <PrivateRoute path={USER + ':id'}
                                    component={
                                        <BasicLayout>
                                            <UserView/>
                                        </BasicLayout>
                                    }
                                />

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
                                <Route path={ERROR_404}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error404')} image={"/images/page_not_found.png"} text={t('pageNotFound')} />
                                    </BasicLayout>
                                </Route>
                                <Route path={ERROR_404_PET}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error404')} image={"/images/pet_not_found.png"} text={t('petNotFound')} />
                                    </BasicLayout>
                                </Route>
                                <Route path={ERROR_404_USER}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error404')} image={"/images/user_not_found.png"} text={t('userNotFound')} />
                                    </BasicLayout>
                                </Route>
                                <Route path={ACCESS_DENIED}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error403')} image={"/images/access_denied.png"} text={t('accessDenied')}/>
                                    </BasicLayout>
                                </Route>
                                <Route path={WRONG_METHOD}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error405')} image={"/images/access_denied.png"} text={t('wrongMethod')}/>
                                    </BasicLayout>
                                </Route>
                                <Route path={BAD_REQUEST}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error400')} image={"/images/access_denied.png"} text={t('badRequest')}/>
                                    </BasicLayout>
                                </Route>
                                <Route path={INTERNAL_SERVER_ERROR}>
                                    <BasicLayout>
                                        <ErrorWithImage title={t('error500')} image={"/images/internal_server_error.png"} text={t('serverError')}/>
                                    </BasicLayout>
                                </Route>
                                <Redirect to={ERROR_404}/>
                            </Switch>
                    }
                </Router>
            </ConstantsContext.Provider>
        </LoginContext.Provider>
    );
}

export default App;
