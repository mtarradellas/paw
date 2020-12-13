import React, {useState} from 'react';
import {useTranslation, Trans} from "react-i18next";
import {List, Button, Modal} from 'antd';
import {PET, USER} from '../../constants/routes';
import {Link} from "react-router-dom";


import GenericNotification from './GenericNotification'

//TODO: ver el tema de formateo de fechas
//TODO: manejo de formularios para botones y links y eso

function RequestNotification(
    {id,creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId, modal}) {
    const {t} = useTranslation("requests");

    let reqTarget = null;
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

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
        shaded = true;
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
        const onConfirm = () => {
            alert("canceled" + id)
        }

        const modalMessage = t("modals.cancel")

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
                <Button type={"primary"} danger onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.cancel")}</Button>
            </div>
        )

    } else if (status === "CANCELED") {
        shaded = true;
        const onConfirm = () => {
            alert("recovered" + id)
        }

        const modalMessage = t("modals.recover")

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
                <Button type={"primary"} danger onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )

    } else if (status === "SOLD") {
        shaded = true;
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
        <GenericNotification target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded} />

    )
}

function RequestContainer({requests}) {
    const {t} = useTranslation("requests");

    const [modalState, setModalState] = useState({show: false, callbackMethod: null, modalMessage: ""});
    const showModal = (callback, message) => {
        setModalState({show: true, callbackMethod: callback, modalMessage:message});
    };
    const onOk = () => {
        modalState.callbackMethod()
        setModalState(false);
    };
    const onCancel = () => {
        setModalState(false);
    };

    return (
        <div className={"requests-container"}>

            <List split={false}>
                {
                    requests
                        .map(
                            (request) => (
                                <List.Item key={request.id}>
                                    <RequestNotification modal={showModal} {...request} />
                                </List.Item>
                            )
                        )
                }
            </List>
            <Modal
                title={t("modals.pleaseConfirm")}
                visible={modalState.show}
                onCancel={onCancel}
                footer={[
                    <div key={"confirmation-modal-key"}>
                    <Button key="cancel" onClick={onCancel}>
                        {t("buttons.cancel")}
                    </Button>
                    <Button key="submit" type="primary" onClick={onOk}>
                        {t("buttons.imSure")}
                    </Button>
                    </div>
                ]}
            >
                <div>
                    {modalState.modalMessage}
                </div>
            </Modal>
        </div>
    )
}

export default RequestContainer;