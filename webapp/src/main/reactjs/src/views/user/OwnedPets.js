import React, {useState} from 'react';
import {Divider, Pagination, Spin} from "antd";
import _ from "lodash";
import usePets from "../../hooks/usePets";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";

function OwnedPets({userId}){
    const {t} = useTranslation('userView');

    const {pets, fetchPets, fetching, amount, pageSize} = usePets({initialFilters: {ownerId: userId}});
    const [currentPage, setCurrentPage] = useState(1);

    const _onChangePagination = async newValue => {
        setCurrentPage(newValue);

        await fetchPets(Object.assign({page: newValue}, {ownerId: userId}));
    };

    return <>
            <h1><b>{t('petsTitle')}</b> {
                pets === null ?
                    <Spin/>
                    :
                    '(' + t('totalResults', {count: amount}) + ')'
            }</h1>

            <div className={"user-view--pets-container"}>
                <Divider orientation={"left"}>
                    {
                        pageSize && amount &&
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
}

export default OwnedPets;