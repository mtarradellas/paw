import React, {useEffect, useState, useContext} from 'react';
import {getRequests} from "../api/requests";
import ConstantsContext from '../constants/constantsContext';
import {getPets} from "../api/pets";

const useRequests = () => {
    const [requests, setRequests] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const fetchRequests = async filters => {
        setFetching(true);

        try{
            const {amount, list, pages, pageSize} = await getRequests(filters);

            setRequests(list);

            setPaginationInfo({amount, pages, pageSize});
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    useEffect(()=>{
        fetchRequests({page: 1});
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {requests, fetchRequests, fetching, pages, amount, pageSize}
};

export default useRequests;