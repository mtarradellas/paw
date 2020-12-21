import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";
import {GET_USER_ERRORS} from "../users";

const GET_USERS_ENDPOINT = "/admin/users";
const RECOVER_USER_ENDPOINT = (id) => "/admin/users/recover/" + id;
const DELETE_USER_ENDPOINT = (id) => "/admin/users/" + id;
const GET_USER_FILTERS_ENDPOINT = "/admin/users/filters";
const EDIT_USERNAME_ENDPOINT = (id) => "/admin/users/" + id + "/edit/username"
const EDIT_PASSWORD_ENDPOINT = (id) => "/admin/users/" + id + "/edit/password"


export const GET_USERS_ERRORS = {
    CONN_ERROR: 0,
    NOT_FOUND: 1,
    FORBIDDEN: 2,
    DUPLICATE: 3
};
export const DELETE_USER_ERRORS = {
    CON_ERROR:0
};
export const RECOVER_USER_ERRORS = {
    CON_ERROR:0
};
export const GET_FILTERS_ERRORS = {
    CON_ERROR:0
};

export async function getUserAdmin(userId, jwt){
    const config = getAuthConfig(jwt);

    try{
        const response = await axios.get(SERVER_URL + GET_USERS_ENDPOINT+"/"+userId,config);
        return _.pick(response.data, ['id', 'status', 'username','mail']);
    }catch (e) {
        if(e.response.status === 403) throw GET_USER_ERRORS.FORBIDDEN;

        throw GET_USER_ERRORS.CONN_ERROR;
    }
}

export async function getAdminUsers(
    {page, status, find, searchCriteria, searchOrder}, jwt
){
    try{
        const config = Object.assign(getAuthConfig(jwt), {
            params: {
                page,status,find,searchCriteria,searchOrder
            }
        });
        const response = await axios.get(SERVER_URL + GET_USERS_ENDPOINT, config);
        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['username', 'id', 'status'])
            })
        }
    }catch (e){
        throw GET_USERS_ERRORS.CON_ERROR;
    }

}

export async function deleteUserAdmin(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.delete(SERVER_URL + DELETE_USER_ENDPOINT(id), config);
        return response.data
    }catch (e){
        throw DELETE_USER_ERRORS.CON_ERROR;
    }
}

export async function recoverUserAdmin(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + RECOVER_USER_ENDPOINT(id),{}, config);
        return response.data
    }catch (e){
        throw RECOVER_USER_ERRORS.CON_ERROR;
    }
}

export async function getAdminUsersFilters(jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.get(SERVER_URL + GET_USER_FILTERS_ENDPOINT, config);
        const {statusList} = response.data;

        return statusList;
    }catch (e){
        throw GET_FILTERS_ERRORS.CON_ERROR
    }
}

export async function createUserAdmin(username, mail, password, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + GET_USERS_ENDPOINT,{
            username:username, password:password, mail:mail
        },config);
        return response.data;

    }catch (e) {
        if(e.response.status === 400){
            throw GET_USERS_ERRORS.DUPLICATE
        }
        throw GET_USERS_ERRORS.CONN_ERROR
    }
}

export async function editUsernameAdmin(username, userId, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + EDIT_USERNAME_ENDPOINT(userId),{
            username:username
        },config);
        return response.data;

    }catch (e) {
        if(e.response.status === 400){
            throw GET_USERS_ERRORS.DUPLICATE
        }
        throw GET_USERS_ERRORS.CONN_ERROR
    }
}

export async function editPasswordAdmin(oldPassword, newPassword, userId,jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + EDIT_PASSWORD_ENDPOINT(userId),{
            oldPassword:oldPassword, newPassword:newPassword
        },config);
        return response.data;

    }catch (e) {
        if(e.response.status === 400){
            throw GET_USERS_ERRORS.DUPLICATE
        }
        throw GET_USERS_ERRORS.CONN_ERROR
    }
}