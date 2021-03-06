import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";
import { GET_PET_ERRORS} from "../pets";

const GET_PETS_ENDPOINT = "/admin/pets";
const REMOVE_PET_ENDPOINT = (id) => "/admin/pets/"+id+"/remove";
const RECOVER_PET_ENDPOINT = (id) => "/admin/pets/"+id+"/recover";
const GET_PETS_FILTERS_ENDPOINT = "/admin/pets/filters";
const CREATE_PET_ENDPOINT = "/admin/pets";
const EDIT_PET_ENDPOINT = id => "/admin/pets/" + id + "/edit";


export const CREATE_PET_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1
};
export const GET_PETS_ERRORS = {
    CONN_ERROR: 0
};
export const REMOVE_PETS_ERRORS = {
    CONN_ERROR: 0
};
export const RECOVER_PETS_ERRORS = {
    CONN_ERROR: 0
};
export const GET_PETS_FILTERS = {
    CONN_ERROR:0
}
export const EDIT_PET_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1,
    IMAGE_QUANTITY_ERROR: 2
};
export async function editPetAdmin(values, id, jwt) {
    const {
        petName, birthDate, gender, isVaccinated, price, uploadDate, description,
        speciesId, breedId, provinceId, departmentId, files
    } = values;

    const form = new FormData();

    Object.keys(_.omit(values, ['files'])).forEach(key => {
        form.append(key, values[key]);
    });
    form.append('pet', id);

    files.forEach((file, i) => {
        form.append('files', file.originFileObj);
    });

    const {headers: authHeaders} = getAuthConfig(jwt);

    const config = {
        headers: Object.assign(authHeaders, {
            'accept': 'application/json',
            'Content-Type': `multipart/form-data; boundary=${form._boundary}`
        })
    };

    try{
        await axios.post(SERVER_URL + EDIT_PET_ENDPOINT(id), form, config);
    }catch (e) {
        console.log(e);
        if(e.response.status === 403) throw EDIT_PET_ERRORS.FORBIDDEN;
        if(e.response.status === 403) throw EDIT_PET_ERRORS.FORBIDDEN;

        throw EDIT_PET_ERRORS.CONN_ERROR;
    }
}
export async function createAdminPet(values, jwt){
    const {
        petName, birthDate, gender, isVaccinated, price, uploadDate, description,
        speciesId, breedId, provinceId, departmentId, user, files
    } = values;

    const form = new FormData();

    Object.keys(_.omit(values, ['files'])).forEach(key => {
        form.append(key, values[key]);
    });

    files.forEach((file, i) => {
        form.append('files', file.originFileObj);
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
        return response.data;
    }catch (e) {
        if(e.response.status === 403) throw CREATE_PET_ERRORS.FORBIDDEN;

        throw CREATE_PET_ERRORS.CONN_ERROR;
    }

}


export async function getPetAdmin(petId, jwt){
    const config = getAuthConfig(jwt);

    try{
        const response = await axios.get(SERVER_URL+GET_PETS_ENDPOINT+"/"+petId, config);
        return _.pick(response.data,
            [
                'petName', 'birthDate', 'gender', 'vaccinated', 'price', 'uploadDate', 'description',
                'status', 'username', 'userId', 'speciesId', 'breedId', 'provinceId', 'departmentId', 'images'
            ]
        );
    } catch (e){
        throw GET_PET_ERRORS.CONN_ERROR;
    }
}

export async function getAdminPets(
    {ownerId, newOwnerId, species, breed, province, department, gender, searchCriteria, find, searchOrder, priceRange,
        status, page}, jwt
){
    try{
        if(gender === "any"){
            gender = null
        }
        const config = Object.assign(getAuthConfig(jwt), {
            params:{
                species, breed, gender, searchCriteria, find, searchOrder, status, page
            }
        });
        const response = await axios.get(SERVER_URL + GET_PETS_ENDPOINT, config);
        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['id','petName','userId','status'])
            })
        }
    }catch (e) {
        throw GET_PETS_ERRORS.CONN_ERROR;
    }
}

export async function removePetAdmin(id, jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL+REMOVE_PET_ENDPOINT(id),{},config);
        return response.data;
    }catch (e){
        throw REMOVE_PETS_ERRORS.CONN_ERROR;
    }
}

export async function recoverPetAdmin(id,jwt){
    try{
        const config = getAuthConfig(jwt);
        const response = await axios.post(SERVER_URL+RECOVER_PET_ENDPOINT(id),{},config);
        return response.data;

    }catch (e) {
        throw RECOVER_PETS_ERRORS.CONN_ERROR
    }
}

export async function getAdminPetsFilters({species, breed, gender},jwt){
    try{
        if(gender === "any"){
            gender = null
        }
        const config = Object.assign(getAuthConfig(jwt), {
            params:{
                species, breed, gender
            }
        });
        const response = await axios.get(SERVER_URL + GET_PETS_FILTERS_ENDPOINT, config);
        const {breedList, departmentList, genderList, provinceList, rangeList, speciesList, statusList} = response.data;

        return {breedList, departmentList, genderList, provinceList, rangeList, speciesList,statusList};
    }catch (e) {
        throw GET_PETS_FILTERS.CONN_ERROR;
    }
}