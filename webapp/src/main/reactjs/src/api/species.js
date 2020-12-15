import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';

const GET_SPECIES_ENDPOINT = "/species/";
export const GET_SPECIES_ERRORS = {
    CONN_ERROR: 0
};
export async function getSpecies() {
    try {
        const response = await axios.get(SERVER_URL + GET_SPECIES_ENDPOINT);

        return _.keyBy(response.data, 'id');
    }catch (e) {
        throw GET_SPECIES_ERRORS.CONN_ERROR;
    }
}

const GET_BREEDS_ENDPOINT = "/species/breeds";
export const GET_BREEDS_ERRORS = {
    CONN_ERROR: 0
};
export async function getBreeds() {
    try {
        const response = await axios.get(SERVER_URL + GET_BREEDS_ENDPOINT);

        return _.keyBy(response.data, 'id');
    }catch (e) {
        throw GET_BREEDS_ERRORS.CONN_ERROR;
    }
}