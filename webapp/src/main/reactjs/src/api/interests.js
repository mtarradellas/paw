import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";

const GET_INTERESTS_ENDPOINT = "/requests";
const ACCEPT_INTEREST_ENDPOINT = (id) => "/requests/" + id + "/accept";
const REJECT_INTEREST_ENDPOINT = (id) => "/requests/" + id + "/reject";
const INTERESTS_FILTERS_ENDPOINT = "/requests/interests/filters";


export const GET_INTERESTS_ERRORS = {
    CONN_ERROR: 0
};
export const ACCEPT_INTEREST_ERRORS = {
    CONN_ERROR: 0
};
export const REJECT_INTEREST_ERRORS = {
    CONN_ERROR: 0
};
export const FILTER_INTERESTS_ERRORS = {
    CONN_ERROR: 0
};

export async function getInterests(
    {page, targetId, petId, status, searchCriteria, searchOrder}, jwt
) {
    try{
        const config = Object.assign(getAuthConfig(jwt),{
            params: {
                page, targetId, petId, status, searchCriteria, searchOrder
            }
        });

        const response = await axios.get(SERVER_URL + GET_INTERESTS_ENDPOINT, config);
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
    }catch (e) {
        throw GET_INTERESTS_ERRORS.CONN_ERROR;

    }
}

export async function acceptInterest(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + ACCEPT_INTEREST_ENDPOINT(id), {}, config);

        return response.data;
    }catch (e) {
        throw ACCEPT_INTEREST_ERRORS.CONN_ERROR
    }
}

export async function rejectInterest(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL + REJECT_INTEREST_ENDPOINT(id), {}, config);

        return response.data;
    }catch (e) {
        throw REJECT_INTEREST_ERRORS.CONN_ERROR
    }
}

export async function getInterestsFilters({petId, status},jwt){
    try{
        const config = Object.assign(getAuthConfig(jwt),{
            params: {petId, status}
        });
        const response = await axios.get(SERVER_URL + INTERESTS_FILTERS_ENDPOINT, config);
        return response.data;
    }catch (e) {
        throw FILTER_INTERESTS_ERRORS.CONN_ERROR;
    }
}
