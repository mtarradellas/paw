import {useReducer} from 'react';

const ACTIONS = {
    LOGOUT: "LOGOUT",
    LOGIN: "LOGIN",
    PROMPT_LOGIN: "PROMPT_LOGIN"
};

const initialState = {
    isLoggedIn: false,
    username: null,
    jwt: null,
    promptLogin: {index: null, path: null}
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

    return {state, login, logout, promptLogin};
};

export default useLoginState;