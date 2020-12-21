import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";
import {CREATE_PET_ERRORS} from "../pets";

const ADMIN_REQUESTS_ENDPOINT = "/admin/requests";
const EDIT_ADMIN_REQUESTS_ENDPOINT = (id) => "/admin/requests/"+id+"/edit"
const GET_ADMIN_REQUESTS_FILTERS_ENDPOINT = "/admin/requests/filters";

export const GET_REQUESTS_ERROR = {
    CON_ERROR: 0
};
export const EDIT_REQUESTS_ERROR = {
    CON_ERROR: 0
};
export const GET_REQUESTS_FILTERS_ERROR = {
    CON_ERROR: 0
};
export const CREATE_REQUEST_ERRORS = {
    CON_ERROR: 0,
    DUPLICATE: 1
};

export async function getRequestAdmin(id,jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.get(SERVER_URL + ADMIN_REQUESTS_ENDPOINT + "/" + id, config);
        return response.data;
    }catch (e){
        throw GET_REQUESTS_ERROR.CON_ERROR;
    }
}

export async function getAdminRequests(
    {page, userId, petId, status, searchCriteria, searchOrder}, jwt
) {
    try {
        const config = Object.assign(getAuthConfig(jwt),{
            params:{
                page, userId, petId, status, searchCriteria, searchOrder
            }
        });
        const response = await axios.get(SERVER_URL + ADMIN_REQUESTS_ENDPOINT, config);
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
    } catch(e){
        throw GET_REQUESTS_ERROR.CON_ERROR;
    }
}

export async function editAdminRequest(status, id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + EDIT_ADMIN_REQUESTS_ENDPOINT(id), {status: status} , config);
        return response.data

    }catch (e){
        throw EDIT_REQUESTS_ERROR.CON_ERROR;
    }
}

export async function getAdminRequestsFilters(jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.get(SERVER_URL + GET_ADMIN_REQUESTS_FILTERS_ENDPOINT,config);
        const {statusList} = response.data;

        return statusList;
    }catch (e){
        throw GET_REQUESTS_FILTERS_ERROR.CON_ERROR;
    }
}

export async function createRequestAdmin(userId, petId, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + ADMIN_REQUESTS_ENDPOINT,{
            userId:userId, petId:petId
        },config);
        return response.data;
    }catch (e) {
        if(e.response.status === 400){
            throw CREATE_REQUEST_ERRORS.DUPLICATE
        }
        throw CREATE_REQUEST_ERRORS.CON_ERROR

    }
}