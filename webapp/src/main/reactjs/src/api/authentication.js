import axios from 'axios';
import {SERVER_URL} from '../config';
import _ from 'lodash';
const qs = require('querystring');


const REGISTER_ENDPOINT = '/register';
export const REGISTER_ERRORS = {
    DUPLICATE_USERNAME: 2,
    DUPLICATE_EMAIL: 3,
    CONN_ERROR: 1
};
export async function register({username, password, email}){
    try {
        await axios.post(SERVER_URL + REGISTER_ENDPOINT, {username, password, mail: email});
    }catch (e) {
        throw _.get(e, 'response.data.code', REGISTER_ERRORS.CONN_ERROR);
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

        return response.data.split(" ")[1];
    }catch (e) {
        throw e.response.status === 401 ? LOGIN_ERRORS.INVALID_USERNAME_OR_PASSWORD : LOGIN_ERRORS.CONN_ERROR;
    }
}

const FORGOT_PASSWORD_ENDPOINT = '/request-password-reset';
export const FORGOT_PASSWORD_ERRORS = {
    NON_EXISTENT_EMAIL: 2,
    CONN_ERROR: 1
};
export async function forgotPassword({email}){
    try {
        await axios.post(SERVER_URL + FORGOT_PASSWORD_ENDPOINT, {mail: email},{});
    }catch (e) {
        throw e.response.status === 404 ? FORGOT_PASSWORD_ERRORS.NON_EXISTENT_EMAIL : FORGOT_PASSWORD_ERRORS.CONN_ERROR;
    }
}

const RESET_PASSWORD_ENDPOINT = '/password-reset';
export const RESET_PASSWORD_ERRORS = {
    NON_EXISTENT_EMAIL: 2,
    CONN_ERROR: 1
};
export async function resetPassword({password}, {token}){
    try {
        await axios.post(SERVER_URL + RESET_PASSWORD_ENDPOINT, {password: password, token: token},{});
    }catch (e) {
        throw e.response.status === 400 ? FORGOT_PASSWORD_ERRORS.NON_EXISTENT_EMAIL : FORGOT_PASSWORD_ERRORS.CONN_ERROR;
    }
}