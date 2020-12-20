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
import AdminLayout from "./components/AdminLayout";

import RequestsView from "./views/requests&interests/RequestsView";
import InterestsView from "./views/requests&interests/InterestsView";

import ErrorWithImage from "./views/errors/ErrorWithImage";

import AdminHome from "./views/admin/AdminHome";
import AdminRequests from "./views/admin/requests/AdminRequests";
import AdminUsers from "./views/admin/users/AdminUsers";
import AdminPets from "./views/admin/pets/AdminPets";

import LoginContext from './constants/loginContext';
import ConstantsContext from './constants/constantsContext';


import {
    HOME,
    LOGIN,
    PET,
    REGISTER,
    USER,
    EDIT_USER,
    REQUESTS,
    INTERESTS,
    ERROR_404,
    ERROR_404_PET,
    ERROR_404_USER,
    ACCESS_DENIED,
    WRONG_METHOD,
    BAD_REQUEST,
    INTERNAL_SERVER_ERROR,
    FORGOT_PASSWORD,
    VERIFY_EMAIL,
    RESET_PASSWORD,
    SUCCESS,
    ADMIN_HOME,
    ADMIN_PETS,
    ADMIN_USERS,
    ADMIN_REQUESTS,
    ADD_PET, EDIT_PET, ADMIN_USER
} from "./constants/routes";
import useLoginState from "./hooks/useLoginState";
import UserView from "./views/user/UserView";
import EditUserView from "./views/user/EditUserView";
import RegisterView from "./views/register/RegisterView";
import LoginView from "./views/login/LoginView";
import PetView from "./views/pet/PetView";
import useConstants from "./hooks/useConstants";
import {Spin} from "antd";
import PrivateRoute from "./components/PrivateRoute";
import AddPetView from "./views/addPet/AddPetView";
import useFormAndSearch from "./hooks/useFormAndSearch";
import FilterAndSearchContext from './constants/filterAndSearchContext'
import EditPetView from "./views/editPet/EditPetView";

import ForgotPasswordView from "./views/forgotPassword/ForgotPasswordView";
import ResetPasswordView from "./views/passwordReset/ResetPasswordView";
import EmailSent from "./views/information/EmailSent";
import OperationSuccessful from "./views/information/OperationSuccessful";
import AdminUserView from "./views/admin/AdminUserView";

function AppSwitch(){
    const {t} = useTranslation('error-pages');

    const formAndSearch = useFormAndSearch();

    return <FilterAndSearchContext.Provider value={formAndSearch}>
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

            <Route exact path={FORGOT_PASSWORD}>
                <BasicLayout>
                    <ForgotPasswordView/>
                </BasicLayout>
            </Route>
            <Route path={RESET_PASSWORD + ':token'}
                   component={
                       () => {
                           return <BasicLayout>
                               <ResetPasswordView/>
                           </BasicLayout>
                       }
                   }/>

            <Route exact path={VERIFY_EMAIL}>
                <BasicLayout>
                    <EmailSent/>
                </BasicLayout>
            </Route>

            <Route exact path={SUCCESS}>
                <BasicLayout>
                    <OperationSuccessful/>
                </BasicLayout>
            </Route>

            <PrivateRoute exact path={EDIT_USER}
                          component={
                              () => (<BasicLayout>
                                  <EditUserView/>
                              </BasicLayout>)
                          }
            />

            <PrivateRoute path={USER + ':id'}
                          component={
                              () => (<BasicLayout>
                                  <UserView/>
                              </BasicLayout>)
                          }
            />

            <PrivateRoute path={ADMIN_HOME}
                          component={
                              () => (
                                  <AdminLayout>
                                      <AdminHome/>
                                  </AdminLayout>
                              )
                          }
            />
            <PrivateRoute path={ADMIN_REQUESTS}
                          component={
                              () => (
                                  <AdminLayout>
                                      <AdminRequests/>
                                  </AdminLayout>
                              )}
            />
            <PrivateRoute path={ADMIN_USERS}
                          component={
                              () => (
                                  <AdminLayout>
                                      <AdminUsers/>
                                  </AdminLayout>
                              )
                          }
            />
            <PrivateRoute path={ADMIN_PETS}
                          component={
                              () => (
                                  <AdminLayout>
                                      <AdminPets/>
                                  </AdminLayout>
                              )
                          }
            />

            <PrivateRoute path={ADMIN_USER + ':id'}
                          component={
                              () => (
                                  <AdminLayout>
                                      <AdminUserView/>
                                  </AdminLayout>
                              )
                          }
            />

            <PrivateRoute path={ADD_PET}
                          component={
                              () => (<BasicLayout>
                                  <AddPetView/>
                              </BasicLayout>)
                          }
            />
            <PrivateRoute path={REQUESTS}
                          component={
                              () => (
                                  <BasicLayout>
                                      <RequestsView/>
                                  </BasicLayout>
                              )

                          }
            />
            <PrivateRoute path={INTERESTS}
                          component={
                              () => (
                                  <BasicLayout>
                                      <InterestsView/>
                                  </BasicLayout>
                              )
                          }
            />
            <Route exact path={PET + ':id'}
                   render={
                       () => (
                           <BasicLayout>
                               <PetView/>
                           </BasicLayout>
                       )
                   }
            />
            <Route exact path={EDIT_PET(':id')}
                   render={
                       () => (
                           <BasicLayout>
                               <EditPetView/>
                           </BasicLayout>
                       )
                   }
            />
            <Route path={ERROR_404}>
                <BasicLayout>
                    <ErrorWithImage title={t('error404')} image={"/images/page_not_found.png"}
                                    text={t('pageNotFound')}/>
                </BasicLayout>
            </Route>
            <Route path={ERROR_404_PET}>
                <BasicLayout>
                    <ErrorWithImage title={t('error404')} image={"/images/pet_not_found.png"}
                                    text={t('petNotFound')}/>
                </BasicLayout>
            </Route>
            <Route path={ERROR_404_USER}>
                <BasicLayout>
                    <ErrorWithImage title={t('error404')} image={"/images/user_not_found.png"}
                                    text={t('userNotFound')}/>
                </BasicLayout>
            </Route>
            <Route path={ACCESS_DENIED}>
                <BasicLayout>
                    <ErrorWithImage title={t('error403')} image={"/images/access_denied.png"}
                                    text={t('accessDenied')}/>
                </BasicLayout>
            </Route>
            <Route path={WRONG_METHOD}>
                <BasicLayout>
                    <ErrorWithImage title={t('error405')} image={"/images/access_denied.png"}
                                    text={t('wrongMethod')}/>
                </BasicLayout>
            </Route>
            <Route path={BAD_REQUEST}>
                <BasicLayout>
                    <ErrorWithImage title={t('error400')} image={"/images/access_denied.png"}
                                    text={t('badRequest')}/>
                </BasicLayout>
            </Route>
            <Route path={INTERNAL_SERVER_ERROR}>
                <BasicLayout>
                    <ErrorWithImage title={t('error500')}
                                    image={"/images/internal_server_error.png"}
                                    text={t('serverError')}/>
                </BasicLayout>
            </Route>
            <Redirect to={ERROR_404}/>
        </Switch>
    </FilterAndSearchContext.Provider>
}


function App() {
    const login = useLoginState();
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
                            <AppSwitch/>
                    }
                </Router>
            </ConstantsContext.Provider>
        </LoginContext.Provider>
    );
}

export default App;
