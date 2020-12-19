import React, {useState} from 'react';
import usePets from "./usePets";

function useFormAndSearch(){
    const [filters, setFilters] = useState({page: 1});

    const {pets, fetching, fetchPets, pages, amount, pageSize} = usePets({});

    const onSubmitFilters = async values => {

        const {find} = filters;
        const newFilters = Object.assign(values, {page: 1, find});

        setFilters(newFilters);

        await fetchPets(newFilters);
    };

    const onSubmitSearch = async ({find}) => {
        const newFilters = Object.assign(filters, {find: find || null}, {page: 1});

        setFilters(newFilters);

        await fetchPets(newFilters);
    };

    const onChangePage = async page => {
        const newFilters = Object.assign(filters, {page});

        setFilters(newFilters);

        await fetchPets(newFilters);
    };

    return {
        onSubmitSearch,
        onChangePage,
        filters,
        onSubmitFilters,
        find: filters.find,
        pets,
        fetching,
        pages,
        amount,
        pageSize
    };
}

export default useFormAndSearch;