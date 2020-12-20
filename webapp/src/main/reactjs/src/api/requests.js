import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";

const GET_REQUESTS_ENDPOINT = "/requests";
const CANCEL_REQUEST_ENDPOINT = (id) => "/requests/" + id + "/cancel";
const RECOVER_REQUEST_ENDPOINT = (id) => "/requests/" + id + "/recover";
const REQUESTS_FILTER_ENDPOINT = "/requests/filters";


export const GET_REQUESTS_ERRORS = {
    CONN_ERROR: 0
};
export const CANCEL_REQUEST_ERRORS = {
    CONN_ERROR: 0
};
export const RECOVER_REQUEST_ERRORS = {
    CONN_ERROR: 0
};
export const FILTER_REQUESTS_ERRORS = {
    CONN_ERROR: 0
};

export async function getRequests(
    {page, userId, petId, status, searchCriteria, searchOrder}, jwt
) {
    try {
        const config = Object.assign(getAuthConfig(jwt), {
            params: {
                page, userId, petId, status, searchCriteria, searchOrder
            }
        });
        const response = await axios.get(SERVER_URL + GET_REQUESTS_ENDPOINT, config);
        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['userId', 'petId', 'username', 'petName', 'creationDate',
                        'id', 'status', 'updateDate'])
            })
        }
    } catch (e) {
        throw GET_REQUESTS_ERRORS.CONN_ERROR;
    }
}

export async function cancelRequest(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + CANCEL_REQUEST_ENDPOINT(id), {} , config);

        return response.data
    } catch (e){
        throw CANCEL_REQUEST_ERRORS.CONN_ERROR;
    }
}

export async function recoverRequest( id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + RECOVER_REQUEST_ENDPOINT(id), {} , config);

        return response.data
    } catch (e){
        throw RECOVER_REQUEST_ERRORS.CONN_ERROR;
    }
}

export async function getRequestsFilters(jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.get(SERVER_URL + REQUESTS_FILTER_ENDPOINT, config);
        const {statusList} = response.data;

        return statusList;
    }catch (e) {
        throw FILTER_REQUESTS_ERRORS.CONN_ERROR;
    }
}


const GET_NOTIFICATIONS_ENDPOINT = '/requests/notifications';
export const GET_NOTIFICATIONS_ERRORS = {
    CONN_ERROR: 0
};
export async function getNotifications(jwt){
    const config = getAuthConfig(jwt);

    try {
        const response = await axios.get(SERVER_URL + GET_NOTIFICATIONS_ENDPOINT, config);

        return _.pick(response.data, ['interests', 'requests']);
    }catch (e) {
        throw GET_NOTIFICATIONS_ERRORS.CONN_ERROR;
    }
}