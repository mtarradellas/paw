import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";

import {ADMIN_USER, ADMIN_EDIT_USER} from "../../../constants/routes";
import ListContainer from "../../../components/ListContainer";


function User({id, username, mail,status, modal}){
    const {t} = useTranslation("admin");

    let reqTarget = (
        <p>{username} - {mail}  (id: {id})</p>

    );
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (status === "ACTIVE") {
        const onConfirm = () => {
            alert("deleted" + id)
        }

        const modalMessage = t("modals.deleteUser")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_USER + id}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_USER + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
            </div>
        )
     } else if (status === "INACTIVE") {
        shaded = true;
        const onConfirm = () => {
            alert("deleted" + id)
        }

        const modalMessage = t("modals.deleteUser")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_USER + id}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_USER + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
            </div>
        )
    } else if (status === "DELETED") {
        shaded = true;
        const onConfirm = () => {
            alert("recovered" + id)
        }

        const modalMessage = t("modals.recoverUser")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_USER + id}>{t("buttons.visitUser")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_USER + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )

    }    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={shaded}/>

    )}

function AdminUsersContainer({users}){
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
                    users
                        .map(
                            (user) => (
                                <List.Item key={user.id}>
                                    <User modal={showModal} {...user} />
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
    )}

export default AdminUsersContainer;