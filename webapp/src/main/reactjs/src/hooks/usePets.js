import React, {useState, useContext} from 'react';
import {getPets} from "../api/pets";
import ConstantsContext from '../constants/constantsContext';

const usePets = ({additionalFilters}) => {
    const [pets, setPets] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);
    
    const fetchPets = async filters => {
        setFetching(true);

        try{
            const {amount, list, pages, pageSize} = await getPets(Object.assign({}, filters, additionalFilters));

            setPets(list);

            setPaginationInfo({amount, pages, pageSize});
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    const {amount, pages, pageSize} = paginationInfo;

    return {pets, fetchPets, fetching, pages, amount, pageSize}
};

export default usePets;