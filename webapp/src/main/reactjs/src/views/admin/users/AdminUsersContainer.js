import React, {useState} from 'react';
import {useTranslation} from "react-i18next";
import {Button, List, Modal} from "antd";

import {ADMIN_USER, ADMIN_EDIT_USER} from "../../../constants/routes";
import ListContainer from "../../../components/ListContainer";
import useLogin from "../../../hooks/useLogin";
import {deleteUserAdmin, recoverUserAdmin} from "../../../api/admin/users";


function User({id, username,status, modal, fetchFilters}){
    const {t} = useTranslation("admin");

    const {jwt} = useLogin().state;

    const USER_STATUS = {
        ACTIVE: 0,
        INACTIVE: 1,
        DELETED: 2
    }

    const [userStatus, setUserStatus] = useState(status)

    let reqTarget = (
        <p>{username} (id: {id})</p>

    );
    let reqStatus = null;
    let reqButtons = null;
    let shaded = false;

    if (userStatus === USER_STATUS.ACTIVE || userStatus === USER_STATUS.INACTIVE) {
        const onConfirm = async () => {
            try{
                await deleteUserAdmin(id, jwt)
                fetchFilters();
                setUserStatus(USER_STATUS.DELETED);
            }catch (e){
                console.log(e)
            }
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
     } else if (userStatus === USER_STATUS.DELETED) {
        shaded = true;
        const onConfirm = async () => {
            try{
                await recoverUserAdmin(id, jwt)
                fetchFilters();
                setUserStatus(USER_STATUS.ACTIVE);
            }catch (e){
                console.log(e)
            }
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

function AdminUsersContainer({users,fetchFilters}){
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
                                    <User
                                        modal={showModal}
                                        fetchFilters={fetchFilters}
                                        {...user} />
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