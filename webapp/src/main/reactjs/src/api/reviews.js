import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {GET_LOGGED_USER_ERRORS} from "./users";
import {getAuthConfig} from "./utils";

const GET_REVIEWS_ENDPOINT = "/reviews";
export const GET_REVIEWS_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1
};
export async function getReviews(
    {
        page, userId, targetId, minScore, maxScore, status, searchCriteria, searchOrder
    }, jwt
) {
    const config = Object.assign(getAuthConfig(jwt),
        {
            params: {
                page, userId, targetId, minScore, maxScore, status, searchCriteria, searchOrder
            }
        });

    try {
        const response = await axios.get(SERVER_URL + GET_REVIEWS_ENDPOINT, config);

        const {list, pages, amount, pagesize, average} = response.data;

        return {
            pages,
            amount,
            pageSize: pagesize,
            average,
            list: list.map(review => {
                return  _.pick(review, [
                    'id', 'score', 'description', 'creationDate', 'status', 'username', 'userId'
                ])})
        };

    }catch (e) {
        if(e.response.status === 403) throw GET_LOGGED_USER_ERRORS.FORBIDDEN;

        throw GET_REVIEWS_ERRORS.CONN_ERROR;
    }
}