import axios from "axios";
import {SERVER_URL} from "../../config";
import _ from 'lodash';
import {getAuthConfig} from "../utils";

const GET_USERS_ENDPOINT = "/admin/users";

export const GET_USERS_ERRORS = {
    CON_ERROR:0
};

export async function getAdminUsers(
    {page, status, find, searchCriteria, searchOrder}, jwt
){
    try{
        const config = Object.assign(getAuthConfig(jwt), {
            params: {
                page,status,find,searchCriteria,searchOrder
            }
        });
        const response = await axios.get(SERVER_URL + GET_USERS_ENDPOINT, config);
        const {list, pages, amount, pagesize} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            list: list.map(request => {
                return _.pick(request, ['username', 'id', 'status'])
            })
        }
    }catch (e){
        throw GET_USERS_ERRORS.CON_ERROR;
    }

}