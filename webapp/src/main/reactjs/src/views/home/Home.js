import React from "react";
import FilterOptionsForm from "./FilterOptionsForm";
import {Divider, Pagination} from 'antd';

import "../../css/home/home.css";
import {useTranslation} from "react-i18next";
import PetCard from "./PetCard";

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
    samplePets.push(nairobi)
}


function Home(){
    const {t} = useTranslation("home");

    const petCount = samplePets.length;

    console.log(samplePets)

    return <div className={"home__container"}>
        <div className={"content-region home__filter"}>
            <FilterOptionsForm/>
        </div>

        <div className={"content-region home__pets"}>
            <h1><b>{t("pets.title")}</b> ({t("pets.results-count", {count: petCount})})</h1>
            <Divider orientation={"left"}>
                <Pagination defaultCurrent={1} total={50}/>
            </Divider>
            <div className={"pet-card-container"}>
                {
                    samplePets.map(
                        (pet) => <PetCard {...pet}/>
                    )
                }
            </div>
            <Divider orientation={"left"}>
                <Pagination defaultCurrent={1} total={50}/>
            </Divider>
        </div>
    </div>;
}

export default Home;