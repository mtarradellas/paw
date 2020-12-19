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
export async function createPet(values, jwt) {
    const {
        petName, birthDate, gender, vaccinated, price, uploadDate, description,
        speciesId, breedId, provinceId, departmentId, files
    } = values;

    const form = new FormData();

    Object.keys(_.omit(values, ['files'])).forEach(key => {
        form.append(key, values[key]);
    });

    files.forEach((file, i) => {
        form.append('files[' + i + ']', file.originFileObj, file.name);
    });

    const {headers: authHeaders} = getAuthConfig(jwt);

    const config = {
        headers: Object.assign(authHeaders, {
            'accept': 'application/json',
            'Content-Type': `multipart/form-data; boundary=${form._boundary}`
        })
    };

    try{
        const response = await axios.post(SERVER_URL + CREATE_PET_ENDPOINT, form, config);
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


const GET_PET_ENDPOINT = id => "/pets/" + id;
export const GET_PET_ERRORS = {
    CONN_ERROR: 0
};
export async function getPet(
    {petId}
) {
    try {
        const response = await axios.get(SERVER_URL + GET_PET_ENDPOINT(petId));

        return _.pick(response.data,
            [
                'petName', 'birthDate', 'gender', 'vaccinated', 'price', 'uploadDate', 'description',
                'status', 'username', 'userId', 'speciesId', 'breedId', 'provinceId', 'departmentId', 'images'
            ]
        );
    } catch (e) {
        throw GET_PET_ERRORS.CONN_ERROR;
    }
}




