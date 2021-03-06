import React, {useEffect, useState} from 'react';
import {getRequests} from "../api/requests";
import useLogin from "./useLogin";

const useRequests = (initialFilters) => {
    const [requests, setRequests] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {id, jwt} = useLogin().state

    const fetchRequests = async filters => {
        setFetching(true);

        try{
            const params = Object.assign(filters, {userId: id});
            const {amount, list, pages, pageSize} = await getRequests(params, jwt);

            setRequests(list);

            setPaginationInfo({amount, pages, pageSize})
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    useEffect(()=>{
        fetchRequests(initialFilters);
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {requests, fetchRequests, fetching, pages, amount, pageSize}
};

export default useRequests;