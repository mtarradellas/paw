import axios from 'axios';
import {SERVER_URL} from '../../config';


const REGISTER_ENDPOINT = '/register';
export const REGISTER_ERRORS = {
    DUPLICATE_USERNAME: 0,
    DUPLICATE_EMAIL: 1,
    CONN_ERROR: 2
};
export async function register({username, password, email}){
    try {
        await axios.post(SERVER_URL + REGISTER_ENDPOINT, {username, password, email});
    }catch (e) {
        //TODO: error handling
        throw REGISTER_ERRORS.DUPLICATE_EMAIL;
    }
}