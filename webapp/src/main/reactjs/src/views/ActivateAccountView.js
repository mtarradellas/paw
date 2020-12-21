import React, {useState, useEffect} from 'react';
import {Link, useParams} from "react-router-dom";
import SmallCenteredContent from "../components/SmallCenteredContent";
import {useTranslation} from "react-i18next";
import {activateAccount} from "../api/authentication";
import {LOGIN} from "../constants/routes";
import {Spin} from "antd";


function ActivateAccountView() {
    const {token} = useParams();
    const {t} = useTranslation("activate")

    let content;
    const [activated, setActivated] = useState(false);
    const [loading, setLoading] = useState(true);

    const fetchActivateAccount = async () =>{
        try{
            await activateAccount(token);
            setActivated(true);
        }catch (e) {
            //TODO: connection error deberia hacer retry
            setActivated(false);
        }
        setLoading(false);
    }

    useEffect(() => {
         fetchActivateAccount();
    }, []);

    if (activated) {
        content = (
            <div>
                <h1>{t('title')}</h1>
                <p>{t("content")}</p>
                <Link to={LOGIN}>{t("link")}</Link>
            </div>
        )
    }else{
        content = (<h2>{t("error")}</h2>)
    }

    return <SmallCenteredContent>
        {loading? <Spin/>:content}

    </SmallCenteredContent>
}

export default ActivateAccountView;