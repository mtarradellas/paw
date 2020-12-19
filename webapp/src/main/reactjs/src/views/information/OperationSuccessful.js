import React from 'react';
import {useTranslation} from "react-i18next";

import {LOGIN} from "../../constants/routes"
import {Link} from "react-router-dom";

function OperationSuccessful({title}) {
    const {t} = useTranslation("informationPages")

    return (
        <div>
            <div className={"centered"}>
                <h1><b>{title}</b></h1>
            </div>
            <br/>
            <br/>
            <div className={"centered"}>
                <h1>{t('operationSuccessful')}</h1>
                <h2><Link to={LOGIN}>{t('goToLogin')}</Link></h2>
            </div>
            <div style={{height: "22rem"}}/>
        </div>

    )

}

export default OperationSuccessful;