import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";


/**
 *
 * @type {object}
 *
 * {
 *     id, status, username
 * }
 */
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