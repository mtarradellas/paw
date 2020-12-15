import React, {useState} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import {Button, Col, Divider, Modal, Pagination, Row} from "antd";

import AdminFilterUsersForm from "./AdminFilterUsersForm";

import {ADMIN_ADD_USER} from "../../../constants/routes";
import AdminUsersContainer from "./AdminUsersContainer";

const user = {
    id:0,
    username: "Manu",
    mail: "manu@manu.com",
    status: "ACTIVE"
}

const user1 = {
    id:1,
    username: "Pedro",
    mail: "pedro@pedro.com",
    status: "INACTIVE"
}

const user2 = {
    id:2,
    username: "Lu",
    mail: "lu@lu.com",
    status: "ACTIVE"
}

const user3 = {
    id:3,
    username: "Facu",
    mail: "facu@facu.com",
    status: "DELETED"
}

const sampleUsers = [];
sampleUsers.push(user);
sampleUsers.push(user1);
sampleUsers.push(user2);
sampleUsers.push(user3);

function SideContent(){
    return <AdminFilterUsersForm/>
}

function MainContent({users, userCount}){
    const {t} = useTranslation('admin');

    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    return (
        <div>
            <Row style={{margin: 0, padding: 0}}>
                <Col span={23}>
                    <Row>
                        <h1><b>{t('usersList.title', {count: userCount})}</b></h1>
                        <Button style={{marginTop: "0.5rem", marginLeft: "1rem"}} type={"primary"} href={ADMIN_ADD_USER}>{t('addUser')}</Button>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
            <Pagination defaultCurrent={1} total={50}/>
            <Row style={{margin: 0, padding: 0}}>
                <Col span={12}>
                    <h3><b>{t("usersList.user")}</b></h3>
                </Col>
                <Col span={4}>
                </Col>
                <Col span={8}>
                    <div className={"centered"}>

                        <h3><b>{t("usersList.actions")}</b></h3>
                    </div>
                </Col>
            </Row>
            <Divider style={{margin: 0, padding: 0}}/>
            <AdminUsersContainer users={users}/>
            <Divider />
            <Pagination defaultCurrent={1} total={50}/>
            <Modal
                title={t("modals.help.title")}
                visible={isModalVisible}
                onCancel={handleCancel}
                footer={[
                    <Button key="submit" type="primary" onClick={handleOk}>
                        {t("buttons.close")}
                    </Button>
                ]}
            >
                <div>
                    <h2>{t("modals.help.myRole")} </h2>
                    <p>{t("modals.help.myRoleDesc")}</p>
                    <h2>{t("modals.help.meaning")}</h2>
                    <p>{t("modals.help.meaningDesc")}</p>
                </div>
            </Modal>
        </div>
    )}

function AdminUsers(){
    const users = sampleUsers;
    const userCount = sampleUsers.length;

    return (
        <ContentWithSidebar
            mainContent={
                <MainContent users={users} userCount={userCount}/>
            }
            sideContent={
                <SideContent/>
            }
        />
    )
}

export default AdminUsers;