import React, {useState} from 'react';
import {Formik} from "formik";
import * as Yup from 'yup';
import {Form, Input} from 'formik-antd';
import {Rate, Divider, Button} from "antd";
import _ from 'lodash';
import {useTranslation} from "react-i18next";

const {TextArea} = Input;

const FormItem = Form.Item;

//targetId, score, description
function MakeAReviewForm({onSubmit}){
    const {t} = useTranslation('userView');

    const [submitting, setSubmitting] = useState(false);

    const _onSubmit = async values => {
        setSubmitting(true);

        await onSubmit(values);

        setSubmitting(false);
    };

    return <>
        <Divider/>
        <h1><b>{t('makeAReview.title')}:</b></h1>
        <Formik
        initialValues={
            {
                score: '',
                description: ''
            }
        }
        validationSchema={
            Yup.object().shape({
                score: Yup.number()
                    .required(t('makeAReview.form.score.required')),
                description: Yup.string()
                    .max(2048, max => t('makeAReview.form.description.max', {max}))
                    .required(t('makeAReview.form.description.required'))
            })
        }
        onSubmit={_onSubmit}
        render={
            ({setFieldValue, values}) => {

                return <Form layout={'vertical'}>
                    <FormItem name={"score"} label={t("makeAReview.form.score.label")}>
                        <Rate tooltips={_.times(5, i => i + 1)}
                              onChange={newValue => setFieldValue('score', newValue)}
                              value={values.score}
                        />
                    </FormItem>

                    <FormItem name={'description'} label={t("makeAReview.form.description.label")}>
                        <TextArea name={'description'} rows={4}/>
                    </FormItem>

                    <FormItem name>
                        <Button type="primary" htmlType="submit" loading={submitting}>
                            {t("makeAReview.form.submit")}
                        </Button>
                    </FormItem>
                </Form>
            }
        }
    />
    </>
}

export default MakeAReviewForm;