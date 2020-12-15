import React, {useState} from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Divider, Pagination, Spin} from 'antd';
import _ from 'lodash';
import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import usePets from "../../hooks/usePets";


function SideContent(){
    return <div className={"home__filter"}>
        <FilterOptionsForm/>
    </div>;
}

function MainContent({petCount, pets, fetching, fetchPage}){
    const [currentPage, setCurrentPage] = useState(1);

    const {t} = useTranslation("home");

    const totalPages = 50;

    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPage(currentPage);
    };

    return <div className={"home__pets"}>
        <h1><b>{t("pets.title")}</b>
            {
                !_.isNil(pets) && " (" + t("pets.results-count", {count: petCount}) + ")"
            }
        </h1>


        <Divider orientation={"left"}>
            <Pagination current={currentPage} total={totalPages} onChange={_onChangePagination}/>
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
                            <Pagination current={currentPage} total={totalPages} onChange={_onChangePagination}/>
                        </Divider>
                    </>
            }
        </div>

    </div>
}


function HomeView(){
    const {pets, fetching, fetchPets} = usePets();

    const petCount = 50;

    const fetchPage = page => {
        fetchPets({page})
    };

    return <ContentWithSidebar
                    sideContent={
                        <SideContent/>
                    }
                    mainContent={
                        <MainContent petCount={petCount} pets={pets} fetching={fetching} fetchPage={fetchPage}/>
                    }
                />;
}

export default HomeView;