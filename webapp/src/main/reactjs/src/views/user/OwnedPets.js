import React, {useState, useEffect} from 'react';
import {Divider, Pagination, Spin} from "antd";
import _ from "lodash";
import usePets from "../../hooks/usePets";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";
import {useLocation, useHistory} from 'react-router-dom';
import queryString from "query-string";

function parseQuery(location, paramKey){
    return parseInt(queryString.parse(location.search)[paramKey]);
}

function OwnedPets({paramKey, userId, title, filters, admin, error}){
    const {t} = useTranslation('userView');

    const history = useHistory();
    const location = useLocation();

    const {pets, fetchPets, fetching, amount, pageSize} = usePets({});
    const [currentPage, setCurrentPage] = useState(1);

    const _onChangePagination = async newValue => {
        history.push({search: '?' + queryString.stringify({[paramKey]: newValue})});

        await onFetchPets(newValue);
    };

    const onFetchPets = async page => {
        setCurrentPage(page);

        await fetchPets(Object.assign({page}, filters));
    };

    useEffect(()=>{
        onFetchPets(parseQuery(location, paramKey) || 1);
    }, [userId]);

    const aux = !(admin === null || !admin);

    const shouldShowPagination = !_.isNil(pageSize) && !_.isNil(amount) && amount > pageSize;

    return <>{
        pets === null ? <Spin/> :
        amount === 0 ? <p><i>{t(error)}</i></p>
        :
        <>
            <h2><b>{t(title)}</b> {
                pets === null ?
                    <Spin/>
                    :
                    '(' + t('totalResults', {count: amount}) + ')'
            }</h2>

            <div className={"user-view--pets-container"}>
                <Divider orientation={"left"}>
                    {
                        shouldShowPagination &&
                            <Pagination showSizeChanger={false} current={currentPage} total={amount} pageSize={pageSize} onChange={_onChangePagination}/>
                    }
                </Divider>
                {
                    _.isNil(pets) || fetching ?
                        <Spin/>
                        :
                        pets.length > 0 ?
                            pets.map(pet => (<PetCard admin={aux} key={pet.id} pet={pet}/>))
                            :
                            <p>{t('noPets')}</p>
                }
                <Divider orientation={"left"}>
                    {
                        shouldShowPagination &&
                            <Pagination showSizeChanger={false} current={currentPage} total={amount} pageSize={pageSize} onChange={_onChangePagination}/>
                    }
                </Divider>
            </div>
        </>
    }</>
}

export default OwnedPets;