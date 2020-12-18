import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";

const GET_PETS_ENDPOINT = "/pets";
export const GET_PETS_ERRORS = {
    CONN_ERROR: 0
};
export async function getPets(
    {
        ownerId, species, breed, province, department, gender,
        searchCriteria, find, searchOrder, priceRange, page, newOwnerId
    }
    ) {
    try {
        const response = await axios.get(SERVER_URL + GET_PETS_ENDPOINT,
            {
                params: {
                    ownerId, species, breed, province, department, gender,
                    searchCriteria, find, searchOrder, priceRange, page, newOwnerId
                }
            }
            );

        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(pet => {
                return  _.pick(pet, [
                    'breedId', 'departmentId', 'description', 'id', 'petName', 'price', 'provinceId', 'speciesId',
                    'status', 'uploadDate', 'userId', 'images', 'username', 'gender'
                ])})
        };

    }catch (e) {
        throw GET_PETS_ERRORS.CONN_ERROR;
    }
}

const CREATE_PET_ENDPOINT = "/pets";
export const CREATE_PET_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1
};
export async function createPet(
    {

    }, jwt
    ){
    const config = getAuthConfig(jwt);

    try{
        const response = await axios.post(SERVER_URL + CREATE_PET_ENDPOINT, {

        }, config);
    }catch (e) {
        if(e.response.status === 403) throw CREATE_PET_ERRORS.FORBIDDEN;

        throw CREATE_PET_ERRORS.CONN_ERROR;
    }
}

const PET_FILTERS_ENDPOINT = "/pets/filters";
export const PET_FILTERS_ERRORS = {
    CONN_ERROR: 0
};
export async function petFilters(
    {ownerId, species, breed, province, department, gender, find, priceRange}
) {
    try {
        const response = await axios.get(SERVER_URL + PET_FILTERS_ENDPOINT,
            {
                params: {ownerId, species, breed, province, department, gender, find, priceRange}
            });

        return _.pick(response.data, ['speciesList', 'breedList', 'departmentList', 'provinceList', 'genderList', 'rangeList']);
    } catch (e) {
        throw PET_FILTERS_ERRORS.CONN_ERROR;
    }
}





