import React, {useState, useContext} from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Divider, Pagination, Spin} from 'antd';
import _ from 'lodash';
import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterAndSearchContext from '../../constants/filterAndSearchContext'


function SideContent({}){
    return <div className={"home__filter"}>
        <FilterOptionsForm/>
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
                !_.isNil(pageSize) && _.isNil(petCount) &&
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

    const {
        pets,
        fetching,
        pages,
        amount,
        pageSize,
        onChangePage
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
                            setCurrentPage={setCurrentPage}
                        />
                    }
                />;
}

export default HomeView;