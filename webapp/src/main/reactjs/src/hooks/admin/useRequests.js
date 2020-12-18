import React, {useEffect, useState} from 'react';
import {getAdminRequests} from "../../api/admin/requests";
import useLogin from "../useLogin";

const useAdminRequests = () => {
    const [adminRequests, setAdminRequests] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {id, jwt} = useLogin().state

    const fetchAdminRequests = async filters => {
        setFetching(true);

        try{
            const params = Object.assign(filters, {});
            const {amount, list, pages, pageSize} = await getAdminRequests(params,jwt);

            setAdminRequests(list);
            setPaginationInfo({amount, pages, pageSize})

        }catch (e){
            console.log(e)
        }
        setFetching(false);
    };

    useEffect(()=>{
        fetchAdminRequests({page: 1});
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {adminRequests,fetchAdminRequests,fetching, pages, amount, pageSize}
}

export default useAdminRequests;