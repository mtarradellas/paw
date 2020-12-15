import React, {useEffect, useState, useContext} from 'react';
import {getPets} from "../api/pets";
import ConstantsContext from '../constants/constantsContext';

const usePets = () => {
    const [pets, setPets] = useState(null);
    const [fetching, setFetching] = useState(false);

    const {breeds, species} = useContext(ConstantsContext);

    const fetchPets = async filters => {
        setFetching(true);

        try{
            const result = await getPets(filters);

            const mappedPets = result.map(
                pet => {
                    const {breedId, speciesId} = pet;

                    return Object.assign(pet, {breed: breeds[breedId].name, specie: species[speciesId].name})
                }
            );

            setPets(mappedPets);
        }catch (e) {
            console.error(e);
        }

        setFetching(false);
    };

    useEffect(()=>{
        fetchPets({});
    }, []);

    return {pets, fetchPets, fetching}
};

export default usePets;