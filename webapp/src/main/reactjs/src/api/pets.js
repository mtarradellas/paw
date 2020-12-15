import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';

const GET_PETS_ENDPOINT = "/pets";
export const GET_PETS_ERRORS = {
    CONN_ERROR: 0
};
export async function getPets(
    {
        ownerId, species, breed, province, department, gender,
        searchCriteria, find, searchOrder, priceRange, page
    }
    ) {
    try {
        const response = await axios.get(SERVER_URL + GET_PETS_ENDPOINT,
            {
                params: {
                    ownerId, species, breed, province, department, gender,
                    searchCriteria, find, searchOrder, priceRange, page
                }
            }
            );

        return response.data.map(pet => {
            return  _.pick(pet, [
                'breedId', 'departmentId', 'description', 'id', 'petName', 'price', 'provinceId', 'speciesId',
                'status', 'uploadDate', 'userId'
            ]);
        });
    }catch (e) {
        throw GET_PETS_ERRORS.CONN_ERROR;
    }
}