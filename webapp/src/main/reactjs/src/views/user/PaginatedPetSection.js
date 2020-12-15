import React, {useState} from 'react';
import {Divider, Pagination, Spin} from "antd";
import _ from "./UserView";
import usePets from "../../hooks/usePets";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";

function PaginatedPetSection({userId}){
    const {t} = useTranslation('userView');

    const {pets, fetchPets, fetching, amount, pageSize} = usePets({initialFilters: {userId}});
    const [currentPage, setCurrentPage] = useState(1);

    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPets(Object.assign({page: newValue}, filter));
    };

    return <>
            <h1><b>{t('petsTitle')}</b> {
                pets === null ?
                    <Spin/>
                    :
                    '(' + t('totalResults', {count: pets.length}) + ')'
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

export default PaginatedPetSection;