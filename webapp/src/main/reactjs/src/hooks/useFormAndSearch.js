import React, {useState, useEffect} from 'react';
import usePets from "./usePets";
import {petStatus} from "../constants/petStatus";
import queryString from 'query-string';
import {useLocation, useHistory} from 'react-router-dom';
import {HOME, LOGIN} from "../constants/routes";

function defaultFilters(location){
    const defaultFilters = {page: 1};
    if(location.pathname === HOME){
        const parsed = queryString.parse(location.search);

        Object.assign(defaultFilters, parsed);
    }
    return defaultFilters;
}

function useFormAndSearch(){
    const location = useLocation();
    const history = useHistory();
    const [filters, setFilters] = useState(defaultFilters(location));

    const {pets, fetching, fetchPets, pages, amount, pageSize} = usePets({additionalFilters: {status: petStatus.AVAILABLE}});

    const pushFiltersToQueryParams = filters => {
        history.push({pathname: HOME, search: '?' + queryString.stringify(filters)});
    };

    const onSubmitFilters = async values => {
        const {find} = filters;
        const newFilters = Object.assign(values, {page: 1, find});

        setFilters(newFilters);

        pushFiltersToQueryParams(newFilters);

        await fetchPets(newFilters);
    };

    const onSubmitSearch = async ({find}) => {
        const newFilters = Object.assign(filters, {find: find || null}, {page: 1});

        setFilters(newFilters);

        pushFiltersToQueryParams(newFilters);

        await fetchPets(newFilters);
    };

    const onChangePage = async page => {
        const newFilters = Object.assign(filters, {page});

        setFilters(newFilters);

        pushFiltersToQueryParams(newFilters);

        await fetchPets(newFilters);
    };

    const clearFilters = async () => {
        const newFilters = {page: 1};
        setFilters(newFilters);

        pushFiltersToQueryParams(newFilters);

        await fetchPets(newFilters);
    };

    useEffect(()=>{
        fetchPets(filters);
    }, []);

    return {
        clearFilters,
        onSubmitSearch,
        onChangePage,
        filters,
        onSubmitFilters,
        find: filters.find,
        pets,
        fetching,
        pages,
        amount,
        pageSize,
        currentPage: parseInt(filters.page)
    };
}

export default useFormAndSearch;