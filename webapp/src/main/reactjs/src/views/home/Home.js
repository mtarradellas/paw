import React from "react";
import FilterOptionsForm from "./FilterOptionsForm";

import "../../css/home.css";

function Home(){
    return <div className={"home__container"}>
        <div className={"content-region home__filter"}>
            <FilterOptionsForm/>
        </div>

        <div className={"content-region home__pets"}>
            <p>Aca aparecen las cards</p>
        </div>
    </div>;
}

export default Home;