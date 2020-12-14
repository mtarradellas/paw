import React from 'react';
import {useTranslation} from "react-i18next";

import {HOME} from "../../constants/routes"
import {Link} from "react-router-dom";

function ErrorWithoutImage({title, text}) {
    const {t} = useTranslation('error-pages')

    return (
        <div>
            <div className={"centered"}>
                <h1><b>{title}</b></h1>
            </div>
            <br/>
            <br/>
            <div className={"centered"}>
                <h1>{text}</h1>
                <h2><Link to={HOME}>{t('backToHome')}</Link></h2>
            </div>
            <div style={{height: "22rem"}}/>
        </div>

    )

}

export default ErrorWithoutImage;