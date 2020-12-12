import React, {useState} from 'react';
import {useTranslation, Trans} from "react-i18next";
import {Row, Col, Divider, List, Button, Modal} from 'antd';
import {PET, USER} from '../../constants/routes';

import GenericNotification from '../other/GenericNotification'

//TODO: ver el tema de formateo de fechas
//TODO: manejo de formularios para botones y links y eso

function RequestNotification(
    {id,creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId}) {
    const {t} = useTranslation("requests");

    let reqTarget = <p>{t("messages.error")}</p>
    let reqStatus = <p>{t("messages.error")}</p>
    let reqButtons = <p>{t("messages.error")}</p>

    if (status === "ACCEPTED") {
        reqTarget = (
            <p>{t("messages.accepted", {petName: pet})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.notSold")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
            </div>
        )
    } else if (status === "REJECTED") {
        reqTarget = (
            <p>{t("messages.rejected", {petName: pet})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.rejected")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
            </div>
        )
    } else if (status === "PENDING") {
        reqTarget = (
            <p>{t("messages.pending", {petName: pet})}
                <small className={"date-text"}> {creationDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.pending")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger /*onClick={showModal}*/>{t("buttons.cancel")}</Button>
            </div>
        )

    } else if (status === "CANCELED") {
        reqTarget = (
            <p>{t("messages.canceled", {petName: pet})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.canceled")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger /*onClick={showModal}*/>{t("buttons.recover")}</Button>
            </div>
        )

    } else if (status === "SOLD") {
        reqTarget = (
            <p>{t("messages.sold", {petName: pet})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.sold", {ownerName: newPetOwner})}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + newPetOwnerId}>{t("buttons.visitOwner")}</Button>
            </div>
        )
    }

    return (
        <GenericNotification target={reqTarget} status={reqStatus} buttons={reqButtons}/>

    )
}

function RequestContainer({requests}) {
    const {t} = useTranslation("requests");

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