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

const CAN_REVIEW_ENDPOINT = "/reviews/can-review";
export const CAN_REVIEW_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1
};
export async function canReview({targetId}, jwt) {
    const config = Object.assign(getAuthConfig(jwt), {params:{targetId}});

    try{
        const response = await axios.get(SERVER_URL + CAN_REVIEW_ENDPOINT, config);

        return response.data.canReview;
    }catch (e) {
        if(e.response.status === 403) throw CAN_REVIEW_ERRORS.FORBIDDEN;

        throw CAN_REVIEW_ERRORS.CONN_ERROR;
    }
}


const CREATE_REVIEW_ENDPOINT = "/reviews";
export const CREATE_REVIEW_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1
};
export async function createReview({targetId, score, description}, jwt){
    const config = getAuthConfig(jwt);

    try{
        await axios.post(SERVER_URL + CREATE_REVIEW_ENDPOINT, {targetId, score, description}, config);
    }catch (e) {
        if(e.response.status === 403) throw CAN_REVIEW_ERRORS.FORBIDDEN;

        throw CAN_REVIEW_ERRORS.CONN_ERROR;
    }
}