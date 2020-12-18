import React, {useState, useEffect} from 'react';
import {Formik} from "formik";
import * as Yup from 'yup';
import {Form, Input} from "formik-antd";
import {useTranslation} from "react-i18next";
import {List, Button, Spin} from "antd";
import {GET_QUESTIONS_ERRORS, getQuestions} from '../../api/questions';
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';
import {ERROR_404_PET} from "../../constants/routes";
import _ from 'lodash';

const FormItem = Form.Item;
const ListItem = List.Item;

const {TextArea} = Input;

function QARow({question}){
    const {t} = useTranslation('petView');

    return <ListItem>
        <div>
            <p>{question.username}: {question.content}</p>
            {
                question.answerContent ?
                    <p>{t('questions.owner')}: {question.answerContent}</p>
                    :
                    <p>{t('questions.noAnswerYet')}</p>
            }
        </div>
    </ListItem>;
}

function QuestionList({petId}){
    const history = useHistory();
    const {state, promptLogin} = useLogin();
    const [questions, setQuestions] = useState([]);
    const [maxPages, setMaxPages] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [fetching, setFetching] = useState(false);
    const {t} = useTranslation('petView');

    const {jwt} = state;

    const fetchQuestions = async (page) => {
        setFetching(true);
        try {
            const {pages, questions: newQuestions} = await getQuestions({petId, page}, jwt);

            setQuestions([...questions, ...newQuestions]);

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
        fetchQuestions();
    }, []);


    const onFetchMoreClick = () => {
        setCurrentPage(currentPage + 1);

        fetchQuestions(currentPage + 1);
    };

    return <>
        <List bordered={true}>
        {
            questions && (questions.length > 0 ?
                questions.map(question => (<QARow key={question.id} question={question}/>))
                :
                <p>{t('questions.noQuestionsYet')}</p>)
        }
        </List>
        {
            (questions === null || fetching) && <Spin/>
        }
        {
            (_.isNil(maxPages) || currentPage !== maxPages) &&
                <Button onClick={onFetchMoreClick}>cargar.mas</Button>
        }

    </>
}

function QuestionsForm({onSubmit}){
    const {t} = useTranslation('petView');

    return <Formik
        initialValues={
            {question: ''}
        }
        onSubmit={onSubmit}
        validationSchema={
            Yup.object().shape({
                question: Yup.string()
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
                <Button type="primary" htmlType="submit">
                    {t('questions.askButton')}
                </Button>
            </FormItem>
        </Form>
    </Formik>;
}

function Questions({petId}){
    const _onSubmit = values => {
        console.log(values);
    };

    return <>
        <QuestionsForm onSubmit={_onSubmit}/>

        <QuestionList petId={petId}/>
    </>;
}

export default Questions;