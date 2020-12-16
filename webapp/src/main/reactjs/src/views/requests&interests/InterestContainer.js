import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";
import {PET, USER} from '../../constants/routes';

import ListContainer from '../../components/ListContainer'

//TODO: ver el tema de formateo de fechas
//TODO: manejo de formularios para botones y links y eso

function InterestNotification(
    {id,creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId, modal}) {
    const {t} = useTranslation("interests");

    let reqTarget = null;
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (status === "ACCEPTED") {
        const onConfirm = () => {
            alert("recovered" + id)
        }
        const modalMessage = t("modals.reserve")
        reqTarget = (
            <p>{t("messages.accepted", {petName: pet, username: user})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.notSold")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.reserve")}</Button>
            </div>
        )
    } else if (status === "REJECTED") {
        shaded = true;
        reqTarget = (
            <p>{t("messages.rejected", {petName: pet, username: user})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.rejected")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + userId}>{t("buttons.visitUser")}</Button>
            </div>
        )
    } else if (status === "PENDING") {
        const onConfirmAccept = () => {
            alert("canceled" + id)
        }
        const modalMessageAccept = t("modals.acceptRequest")

        const onConfirmReject = () => {
            alert("canceled" + id)
        }
        const modalMessageReject = t("modals.rejectRequest")

        reqTarget = (
            <p>{t("messages.pending", {petName: pet, username: user})}
                <small className={"date-text"}> {creationDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.pending")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger onClick={() => modal(onConfirmAccept, modalMessageAccept)}>{t("buttons.accept")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger onClick={() => modal(onConfirmReject, modalMessageReject)}>{t("buttons.reject")}</Button>
            </div>
        )

    } else if (status === "CANCELED") {
        shaded = true;
        reqTarget = (
            <p>{t("messages.canceled", {petName: pet, username: user})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.canceled")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + userId}>{t("buttons.visitUser")}</Button>
            </div>
        )

    } else if (status === "SOLD") {
        shaded = true;
        reqTarget = (
            <p>{t("messages.sold", {petName: pet, username: user})}
                <small className={"date-text"}> {updateDate.toLocaleString()}</small>
            </p>
        )
        reqStatus = <p>{t("status.soldTo", {ownerName: newPetOwner, username: user})}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + newPetOwnerId}>{t("buttons.visitOwner")}</Button>
            </div>
        )
    }

    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded} />

    )
}

function InterestContainer({interests}) {
    const {t} = useTranslation("interests");

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
                    interests
                        .map(
                            (request) => (
                                <List.Item key={request.id}>
                                    <InterestNotification modal={showModal} {...request} />
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

export default InterestContainer;