import React, {useState} from 'react';
import {Button, List, Modal} from "antd";
import {useTranslation} from "react-i18next";

import ListContainer from "../../../components/ListContainer";
import {ADMIN_PET, ADMIN_USER, ADMIN_EDIT_REQUEST} from "../../../constants/routes";
import useLogin from "../../../hooks/useLogin";
import {editAdminRequest} from "../../../api/admin/requests";
import moment from "moment";


function Request(
    {id, creationDate, updateDate, status, username, userId, petName, petId, modal, fetchFilters}) {
    const {t} = useTranslation("admin");

    const {jwt} = useLogin().state;

    const REQUEST_STATUS = {
        PENDING: 0,
        ACCEPTED: 1,
        REJECTED: 2,
        CANCELED: 3,
        SOLD: 4
    }

    const [requestStatus, setRequestStatus] = useState(status)

    let reqTarget = (
        <p>{t("requestsList.isInterested", {petName: petName, username: username})}
            <small className={"date-text"}> {moment(updateDate).format("DD/MM/YYYY")}</small>
            (id: {id})
        </p>

    );
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (requestStatus === REQUEST_STATUS.REJECTED) {
        const onConfirm = async () => {
            try{
                await editAdminRequest(REQUEST_STATUS.PENDING, id, jwt)
                fetchFilters();
                setRequestStatus(REQUEST_STATUS.PENDING);
            }catch (e){
                console.log(e)
            }
        }

        const modalMessage = t("modals.recoverRequest")

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
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )
    } else if (requestStatus === REQUEST_STATUS.PENDING) {
        const onConfirm = async () => {
            try{
                await editAdminRequest( REQUEST_STATUS.CANCELED, id, jwt)
                fetchFilters();
                setRequestStatus(REQUEST_STATUS.CANCELED);
            }catch (e){
                console.log(e)
            }
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

    } else if (requestStatus === REQUEST_STATUS.CANCELED) {
        const onConfirm = async () => {
            try{
                await editAdminRequest( REQUEST_STATUS.PENDING, id, jwt)
                fetchFilters();
                setRequestStatus(REQUEST_STATUS.PENDING);
            }catch (e){
                console.log(e)
            }
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

    } else if (requestStatus === REQUEST_STATUS.SOLD || requestStatus === REQUEST_STATUS.ACCEPTED) {
        const onConfirm = async () => {
            try{
                await editAdminRequest(REQUEST_STATUS.PENDING, id, jwt)
                fetchFilters();
                setRequestStatus(REQUEST_STATUS.PENDING);
            }catch (e){
                console.log(e)
            }
        }

        const modalMessage = t("modals.recoverRequest")

        reqStatus = <p>{t("status.sold")}</p>
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

function AdminRequestsContainer({requests, fetchFilters}) {
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
                                    <Request
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

export default AdminRequestsContainer;