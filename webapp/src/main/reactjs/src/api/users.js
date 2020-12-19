import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";


const GET_USER_ENDPOINT = "/users/";
export const GET_USER_ERRORS = {
    CONN_ERROR: 0,
    NOT_FOUND: 1,
    FORBIDDEN: 2
};
export async function getUser(userId, jwt) {
    const config = getAuthConfig(jwt);

    try {
        const response = await axios.get(SERVER_URL + GET_USER_ENDPOINT + userId, config);

        return _.pick(response.data, ['id', 'status', 'username']);
    }catch (e) {
        if(e.response.status === 403) throw GET_USER_ERRORS.FORBIDDEN;

        throw GET_USER_ERRORS.CONN_ERROR;
    }
}

const GET_LOGGED_USER_ENDPOINT = "/users/logged-user";
export const GET_LOGGED_USER_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 2
};
export async function getLoggedUser(jwt){
    const config = getAuthConfig(jwt);

    try{
        const response = await axios.get(SERVER_URL + GET_LOGGED_USER_ENDPOINT, config);

        return _.pick(response.data, ['id', 'mail', 'status', 'isAdmin']);
    }catch (e) {
        if(e.response.status === 403) throw GET_LOGGED_USER_ERRORS.FORBIDDEN;

        throw GET_LOGGED_USER_ERRORS.CONN_ERROR;
    }
}


const GET_MAIL_ENDPOINT = (id) => "/users/" + id + "/mail";
export const GET_MAIL_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 2,
    NOT_ALLOWED: 3
};
export async function getMail({userId}, jwt){
    const config = getAuthConfig(jwt);

    try{
        const response = await axios.get(SERVER_URL + GET_MAIL_ENDPOINT(userId), config);

        return response.data.mail;
    }catch (e) {
        if(e.response.status === 403){
            const code = _.get(e, 'response.data.code', null);

            if(!_.isNil(code) && code === 1)
                throw GET_MAIL_ERRORS.NOT_ALLOWED;

            throw GET_MAIL_ERRORS.FORBIDDEN;
        }

        throw GET_MAIL_ERRORS.CONN_ERROR;
    }
}