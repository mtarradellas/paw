import React, {useEffect, useState} from 'react';
import {getAdminUsers} from "../../api/admin/users";
import useLogin from "../useLogin";

const useAdminUsers = () => {
    const [adminUsers, setAdminUsers] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {jwt} = useLogin().state;

    const fetchAdminUsers = async filters => {
        setFetching(true);
        try{
            const {amount, list, pages, pageSize} = await getAdminUsers(filters, jwt);

            setAdminUsers(list);
            setPaginationInfo({amount, pages, pageSize});
        }catch (e){
            console.log(e)
        }
        setFetching(false);
    };

    useEffect(()=>{
        fetchAdminUsers({page: 1});
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {adminUsers, fetchAdminUsers, fetching, pages, amount, pageSize}
}
export default useAdminUsers;