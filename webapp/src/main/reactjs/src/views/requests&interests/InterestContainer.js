import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";
import {PET, USER} from '../../constants/routes';

import ListContainer from '../../components/ListContainer'
import useLogin from "../../hooks/useLogin";

import {acceptInterest,rejectInterest} from "../../api/interests";
import moment from "moment";

function InterestNotification(
    {id,creationDate, updateDate, status, username, userId, petName, petId, modal, fetchFilters}) {
    const {t} = useTranslation("interests");

    const INTEREST_STATUS = {
        PENDING: 0,
        REJECTED: 2,
        CANCELED: 3,
        SOLD: 4
    }

    const {jwt} = useLogin().state;
    const [petStatus, setStatus] = useState(status)

    let reqTarget = null;
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (petStatus === INTEREST_STATUS.REJECTED) {
        reqTarget = (
            <p>{t("messages.rejected", {petName: petName, username: username})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
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
    } else if (petStatus === INTEREST_STATUS.PENDING) {
        const onConfirmAccept = async () => {
            try{
                await acceptInterest(id,jwt);
                fetchFilters();
                setStatus(INTEREST_STATUS.SOLD);
            }catch (e){
                console.log(e)
            }
        }
        const modalMessageAccept = t("modals.acceptRequest")

        const onConfirmReject = async () => {
            try{
                await rejectInterest(id,jwt);
                fetchFilters();
                setStatus(INTEREST_STATUS.REJECTED);
            }catch (e){
                console.log(e)
            }
        }
        const modalMessageReject = t("modals.rejectRequest")

        reqTarget = (
            <p>{t("messages.pending", {petName: petName, username: username})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
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

    } else if (petStatus === INTEREST_STATUS.CANCELED) {
        reqTarget = (
            <p>{t("messages.canceled", {petName: petName, username: username})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
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

    } else if (petStatus === INTEREST_STATUS.SOLD) {
        reqTarget = (
            <p>{t("messages.sold", {petName: petName, username: username})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            </p>
        )
        reqStatus = <p>{t("status.sold")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={PET + petId}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={USER + userId}>{t("buttons.visitUser")}</Button>
            </div>
        )
    }

    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded} />

    )
}

function InterestContainer({interests, fetchFilters}) {
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
                                    <InterestNotification
                                        modal={showModal}
                                        fetchFilters={fetchFilters}
                                        {...request} />
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