import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";

const GET_ADMIN_REQUESTS_ENDPOINT = "/admin/requests";

export const GET_REQUESTS_ERROR = {
    CON_ERROR: 0
};

export async function getAdminRequests(
    {page, userId, petId, status, searchCriteria, searchOrder}, jwt
) {
    try {
        const config = Object.assign(getAuthConfig(jwt),{
            params:{
                page, userId, petId, status, searchCriteria, searchOrder
            }
        });
        const response = axios.get(SERVER_URL + GET_ADMIN_REQUESTS_ENDPOINT, config);
        console.log(response)
        const {list, pages, amount, pagesize} = response.data;
        console.log(list)

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['userId', 'petId', 'username', 'petName', 'creationDate',
                    'id', 'status', 'updateDate'])
            })
        }
    } catch(e){
        throw GET_REQUESTS_ERROR.CON_ERROR;
    }
}