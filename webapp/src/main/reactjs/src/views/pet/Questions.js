import React, {useState, useEffect} from 'react';
import {Formik} from "formik";
import * as Yup from 'yup';
import {Form, Input} from "formik-antd";
import {useTranslation} from "react-i18next";
import {List, Button, Spin} from "antd";
import {
    CREATE_ANSWER_ERRORS,
    CREATE_QUESTION_ERRORS,
    createAnswer as createAnswerApi,
    createQuestion as createQuestionApi,
    GET_QUESTIONS_ERRORS,
    getQuestions
} from '../../api/questions';
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';
import {ERROR_404_PET} from "../../constants/routes";
import _ from 'lodash';

const FormItem = Form.Item;
const ListItem = List.Item;

const {TextArea} = Input;

function AnswerForm({onSubmit, submitting}){
    const {t} = useTranslation('petView');

    return <Formik
        initialValues={
            {answer: ''}
        }
        onSubmit={onSubmit}
        validationSchema={
            Yup.object().shape({
                answer: Yup.string()
                    .min(1, min => t('answers.errors.min', {min}))
                    .max(250, max => t('answers.errors.max', {max}))
                    .required(t('answers.errors.required'))
            })
        }
        validateOnBlur={false}
    >
        <Form layout={'inline'}>
            <FormItem name={"answer"}>
                <Input name={"answer"} placeholder={t('answers.answer')}/>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('answers.answerButton')}
                </Button>
            </FormItem>
        </Form>
    </Formik>;
}

function QARow({question, isOwner, createAnswer}){
    const [submitting, setSubmitting] = useState(false);
    const {t} = useTranslation('petView');

    const onCreateAnswer = async ({answer}) => {
        setSubmitting(true);
        await createAnswer(question.id, answer);
        setSubmitting(false);
    };

    return <ListItem>
        <div>
            <p>{question.username}: {question.content}</p>
            {
                question.answerContent ?
                    <p>{t('questions.owner')}: {question.answerContent}</p>
                    :

                    isOwner ?
                        <AnswerForm onSubmit={onCreateAnswer} submitting={submitting}/>
                        :
                        <p>{t('questions.noAnswerYet')}</p>

            }
        </div>
    </ListItem>;
}

function QuestionList({petId, questions, maxPages, currentPage, fetching, onFetchMoreClick, isOwner, createAnswer}){
    const {t} = useTranslation('petView');

    return <>
        {
            questions && (questions.length > 0 ?
                <List bordered={true}>
                    {questions.map(question => (<QARow key={question.id} question={question} isOwner={isOwner} createAnswer={createAnswer}/>))}
                </List>
                :
                <p>{t('questions.noQuestionsYet')}</p>)
        }

        {
            (questions === null || fetching) && <Spin/>
        }
        {
            (_.isNil(maxPages) || currentPage !== maxPages) &&
                <Button onClick={onFetchMoreClick}>{t('questions.loadMore')}</Button>
        }

    </>
}

function QuestionsForm({onSubmit, submitting}){
    const {t} = useTranslation('petView');

    return <Formik
        initialValues={
            {question: ''}
        }
        onSubmit={onSubmit}
        validationSchema={
            Yup.object().shape({
                question: Yup.string()
                    .min(1, min => t('questions.errors.min', {min}))
                    .max(250, max => t('questions.errors.max', {max}))
                    .required(t('questions.errors.required'))
            })
        }
        validateOnChange={false}
        validateOnBlur={false}
    >
        <Form>
            <FormItem name={"question"}>
                <TextArea name={"question"} rows={4} placeholder={t('questions.askSomething')}/>
            </FormItem>

            <FormItem name>
                <Button type="primary" htmlType="submit" loading={submitting}>
                    {t('questions.askButton')}
                </Button>
            </FormItem>
        </Form>
    </Formik>;
}

function Questions({petId, ownerId}){
    const history = useHistory();
    const {state, promptLogin} = useLogin();
    const [submitting, setSubmitting] = useState(false);

    const [questions, setQuestions] = useState([]);
    const [maxPages, setMaxPages] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [fetching, setFetching] = useState(false);

    const {jwt, id: loggedUserId} = state;

    const isOwner = loggedUserId === ownerId;

    const createQuestion = async values => {
        setSubmitting(true);
        try{
            await createQuestionApi({content: values.question, petId}, jwt);
        }catch (e) {
            switch (e) {
                case CREATE_QUESTION_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_PET);
                    break;
                case CREATE_QUESTION_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case CREATE_QUESTION_ERRORS.CONN_ERROR:
                default:
                    //TODO. con error
            }
            return;
        }
        setSubmitting(false);

        await refresh();
    };

    const fetchQuestions = async (page, currentQuestions) => {
        setFetching(true);
        try {
            const {pages, questions: newQuestions} = await getQuestions({petId, page}, jwt);

            setQuestions([...currentQuestions, ...newQuestions]);

            setMaxPages(pages);
        }catch (e) {
            switch (e) {
                case GET_QUESTIONS_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_PET);
                    break;
                case GET_QUESTIONS_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case GET_QUESTIONS_ERRORS.CONN_ERROR:
                default:
                //TODO. conn error
            }
        }
        setFetching(false);
    };

    useEffect(()=>{
        fetchQuestions(1, []);
    }, []);


    const onFetchMoreClick = async () => {
        setCurrentPage(currentPage + 1);

        await fetchQuestions(currentPage + 1, questions);
    };

    const createAnswer = async (id, content) => {
        try {
            await createAnswerApi({content, questionId: id}, jwt);
        }catch (e) {
            switch (e) {
                case CREATE_ANSWER_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case CREATE_ANSWER_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_PET);
                    break;
                case CREATE_ANSWER_ERRORS.CONN_ERROR:
                default:
                    //TODO: con error
            }
            return;
        }

        await refresh();
    };

    const refresh = async () => {
        setFetching(true);
        try {
            const result = await Promise.all(
                _.times(currentPage, p => {
                    return getQuestions({petId, page: p + 1}, jwt)
                })
            );

            const {pages} = result[0];

            const updatedQuestions = result
                .map(({questions}) => questions)
                .reduce((acc, val) => [...acc, ...val], []);

            setQuestions(updatedQuestions);

            setMaxPages(pages);
        }catch (e) {
            switch (e) {
                case GET_QUESTIONS_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_PET);
                    break;
                case GET_QUESTIONS_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case GET_QUESTIONS_ERRORS.CONN_ERROR:
                default:
                //TODO. conn error
            }
        }
        setFetching(false);
    };

    return <>
        {
            !isOwner && <QuestionsForm onSubmit={createQuestion} submitting={submitting}/>
        }

        <QuestionList
            petId={petId}
            questions={questions}
            fetching={fetching}
            onFetchMoreClick={onFetchMoreClick}
            maxPages={maxPages}
            currentPage={currentPage}
            isOwner={isOwner}
            createAnswer={createAnswer}
        />
    </>;
}

export default Questions;