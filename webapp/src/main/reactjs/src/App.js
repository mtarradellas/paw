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

import LoginContext from './constants/loginContext';

import {HOME, REGISTER, USER} from "./constants/routes";
import useLogin from "./hooks/useLogin";
import UserView from "./views/user/UserView";
import RegisterView from "./views/register/RegisterView";

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
                    <Route exact path={USER}
                        render={
                            ({id}) => (
                                <BasicLayout>
                                    <UserView id={id}/>
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
