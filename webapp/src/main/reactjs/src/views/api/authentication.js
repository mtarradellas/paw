import axios from 'axios';
import {SERVER_URL} from '../../config';
const qs = require('querystring')


const REGISTER_ENDPOINT = '/register';
export const REGISTER_ERRORS = {
    DUPLICATE_USERNAME: 0,
    DUPLICATE_EMAIL: 1,
    CONN_ERROR: 2
};
export async function register({username, password, email}){
    try {
        await axios.post(SERVER_URL + REGISTER_ENDPOINT, {username, password, mail: email});
    }catch (e) {
        //TODO: error handling
        throw REGISTER_ERRORS.CONN_ERROR;
    }
}

const LOGIN_ENDPOINT = '/login';
export const LOGIN_ERRORS = {
    INVALID_USERNAME_OR_PASSWORD: 0,
    CONN_ERROR: 1
};
export async function login({username, password}){
    const config = {
        headers: {
            'content-type': 'application/x-www-form-urlencoded'
        }
    };

    try {
        const response = await axios.post(SERVER_URL + LOGIN_ENDPOINT, qs.stringify({username, password}), config);
        console.log(response);
    }catch (e) {
        //TODO: error handling
        throw LOGIN_ERRORS.CONN_ERROR;
    }
}