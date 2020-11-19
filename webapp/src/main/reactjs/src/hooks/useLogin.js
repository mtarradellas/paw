import {useReducer} from 'react';

const ACTIONS = {
    LOGOUT: "logout",
    LOGIN: "login"
}

const initialState = {
    isLoggedIn: false,
    username: "Fastiz",
    jwt: null
}

function reducer(state, action){
    switch (action.type){
        case ACTIONS.LOGIN: {
            const {username, jwt} = action;
            return Object.assign({}, state, {username, jwt, isLoggedIn: true});
        }
        case ACTIONS.LOGOUT: {
            return initialState;
        }
        default:
            return state;
    }
}

const useLogin = () => {
    const [state, dispatch] = useReducer(reducer, initialState);

    const login = ({username, jwt})=>{
        dispatch({action: ACTIONS.LOGIN, username, jwt});
    }

    const logout = () => {
        dispatch({action: ACTIONS.LOGOUT});
    }

    return {state, login, logout};
}

export default useLogin;