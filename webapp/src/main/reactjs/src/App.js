import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route
} from "react-router-dom";

import Home from "./views/home/Home";
import BasicLayout from "./components/BasicLayout";

import {HOME} from "./constants/routes";

function App() {
  return (
    <Router>
      <Switch>
        <Route path={HOME}>
            <BasicLayout>
                <Home/>
            </BasicLayout>
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
