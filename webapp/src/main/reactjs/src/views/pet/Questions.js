import React from 'react';
import {Formik} from "formik";
import * as Yup from 'yup';
import {Form, Input} from "formik-antd";
import {useTranslation} from "react-i18next";
import {List, Button} from "antd";

const FormItem = Form.Item;
const ListItem = List.Item;

const {TextArea} = Input;

const questions = [
    {
        question: {
            username: 'fastiz13',
            text: 'una pregunta'
        },
        answer: null
    },
    {
        question: {
            username: 'fastiz13',
            text: 'una pregunta'
        },
        answer: {
            username: 'dueño',
            text: 'una respuesta'
        }
    },
    {
        question: {
            username: 'fastiz13',
            text: 'una pregunta'
        },
        answer: null
    }
];

function QARow({QA}){
    const {t} = useTranslation('petView');

    const {question, answer} = QA;

    return <ListItem>
        <div>
            <p>{question.username}: {question.text}</p>
            {
                answer ?
                    <p>{answer.username}: {answer.text}</p>
                    :
                    <p>{t('questions.noAnswerYet')}</p>
            }
        </div>
    </ListItem>;
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

function Questions(){
    const {t} = useTranslation('petView');

    const _onSubmit = values => {
        console.log(values);
    }

    return <>
        <QuestionsForm onSubmit={_onSubmit}/>

        <List bordered={true}>
            {
                questions.length > 0 ?
                    questions.map(question => (<QARow QA={question}/>))
                    :
                    <p>{t('questions.noQuestionsYet')}</p>
            }
        </List>
    </>;
}

export default Questions;