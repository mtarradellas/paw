import React, {useReducer, useEffect} from 'react';
import _ from "lodash";

const ACTIONS = {
    LOGOUT: "LOGOUT",
    LOGIN: "LOGIN",
    PROMPT_LOGIN: "PROMPT_LOGIN",
    SET_LOGGED_USER: "SET_LOGGED_USER"
};

const initialState = {
    isLoggedIn: false,
    username: null,
    jwt: null,
    promptLogin: {index: null, path: null},
    id: null,
    mail: null,
    isAdmin: null,
    status: null
};

function reducer(state, action){
    switch (action.type){
        case ACTIONS.LOGIN: {
            const {username, jwt} = action;

            return Object.assign({}, state, {username, jwt, isLoggedIn: true});
        }
        case ACTIONS.LOGOUT: {
            return initialState;
        }
        case ACTIONS.PROMPT_LOGIN: {
            const {index, path} = action;
            return Object.assign({}, state, {promptLogin: {index, path}})
        }
        case ACTIONS.SET_LOGGED_USER:
            const {id, mail, isAdmin, status} = action;
            return Object.assign({}, state, {id, mail, isAdmin, status});
        default:
            return state;
    }
}

const useLoginState = () => {
    const [state, dispatch] = useReducer(reducer, initialState);

    const login = ({username, jwt})=>{
        dispatch({type: ACTIONS.LOGIN, username, jwt});
    };

    const logout = () => {
        dispatch({type: ACTIONS.LOGOUT});
    };

    const promptLogin = ({path, index}) => {
        dispatch({
            type: ACTIONS.PROMPT_LOGIN,
            path,
            index
        });
    };

    const setUserInfo = ({id, mail, isAdmin, status}) => {
        dispatch({
            type: ACTIONS.SET_LOGGED_USER,
            id, mail, isAdmin, status
        });
    };

    useEffect(()=>{
        const auth = localStorage.getItem("LOCAL_STORAGE_AUTH_KEY");

        if(!_.isNil(auth)){
            const {username, jwt, id, mail, isAdmin, status} = JSON.parse(auth);

            login({username, jwt});

            setUserInfo({id: parseInt(id),
                mail,
                isAdmin: isAdmin === 'true',
                status: parseInt(status)})
        }
    }, []);

    return {state, login, logout, promptLogin, setUserInfo};
};

export default useLoginState;