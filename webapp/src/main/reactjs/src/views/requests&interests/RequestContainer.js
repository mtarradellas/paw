import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {List, Button, Modal} from 'antd';
import {PET, USER} from '../../constants/routes';
import {requestStatus as REQUEST_STATUS} from '../../constants/requestStatus';

import useLogin from "../../hooks/useLogin";

import {cancelRequest, recoverRequest} from "../../api/requests";

import ListContainer from '../../components/ListContainer'
import moment from "moment";
import {Link} from "react-router-dom";

function RequestNotification(
    {id, creationDate, updateDate, status, username, userId, petName, petId, modal, fetchFilters}) {
    const {t} = useTranslation("requests");

    const {jwt} = useLogin().state;

    const [petStatus, setStatus] = useState(status)

    let reqTarget = null;
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (petStatus === REQUEST_STATUS.REJECTED) {
        reqTarget = (
            <p>{t("messages.rejected", {petName: petName})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            </p>
        )
        reqStatus = <p>{t("status.rejected")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Link to={PET + petId}>
                    <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
            </div>
        )
    } else if (petStatus === REQUEST_STATUS.PENDING) {
        const onConfirm = async () => {
            try{
                await cancelRequest(id,jwt);
                fetchFilters();
                setStatus(REQUEST_STATUS.CANCELED);
            }catch (e){
                console.log(e)
            }
        }

        const modalMessage = t("modals.cancel")

        reqTarget = (
            <p>{t("messages.pending", {petName: petName})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            </p>
        )
        reqStatus = <p>{t("status.pending")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Link to={PET + petId}>
                    <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Button type={"primary"} danger onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.cancel")}</Button>
            </div>
        )

    } else if (petStatus === REQUEST_STATUS.CANCELED) {
        const onConfirm = async () => {
            try{
                await recoverRequest(id, jwt);
                fetchFilters();
                setStatus(REQUEST_STATUS.PENDING);
            }catch (e){
                console.log(e)
            }

        }

        const modalMessage = t("modals.recover")

        reqTarget = (
            <p>{t("messages.canceled", {petName: petName})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            </p>
        )
        reqStatus = <p>{t("status.canceled")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Link to={PET + petId}>
                    <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Button type={"primary"} danger onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )

    } else if ( petStatus === REQUEST_STATUS.SOLD) {
        reqTarget = (
            <p>{t("messages.sold", {petName: petName})}
                <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            </p>
        )
        reqStatus = <p>{t("status.sold")}</p>
        reqButtons = (
            <div className={"button-container"}>
                <Link to={PET + petId}>
                <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Link to={USER + userId}>
                    <Button type={"primary"}>{t("buttons.visitOwner")}</Button>
                </Link>
            </div>
        )
    }


    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded} />
    )
}

function RequestContainer({requests, fetchFilters}) {
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
                                    <RequestNotification
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

export default RequestContainer;