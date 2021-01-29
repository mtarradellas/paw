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
    ADD_PET,
    EDIT_PET,
    ADMIN_USER,
    ADMIN_PET,
    ADMIN_ADD_PET,
    ADMIN_ADD_REQUEST,
    ADMIN_ADD_USER,
    ADMIN_EDIT_PET,
    ADMIN_EDIT_USER,
    ADMIN_EDIT_REQUEST, ACTIVATE_ACCOUNT, ADMIN
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
import AdminPetView from "./views/admin/AdminPetView";
import AdminAddRequest from "./views/admin/requests/AdminAddRequest";
import AdminEditRequest from "./views/admin/requests/AdminEditRequest";
import AdminAddUser from "./views/admin/users/AdminAddUser";
import AdminEditUser from "./views/admin/users/AdminEditUser";
import AdminAddPet from "./views/admin/pets/AdminAddPet";
import AdminEditPet from "./views/admin/pets/AdminEditPet";
import {CONTEXT} from "./config";
import ActivateAccountView from "./views/ActivateAccountView";
import ScrollToTop from "./components/ScrollToTop";

function AppSwitch(){
    const {t} = useTranslation('error-pages');

    const formAndSearch = useFormAndSearch();

    return <FilterAndSearchContext.Provider value={formAndSearch}>
        <Switch>
            <Route path={ADMIN}>
                <AdminLayout>
                    <Switch>

                        <PrivateRoute adminPage path={ADMIN_HOME}
                                      component={AdminHome}
                        />
                        <PrivateRoute adminPage path={ADMIN_REQUESTS}
                                      component={AdminRequests}
                        />
                        <PrivateRoute adminPage path={ADMIN_USERS}
                                      component={AdminUsers}
                        />
                        <PrivateRoute adminPage path={ADMIN_PETS}
                                      component={AdminPets}
                        />

                        <PrivateRoute adminPage path={ADMIN_USER + ':id'}
                                      component={AdminUserView}
                        />
                        <PrivateRoute adminPage path={ADMIN_PET + ':id'}
                                      component={AdminPetView}
                        />

                        <PrivateRoute adminPage path={ADMIN_ADD_REQUEST}
                                      component={AdminAddRequest}
                        />

                        <PrivateRoute adminPage path={ADMIN_EDIT_REQUEST + ':id'}
                                      component={AdminEditRequest}
                        />

                        <PrivateRoute adminPage path={ADMIN_ADD_USER}
                                      component={AdminAddUser}
                        />

                        <PrivateRoute adminPage path={ADMIN_EDIT_USER + ':id'}
                                      component={AdminEditUser}
                        />

                        <PrivateRoute adminPage path={ADMIN_ADD_PET}
                                      component={AdminAddPet}
                        />

                        <PrivateRoute adminPage path={ADMIN_EDIT_PET + ':id'}
                                      component={AdminEditPet}
                        />


                        <Route exact path={"/admin"}>
                            <Redirect to={ADMIN_HOME}/>
                        </Route>

                    </Switch>
                </AdminLayout>
            </Route>

            <Route>
                <BasicLayout>
                    <Switch>

                        <Route exact path={HOME}>
                            <HomeView/>
                        </Route>

                        <Route exact path={REGISTER}>
                                <RegisterView/>
                        </Route>

                        <Route exact path={LOGIN}>
                            <LoginView/>
                        </Route>

                        <Route exact path={FORGOT_PASSWORD}>
                            <ForgotPasswordView/>
                        </Route>

                        <Route path={RESET_PASSWORD + ':token'}
                               component={ResetPasswordView}/>

                        <Route path={ACTIVATE_ACCOUNT + ':token'}
                               component={ActivateAccountView}/>

                        <Route exact path={VERIFY_EMAIL}>
                            <EmailSent/>
                        </Route>

                        <Route exact path={SUCCESS}>
                            <OperationSuccessful/>
                        </Route>

                        <PrivateRoute exact path={EDIT_USER}
                                      component={EditUserView}
                        />

                        <PrivateRoute exact path={USER + ':id'}
                                      component={UserView}
                        />

                        <PrivateRoute path={ADD_PET}
                                      component={AddPetView}
                        />

                        <PrivateRoute path={REQUESTS}
                                      component={RequestsView}
                        />
                        <PrivateRoute path={INTERESTS}
                                      component={InterestsView}
                        />
                        <Route exact path={PET + ':id'}
                               component={PetView}
                        />

                        <Route exact path={EDIT_PET(':id')}
                               component={EditPetView}
                        />
                        <Route path={ERROR_404}>
                            <ErrorWithImage title={t('error404')} image={"/images/page_not_found.png"}
                                            text={t('pageNotFound')}/>
                        </Route>
                        <Route path={ERROR_404_PET}>
                            <ErrorWithImage title={t('error404')} image={"/images/pet_not_found.png"}
                                            text={t('petNotFound')}/>
                        </Route>
                        <Route path={ERROR_404_USER}>
                            <ErrorWithImage title={t('error404')} image={"/images/user_not_found.png"}
                                            text={t('userNotFound')}/>
                        </Route>
                        <Route path={ACCESS_DENIED}>
                            <ErrorWithImage title={t('error403')} image={"/images/access_denied.png"}
                                            text={t('accessDenied')}/>
                        </Route>
                        <Route path={WRONG_METHOD}>
                            <ErrorWithImage title={t('error405')} image={"/images/access_denied.png"}
                                            text={t('wrongMethod')}/>
                        </Route>
                        <Route path={BAD_REQUEST}>
                            <ErrorWithImage title={t('error400')} image={"/images/access_denied.png"}
                                            text={t('badRequest')}/>
                        </Route>
                        <Route path={INTERNAL_SERVER_ERROR}>
                            <ErrorWithImage title={t('error500')}
                                            image={"/images/internal_server_error.png"}
                                            text={t('serverError')}/>
                        </Route>
                        <Route path={'/index.html'}>
                            <Redirect to={HOME}/>
                        </Route>
                        <Redirect to={ERROR_404}/>

                    </Switch>
                </BasicLayout>
            </Route>
        </Switch>

    </FilterAndSearchContext.Provider>
}


function App() {
    const login = useLoginState();
    const constants = useConstants();

    return (
        <LoginContext.Provider value={login}>
            <ConstantsContext.Provider value={constants}>
                <Router basename={CONTEXT}>
                    <ScrollToTop/>
                    <AppSwitch/>
                </Router>
            </ConstantsContext.Provider>
        </LoginContext.Provider>
    );
}

export default App;
