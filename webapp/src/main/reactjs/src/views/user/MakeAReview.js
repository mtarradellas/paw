import React, {useState, useEffect} from 'react';
import {retryFetch} from "../../api/utils";
import useLogin from "../../hooks/useLogin";
import {CAN_REVIEW_ERRORS, CREATE_REVIEW_ERRORS, createReview} from "../../api/reviews";
import {canReview as canReviewApi} from "../../api/reviews";
import MakeAReviewForm from "./MakeAReviewForm";

function MakeAReview({userId, refreshReviews}) {
    const [canReview, setCanReview] = useState(false);
    const {state, promptLogin} = useLogin();

    const {jwt} = state;

    const fetchCanReview = async () => {
        await retryFetch(async () => {
            try {
                const canReview = await canReviewApi({targetId: userId}, jwt);

                setCanReview(canReview);

                refreshReviews();
            }catch (e) {
                switch (e) {
                    case CAN_REVIEW_ERRORS.FORBIDDEN:
                        promptLogin();
                        return;
                    case CAN_REVIEW_ERRORS.CONN_ERROR:
                    default:
                        //TODO: conn error
                        break;
                }
                throw e;
            }
        })
    };

    const onRateUser = async ({score, description}) => {
        try {
            await createReview({targetId: userId, score, description}, jwt);

            refreshReviews();

            setCanReview(false);
        }catch (e) {
            switch (e) {
                case CREATE_REVIEW_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case CREATE_REVIEW_ERRORS.CONN_ERROR:
                default:
                    //TODO: conn error
                    break;
            }
        }
    };

    useEffect(()=>{
        fetchCanReview();
    }, []);

    if(!canReview)
        return <></>;

    return <MakeAReviewForm onSubmit={onRateUser}/>;
}

export default MakeAReview;