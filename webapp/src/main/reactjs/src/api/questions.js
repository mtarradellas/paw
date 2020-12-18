import axios from "axios";
import {SERVER_URL} from "../config";
import _ from 'lodash';
import {getAuthConfig} from "./utils";


const GET_QUESTIONS_ENDPOINT = '/questions';
export const GET_QUESTIONS_ERRORS = {
    CONN_ERROR: 0,
    FORBIDDEN: 1,
    NOT_FOUND: 2
};
export async function getQuestions ({petId, page}, jwt){
    const config = Object.assign(
        getAuthConfig(jwt),
        {
            params: {
                page, petId
            }
        }
    );

    try{
        const response = await axios.get(SERVER_URL + GET_QUESTIONS_ENDPOINT, config);

        const {pages, list} = response.data;

        return {
            pages,
            questions: list.map(
                q => _.pick(q, ['content', 'userId', 'username', 'answerContent', 'id'])
            )
        }

    }catch (e) {
        if(e.response.status === 403) throw GET_QUESTIONS_ERRORS.FORBIDDEN;

        if(e.response.status === 404) throw GET_QUESTIONS_ERRORS.NOT_FOUND;

        throw GET_QUESTIONS_ERRORS.CONN_ERROR;
    }
}