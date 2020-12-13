import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';


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
    NOT_FOUND: 1
};
export async function getUser(userId, jwt) {
    const config = {
        headers: {
            authorization: "Bearer " + jwt
        }
    };

    try {
        const response = await axios.get(SERVER_URL + GET_USER_ENDPOINT + userId, config);

        return _.pick(response.data, ['id', 'status', 'username']);
    }catch (e) {
        throw GET_USER_ERRORS.CONN_ERROR;
    }
}