import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";

const GET_PETS_ENDPOINT = "/admin/pets";
const REMOVE_PET_ENDPOINT = (id) => "/admin/pets/"+id+"/remove";
const RECOVER_PET_ENDPOINT = (id) => "/admin/pets/"+id+"/recover"

export const GET_PETS_ERRORS = {
    CONN_ERROR: 0
};
export const REMOVE_PETS_ERRORS = {
    CONN_ERROR: 0
};
export const RECOVER_PETS_ERRORS = {
    CONN_ERROR: 0
};

export async function getAdminPets(
    {ownerId, newOwnerId, species, breed, province, department, gender, searchCriteria, find, searchOrder, priceRange,
        status, page}, jwt
){
    try{
        const config = Object.assign(getAuthConfig(jwt), {
            params:{
                ownerId, newOwnerId, species, breed, province, department, gender, searchCriteria, find, searchOrder,
                priceRange, status, page
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
        throw GET_PETS_ENDPOINT;
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