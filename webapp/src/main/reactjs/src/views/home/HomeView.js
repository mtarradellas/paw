import React from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Divider, Pagination} from 'antd';

import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";
import ContentWithSidebar from "../../components/ContentWithSidebar";

const nairobi =  {
    name: "Nairobi",
    specie: "Perro",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    uploadDate: "05-05-2019"
};

const samplePets = []
for(let i=0; i<50; i++){
    samplePets.push(Object.assign({}, nairobi, {id: i}))
}

function SideContent(){
    return <div className={"home__filter"}>
        <FilterOptionsForm/>
    </div>;
}

function MainContent({pets, petCount}){
    const {t} = useTranslation("home");

    return <div className={"home__pets"}>
        <h1><b>{t("pets.title")}</b> ({t("pets.results-count", {count: petCount})})</h1>
        <Divider orientation={"left"}>
            <Pagination defaultCurrent={1} total={50}/>
        </Divider>
        <div className={"pet-card-container"}>
            {
                pets.map(
                    (pet) => <PetCard key={pet.id} pet={pet}/>
                )
            }
        </div>
        <Divider orientation={"left"}>
            <Pagination defaultCurrent={1} total={50}/>
        </Divider>
    </div>
}


function HomeView(){
    const pets = samplePets;
    const petCount = samplePets.length;

    return <ContentWithSidebar
                    sideContent={
                        <SideContent/>
                    }
                    mainContent={
                        <MainContent petCount={petCount} pets={pets}/>
                    }
                />;
}

export default HomeView;