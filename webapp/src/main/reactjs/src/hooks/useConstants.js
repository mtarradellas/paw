import React, {useState, useEffect} from 'react';
import {repeatedFetch, retryFetch} from "../api/utils";
import {getDepartments, getProvinces} from "../api/locations";
import {getBreeds, getSpecies} from "../api/species";

const FETCH_AGAIN_TIME = 1000 * 60 * 60;

const RETRY_FETCH_TIME = 2000;

const useConstants = () => {
    const [provinces, setProvinces] = useState(null);
    const [departments, setDepartments] = useState(null);
    const [breeds, setBreeds] = useState(null);
    const [species, setSpecies] = useState(null);

    const [fetching, setFetching] = useState(true);
    const [loaded, setLoaded] = useState(false);

    const fetchProvinces = async () => {
        const result = await getProvinces();

        setProvinces(result);
    };

    const fetchDepartments = async () => {
        const result = await getDepartments();

        setDepartments(result);
    };

    const fetchBreeds = async () => {
        const result = await getBreeds();

        setBreeds(result);
    };

    const fetchSpecies = async () => {
        const result = await getSpecies();

        setSpecies(result);
    };

    const fetch = async () => {
        setFetching(true);

        await Promise.all([
            retryFetch(fetchProvinces, RETRY_FETCH_TIME),
            retryFetch(fetchDepartments, RETRY_FETCH_TIME),
            retryFetch(fetchBreeds, RETRY_FETCH_TIME),
            retryFetch(fetchSpecies, RETRY_FETCH_TIME)
        ]);

        setFetching(false);
        setLoaded(true);
    };

    useEffect(()=>{
        repeatedFetch(fetch, FETCH_AGAIN_TIME);
    }, []);


    return {provinces, departments, breeds, species, fetching, loaded};
};

export default useConstants;