import React, {useState, useEffect} from 'react';
import {getReviews} from "../api/reviews";
import useLogin from "./useLogin";
import {useLocation, useHistory} from 'react-router-dom';
import queryString from 'query-string';


function parseQuery(location){
    return parseInt(queryString.parse(location.search)['reviewsPage']);
}

function useReviewsPagination({userId}){
    const location = useLocation();
    const history = useHistory();
    const [reviews, setReviews] = useState(null);
    const [currentPage, setCurrentPage] = useState(parseQuery(location));
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

        history.replace({search: '?' + queryString.stringify({reviewsPage: page})});

        await fetchReviews({page, targetId: userId});
    };

    const refresh = async () => {
        await fetchReviews({page: currentPage, targetId: userId});
    };

    useEffect(()=>{
        fetchReviews({page: 1, targetId: userId});
    }, [userId]);

    const {amount, pageSize, average} = paginationInfo;

    return {reviews, currentPage, fetching, fetchReviews, setCurrentPage, amount, pageSize, average, changePage, refresh};
}

export default useReviewsPagination;