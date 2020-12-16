import React, {useState, useEffect} from 'react';
import {getReviews} from "../api/reviews";
import useLogin from "./useLogin";

function useReviewsPagination({userId}){
    const [reviews, setReviews] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [fetching, setFetching] = useState(false);
    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null, average: null});
    const {state} = useLogin();

    const {jwt} = state;

    const fetchReviews = async filters => {
        setFetching(true);

        try{
            const {amount, list, pages, pageSize, average} = await getReviews(filters, jwt);

            setReviews(list);

            setPaginationInfo({amount, pages, pageSize, average});
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    const changePage = async page => {
        setCurrentPage(page);

        await fetchReviews({page, targetId: userId});
    };

    useEffect(()=>{
        fetchReviews({page: 1, targetId: userId});
    }, []);

    const {amount, pageSize, average} = paginationInfo;

    return {reviews, currentPage, fetching, fetchReviews, setCurrentPage, amount, pageSize, average, changePage};
}

export default useReviewsPagination;