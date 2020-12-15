import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";

import {ADMIN_USER, ADMIN_PET, ADMIN_EDIT_PET} from "../../../constants/routes";
import {Link} from "react-router-dom";
import ListContainer from "../../../components/ListContainer";


function Pet({id, name, ownerId, status, modal}){
    const {t} = useTranslation("admin");

    let reqTarget = (

        <p> <img width={"70px"} alt="example" src="http://pawserver.it.itba.edu.ar/paw-2020a-7/img/1" />
            {name} (id: {id})</p>

    );
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (status === "AVAILABLE") {
        const onConfirm = () => {
            alert("deleted" + id)
        }

        const modalMessage = t("modals.deletePet")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + id}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + ownerId}>{t("buttons.visitOwner")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_PET + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
            </div>
        )
    }else if (status === "UNAVAILABLE") {
        shaded = true
        const onConfirm = () => {
            alert("deleted" + id)
        }

        const modalMessage = t("modals.deletePet")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + id}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + ownerId}>{t("buttons.visitOwner")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_PET + id}>{t("buttons.edit")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
            </div>
        )
    } else if (status === "SOLD" || status === "REMOVED") {
        shaded = true
        const onConfirm = () => {
            alert("recovered" + id)
        }

        const modalMessage = t("modals.recoverPet")

        reqButtons = (
            <div className={"button-container"}>
                <Button type={"primary"} href={ADMIN_PET + id}>{t("buttons.visitPet")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_USER + ownerId}>{t("buttons.visitOwner")}</Button>
                &nbsp;&nbsp;
                <Button type={"primary"} href={ADMIN_EDIT_PET + id}>{t("buttons.edit")}</Button>
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


function AdminPetsContainer({pets}){
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
                    pets
                        .map(
                            (pet) => (
                                <List.Item key={pet.id}>
                                    <Pet modal={showModal} {...pet} />
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

export default AdminPetsContainer;