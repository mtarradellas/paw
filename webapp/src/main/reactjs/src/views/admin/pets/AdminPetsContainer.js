import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";
import {Link} from 'react-router-dom';

import {ADMIN_USER, ADMIN_PET, ADMIN_EDIT_PET} from "../../../constants/routes";
import ListContainer from "../../../components/ListContainer";
import useLogin from "../../../hooks/useLogin";
import {recoverPetAdmin, removePetAdmin} from "../../../api/admin/pets";


function Pet({id, petName, userId, status, modal, fetchFilters}){
    const {t} = useTranslation("admin");

    const statusLocale = [
        t("status.available"),
        t("status.removed"),
        t("status.sold"),
        t("status.unavailable")
    ]

    const {jwt} = useLogin().state;

    const PET_STATUS = {
        AVAILABLE: 0,
        REMOVED: 1,
        SOLD: 2,
        UNAVAILABLE: 3
    }

    const [petStatus, setPetStatus] = useState(status)

    let reqTarget = (

        <p>
            &nbsp;&nbsp;{petName} (id: {id}, {statusLocale[petStatus]})</p>

    );

    let reqStatus = null;
    let reqButtons = null;

    if (petStatus === PET_STATUS.AVAILABLE || petStatus === PET_STATUS.UNAVAILABLE) {
        const onConfirm = async () => {
            try{
                await removePetAdmin(id, jwt)
                setPetStatus(PET_STATUS.REMOVED);
                fetchFilters();
            }catch (e){
                console.log(e)
            }
        }
        const modalMessage = t("modals.deletePet")

        reqButtons = (
            <div className={"button-container"}>
                <Link to={ADMIN_PET + id}>
                    <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Link to={ADMIN_USER + userId}>
                    <Button type={"primary"}>{t("buttons.visitOwner")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Link to={ADMIN_EDIT_PET + id}>
                    <Button type={"primary"}>{t("buttons.edit")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Button type={"primary"} danger
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
            </div>
        )
    }else if (petStatus === PET_STATUS.SOLD || petStatus === PET_STATUS.REMOVED) {
        const onConfirm = async () => {
            try{
                await recoverPetAdmin(id, jwt)
                setPetStatus(PET_STATUS.AVAILABLE);
                fetchFilters();
            }catch (e){
                console.log(e)
            }
        }

        const modalMessage = t("modals.recoverPet")

        reqButtons = (
            <div className={"button-container"}>
                <Link to={ADMIN_PET + id}>
                    <Button type={"primary"}>{t("buttons.visitPet")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Link to={ADMIN_USER + userId}>
                    <Button type={"primary"}>{t("buttons.visitOwner")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Link to={ADMIN_EDIT_PET + id}>
                    <Button type={"primary"}>{t("buttons.edit")}</Button>
                </Link>
                &nbsp;&nbsp;
                <Button type={"primary"} style={{background: 'limegreen', borderColor: 'limegreen'}}
                        onClick={() => modal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
            </div>
        )
    }

    return (
        <ListContainer target={reqTarget} status={reqStatus} buttons={reqButtons} shaded={false}/>

    )

}


function AdminPetsContainer({pets, fetchFilters}){
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
                                    <Pet
                                        modal={showModal}
                                        fetchFilters={fetchFilters}
                                        {...pet} />
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