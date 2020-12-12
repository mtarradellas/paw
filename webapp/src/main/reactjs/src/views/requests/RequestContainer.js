import React from 'react';
import {useTranslation, Trans} from "react-i18next";
import {Row, Col, Divider, List, Button} from 'antd';
import {Link} from "react-router-dom";
import {PET} from '../../constants/routes';

import GenericNotification from '../other/GenericNotification'

//TODO: ver el tema de formateo de fechas
//TODO: manejo de formularios para botones y links y eso

function RequestNotification(
    {creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId}) {
    const {t} = useTranslation("requests");

    let reqTarget = <p>{t("messages.error")}</p>
    let reqStatus = <p>{t("messages.error")}</p>
    let reqButtons = <p>{t("messages.error")}</p>

    if (status === "ACCEPTED") {

    } else if (status === "REJECTED") {

    } else if (status === "PENDING") {
        reqTarget = (
            <p>{t("messages.pending", {petName: pet})}
                <small className={"date-text"}> {creationDate}</small>
            </p>
        )
        reqStatus = <p>{t("status.pending")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={"/pet/" + petId}>Visit Pet</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger href={"/borrarpet"}>Cancel</Button>
            </div>
        )

    } else if (status === "CANCELED") {

    } else if (status === "SOLD") {

    }

    return (
        <GenericNotification target={reqTarget} status={reqStatus} buttons={reqButtons}/>
    )
}

function RequestContainer(
    {
        requests
    }
) {
    return (
        <div className={"requests-container"}>

            <List split={false}>
                {
                    requests
                        .map(
                            (request) => (
                                <List.Item key={request.id}>
                                    <RequestNotification {...request} />
                                </List.Item>
                            )
                        )
                }
            </List>
        </div>
    )
}

export default RequestContainer;