import React, {useContext, useEffect, useState} from 'react';
import {getAdminPets} from "../../api/admin/pets";
import useLogin from "../useLogin";
import FilterAndSearchContext from "../../constants/filterAndSearchContext";

const useAdminPets = () => {
    const [adminPets, setAdminPets] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {jwt} = useLogin().state;

    const {
        find
    } = useContext(FilterAndSearchContext);

    const fetchAdminPets = async filters => {
        setFetching(true);
        if(find != null){
            filters.find = find;
        }
        try{
            const {amount, list, pages, pageSize} = await getAdminPets(filters,jwt);

            setAdminPets(list);
            setPaginationInfo({amount, pages, pageSize});
        }catch (e) {
            console.log(e)
        }
        setFetching(false);
    };
    useEffect(()=>{
        fetchAdminPets({page: 1});
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {adminPets, fetchAdminPets, fetching, pages, amount, pageSize};
}

export default useAdminPets;