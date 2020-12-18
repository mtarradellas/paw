import React from 'react';
import {useTranslation} from "react-i18next";

import {HOME} from "../../constants/routes"
import {Link} from "react-router-dom";

function EmailSent({title}) {
    const {t} = useTranslation("informationPages")

    return (
        <div>
            <div className={"centered"}>
                <h1><b>{title}</b></h1>
            </div>
            <br/>
            <br/>
            <div className={"centered"}>
                <h1>{t('emailSent.checkMail')}</h1>
                <h2><Link to={HOME}>{t('backToHome')}</Link></h2>
            </div>
            <div style={{height: "22rem"}}/>
        </div>

    )

}

export default EmailSent;