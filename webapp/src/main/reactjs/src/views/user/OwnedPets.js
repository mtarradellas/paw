import React, {useState, useEffect} from 'react';
import {Divider, Pagination, Spin} from "antd";
import _ from "lodash";
import usePets from "../../hooks/usePets";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";

function OwnedPets({userId, title, error, filters}){
    const {t} = useTranslation('userView');

    const {pets, fetchPets, fetching, amount, pageSize} = usePets({});
    const [currentPage, setCurrentPage] = useState(1);

    const _onChangePagination = async newValue => {
        setCurrentPage(newValue);

        await fetchPets(Object.assign({page: newValue}, filters));
    };

    useEffect(()=>{
        _onChangePagination(1);
    }, []);

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
                        !_.isNil(pageSize) && !_.isNil(amount) &&
                            <Pagination showSizeChanger={false} current={currentPage} total={amount} pageSize={pageSize} onChange={_onChangePagination}/>
                    }
                </Divider>
                {
                    _.isNil(pets) || fetching ?
                        <Spin/>
                        :
                        pets.length > 0 ?
                            pets.map(pet => (<PetCard key={pet.id} pet={pet}/>))
                            :
                            <p>{t('noPets')}</p>
                }
                <Divider orientation={"left"}>
                    <Pagination showSizeChanger={false} current={currentPage} total={amount} pageSize={pageSize} onChange={_onChangePagination}/>
                </Divider>
            </div>
        </>
    }</>
}

export default OwnedPets;