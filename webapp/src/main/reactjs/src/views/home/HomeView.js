import React, {useState} from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Divider, Pagination, Spin} from 'antd';
import _ from 'lodash';
import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import usePets from "../../hooks/usePets";


function SideContent({onChangeFilters, fetching}){
    return <div className={"home__filter"}>
        <FilterOptionsForm onChangeFilters={onChangeFilters} fetching={fetching}/>
    </div>;
}

function MainContent({petCount, pets, fetching, fetchPage, pages, pageSize, setCurrentPage, currentPage}){
    const {t} = useTranslation("home");

    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPage(newValue);
    };

    return <div className={"home__pets"}>
        <h1><b>{t("pets.title")}</b>
            {
                !_.isNil(petCount) && " (" + t("pets.results-count", {count: petCount}) + ")"
            }
        </h1>


        <Divider orientation={"left"}>
            {
                pageSize && petCount &&
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
                            <Pagination showSizeChanger={false} current={currentPage} total={petCount} pageSize={pageSize} onChange={_onChangePagination}/>
                        </Divider>
                    </>
            }
        </div>

    </div>
}


function HomeView(){
    const [currentPage, setCurrentPage] = useState(1);
    const [filters, setFilters] = useState({});

    const {pets, fetching, fetchPets, pages, amount, pageSize} = usePets({});

    const fetchPage = page => {
        fetchPets(Object.assign({page}, filters))
    };

    const onChangeFilters = newFilters => {
        setFilters(newFilters);

        fetchPets(Object.assign({page: 1}, newFilters));
    };

    return <ContentWithSidebar
                    sideContent={
                        <SideContent onChangeFilters={onChangeFilters} fetching={fetching}/>
                    }
                    mainContent={
                        <MainContent
                            petCount={amount}
                            pets={pets}
                            fetching={fetching}
                            pages={pages}
                            fetchPage={fetchPage}
                            pageSize={pageSize}
                            currentPage={currentPage}
                            setCurrentPage={setCurrentPage}
                        />
                    }
                />;
}

export default HomeView;