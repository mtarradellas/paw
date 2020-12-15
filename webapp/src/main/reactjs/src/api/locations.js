import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';

const GET_PROVINCES_ENDPOINT = "/location/provinces";
export const GET_PROVINCES_ERRORS = {
    CONN_ERROR: 0
};
export async function getProvinces() {
    try {
        const response = await axios.get(SERVER_URL + GET_PROVINCES_ENDPOINT);

        return _.keyBy(response.data, 'id');;
    }catch (e) {
        throw GET_PROVINCES_ERRORS.CONN_ERROR;
    }
}

const GET_DEPARTMENTS_ENDPOINT = "/location/departments";
export const GET_DEPARTMENTS_ERRORS = {
    CONN_ERROR: 0
};
export async function getDepartments() {
    try {
        const response = await axios.get(SERVER_URL + GET_DEPARTMENTS_ENDPOINT);

        return _.keyBy(response.data, 'id');
    }catch (e) {
        throw GET_DEPARTMENTS_ERRORS.CONN_ERROR;
    }
}