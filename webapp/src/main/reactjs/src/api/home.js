import axios from "axios";
import {SERVER_URL} from "../config";


//TODO: esto no lo use todavía
const GET_AVAILABLE_ENDPOINT = "/";
export const GET_AVAILABLE_ERRORS = {
    CONN_ERROR: 0
};
export async function getAvailable() {
    try {
        const response = await axios.get(SERVER_URL + GET_AVAILABLE_ENDPOINT);
        console.log(response);

        return response.data;
    }catch (e) {
        throw GET_AVAILABLE_ERRORS.CONN_ERROR;
    }
}