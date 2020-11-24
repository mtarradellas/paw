import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import 'antd/dist/antd.css';
import './css/html.css';

import Home from "./views/home/Home";
import BasicLayout from "./components/BasicLayout";

import LoginContext from './constants/loginContext';

import {HOME} from "./constants/routes";
import useLogin from "./hooks/useLogin";

function App() {
    const login = useLogin();

    return (
        <LoginContext.Provider value={login}>
            <Router>
                <Switch>
                    <Route path={HOME}>
                        <BasicLayout>
                            <Home/>
                        </BasicLayout>
                    </Route>
                </Switch>
            </Router>
        </LoginContext.Provider>
    );
}

export default App;
