import React, {useState, useContext} from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Button, Divider, Pagination, Spin} from 'antd';
import _ from 'lodash';
import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterAndSearchContext from '../../constants/filterAndSearchContext'


function SideContent(){
    return <div className={"home__filter"}>
        <FilterOptionsForm/>
    </div>;
}

function MainContent({petCount, pets, fetching, fetchPage, pages, pageSize, currentPage}){
    const {t} = useTranslation("home");

    const {clearFilters} = useContext(FilterAndSearchContext);

    const _onChangePagination = newValue => {
        fetchPage(newValue);
    };

    const shouldShowPagination = !_.isNil(pageSize) && !_.isNil(petCount) && petCount > pageSize;

    return <div className={"home__pets"}>{

        fetching ? <Spin/> 
        :
        petCount === 0 ? <div className={"empty-home"}> 
            <div>{t('noResultsText')}</div> 
            <Button type='primary' onClick={clearFilters}>{t('noResultsBtn')}</Button>
        </div>
        :
        <>
        <h1><b>{t("pets.title")}</b>
            {
                !_.isNil(petCount) && " (" + t("pets.results-count", {count: petCount}) + ")"
            }
        </h1>


        <Divider orientation={"left"}>
            {
                shouldShowPagination &&
                    <Pagination showSizeChanger={false} current={currentPage} total={petCount} pageSize={pageSize} onChange={_onChangePagination}/>
            }
        </Divider>
        <div className={"pet-card-container"}>
            {
                _.isNil(pets) || fetching ?
                    <Spin/>
                    :
                    <>
                        {
                            pets.map(
                                (pet) => <PetCard key={pet.id} pet={pet}/>
                            )
                        }

                        <Divider orientation={"left"}>
                            {
                                shouldShowPagination &&
                                    <Pagination showSizeChanger={false} current={currentPage} total={petCount} pageSize={pageSize} onChange={_onChangePagination}/>
                            }
                        </Divider>
                    </>
            }
        </div>
        </>
    }</div>
}


function HomeView(){

    const {
        pets,
        fetching,
        pages,
        amount,
        pageSize,
        onChangePage,
        currentPage,
    } = useContext(FilterAndSearchContext);

    return <ContentWithSidebar
                    sideContent={
                        <SideContent/>
                    }
                    mainContent={
                        <MainContent
                            petCount={amount}
                            pets={pets}
                            fetching={fetching}
                            pages={pages}
                            fetchPage={onChangePage}
                            pageSize={pageSize}
                            currentPage={currentPage}
                        />
                    }
                />;
}

export default HomeView;