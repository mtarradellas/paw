import React, {useState} from 'react';
import {Button, Divider, List, Modal} from "antd";
import {useTranslation} from "react-i18next";

import ListContainer from "../../../components/ListContainer";
import {ADMIN_PET, ADMIN_USER, ADMIN_EDIT_REQUEST} from "../../../constants/routes";


function Request(
    {id, creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId, modal}) {
    const {t} = useTranslation("admin");

    let reqTarget = (
        <p>{t("requestsList.isInterested", {petName: pet, username: user})}
            <small className={"date-text"}> {updateDate.toLocaleString()}  </small>
            (id: {id})
        </p>

    );
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (status === "ACCEPTED") {
        const onConfirm = () => {
            alert("canceled" + id)
        }

        const modalMessage = t("modals.cancelRequest")

        reqStatus = <p>{t("status.notSold")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_REQUEST + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.cancel")}</Button>
            </div>
        )
    } else if (status === "REJECTED") {
        const onConfirm = () => {
            alert("canceled" + id)
        }

        const modalMessage = t("modals.cancelRequest")

        reqStatus = <p>{t("status.rejected")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_REQUEST + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.cancel")}</Button>
            </div>
        )
    } else if (status === "PENDING") {
        const onConfirm = () => {
            alert("canceled" + id)
        }

        const modalMessage = t("modals.cancelRequest")

        reqStatus = <p>{t("status.pending")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_REQUEST + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.cancel")}</Button>
            </div>
        )

    } else if (status === "CANCELED") {
        shaded = true;
        const onConfirm = () => {
            alert("recovered" + id)
        }

        const modalMessage = t("modals.recoverRequest")

        reqStatus = <p>{t("status.canceled")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_REQUEST + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )

    } else if (status === "SOLD") {
        shaded = true;
        const onConfirm = () => {
            alert("recovered" + id)
        }

        const modalMessage = t("modals.recoverRequest")

        reqStatus = <p>{t("status.soldTo", {ownerName: newPetOwner})}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + userId}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_REQUEST + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )
    }

    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded}/>

    )
}

function AdminRequestsContainer({requests}) {
    const {t} = useTranslation("admin");

    const [modalState, setModalState] = useState({show: false, callbackMethod: null, modalMessage: ""});
    const showModal = (callback, message) => {
        setModalState({show: true, callbackMethod: callback, modalMessage: message});
    };
    const onOk = () => {
        modalState.callbackMethod()
        setModalState(false);
    };
    const onCancel = () => {
        setModalState(false);
    };

    return (
        <div className={"admin-container"}>
            <List split={false}>
                {
                    requests
                        .map(
                            (request) => (
                                <List.Item key={request.id}>
                                    <Request modal={showModal} {...request} />
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

export default AdminRequestsContainer;