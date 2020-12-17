import React, {useEffect, useState} from 'react';
import useLogin from "./useLogin";
import {getInterests} from "../api/interests";

const useInterests = () => {
    const [interests, setInterests] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {id, jwt} = useLogin().state

    const fetchInterests = async filters => {
        setFetching(true);

        try{
            const params = Object.assign(filters, {targetId: id});
            const {amount, list, pages, pageSize} = await getInterests(params, jwt);

            setInterests(list);

            setPaginationInfo({amount, pages, pageSize})
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    useEffect(()=>{
        fetchInterests({page: 1});
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {interests, fetchInterests, fetching, pages, amount, pageSize}
};

export default useInterests;