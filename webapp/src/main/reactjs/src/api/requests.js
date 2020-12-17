import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";

const GET_REQUESTS_ENDPOINT = "/requests";
const CANCEL_REQUEST_ENDPOINT = (id) => "/requests/" + id + "/cancel";
const RECOVER_REQUEST_ENDPOINT = (id) => "/requests/" + id + "/recover";
const FILTER_REQUEST_ENDPOINT = "/requests/filters";


export const GET_REQUESTS_ERRORS = {
    CONN_ERROR: 0
};
export const CANCEL_REQUEST_ERRORS = {
    CONN_ERROR: 0
};
export const RECOVER_REQUEST_ERRORS = {
    CONN_ERROR: 0
};
export const FILTER_REQUEST_ERRORS = {
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

export async function getRequestsFilter(jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.get(SERVER_URL + FILTER_REQUEST_ENDPOINT, config);
        const {statusList} = response.data;

        return statusList;
    }catch (e) {
        throw FILTER_REQUEST_ERRORS.CONN_ERROR;
    }
}
