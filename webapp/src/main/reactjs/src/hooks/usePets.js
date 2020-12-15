import React, {useEffect, useState, useContext} from 'react';
import {getPets} from "../api/pets";
import ConstantsContext from '../constants/constantsContext';

const usePets = ({initialFilters}) => {
    const [pets, setPets] = useState(null);

    const [paginationInfo, setPaginationInfo] = useState({pages: null, amount: null, pageSize: null});

    const [fetching, setFetching] = useState(false);

    const {breeds, species} = useContext(ConstantsContext);

    const fetchPets = async filters => {
        setFetching(true);

        try{
            const {amount, list, pages, pageSize} = await getPets(filters);

            const mappedPets = list.map(
                pet => {
                    const {breedId, speciesId} = pet;

                    return Object.assign(pet, {breed: breeds[breedId].name, specie: species[speciesId].name})
                }
            );

            setPets(mappedPets);

            setPaginationInfo({amount, pages, pageSize});
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    useEffect(()=>{
        fetchPets(Object.assign({page: 1}, initialFilters));
    }, []);

    const {amount, pages, pageSize} = paginationInfo;

    return {pets, fetchPets, fetching, pages, amount, pageSize}
};

export default usePets;