import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";

const GET_REQUESTS_ENDPOINT = "/requests";
export const GET_REQUESTS_ERRORS = {
    CONN_ERROR: 0
};

export async function getRequests(
    {page, userId, petId, status, searchCriteria, searchOrder}, jwt
) {
    try {
        const config = Object.assign(getAuthConfig(jwt), {
            params: {
                page, userId, petId, status, searchCriteria, searchOrder
            }
        });
        const response = await axios.get(SERVER_URL + GET_REQUESTS_ENDPOINT, config);
        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['userId', 'petId', 'username', 'petName', 'creationDate',
                        'id', 'status', 'updateDate'])
            })
        }
    } catch (e) {
        throw GET_REQUESTS_ERRORS.CONN_ERROR;
    }
}
