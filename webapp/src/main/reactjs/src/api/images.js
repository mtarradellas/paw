import {SERVER_URL} from "../config";


const PETS_IMAGE_ENDPOINT = '/pets/images/';
export const petImageSrc = id => {
    return SERVER_URL + PETS_IMAGE_ENDPOINT + id;
};