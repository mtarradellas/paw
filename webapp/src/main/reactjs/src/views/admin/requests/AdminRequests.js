import React, {useState} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import AdminFilterRequestsForm from "./AdminFilterRequestsForm";
import {Button, Col, Divider, Modal, Pagination, Row} from "antd";
import AdminRequestsContainer from "./AdminRequestsContainer";

import {ADMIN_ADD_REQUEST} from "../../../constants/routes";

const request = {
    id: 0,
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "ACCEPTED",
    user: "pedro",
    userId: 4,
    pet: "Cato",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "JORGE",
    newPetOwnerId: 1

}

const request1 = {
    id: 42,
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "CANCELED",
    user: "pedro",
    userId: 4,
    pet: "nairobi",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "JORGE",
    newPetOwnerId: 1

}

const request2 = {
    id: 12,
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "REJECTED",
    user: "pedro",
    userId: 4,
    pet: "nairobi",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "JORGE",
    newPetOwnerId: 1

}

const request3 = {
    id: 414,
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "PENDING",
    user: "pedro",
    userId: 4,
    pet: "nairobi",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "JORGE",
    newPetOwnerId: 1

}

const request4 = {
    id: 20,
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "SOLD",
    user: "pedro",
    userId: 4,
    pet: "nairobi",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "JORGE",
    newPetOwnerId: 1

}

const sampleRequests = []
sampleRequests.push(request)
sampleRequests.push(request1)
sampleRequests.push(request2)
sampleRequests.push(request3)
sampleRequests.push(request4)

function SideContent() {
    return (<div>
        <AdminFilterRequestsForm/>
    </div>)
}

function MainContent({requests, requestsCount}) {
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
                        <h1><b>{t('requestsList.title', {count: requestsCount})}</b></h1>
                        <Button style={{marginTop: "0.5rem", marginLeft: "1rem"}} type={"primary"} href={ADMIN_ADD_REQUEST}>{t('addRequest')}</Button>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
            <Pagination defaultCurrent={1} total={50}/>
            <Row style={{margin: 0, padding: 0}}>
                <Col span={12}>
                    <h3><b>{t("requestsList.request")}</b></h3>
                </Col>
                <Col span={4}>
                    <h3><b>{t("requestsList.status")}</b></h3>
                </Col>
                <Col span={8}>
                    <div className={"centered"}>

                        <h3><b>{t("requestsList.actions")}</b></h3>
                    </div>
                </Col>
            </Row>
            <Divider style={{margin: 0, padding: 0}}/>
            <AdminRequestsContainer requests={requests}/>
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
    )
}

function AdminRequests() {
    const requests = sampleRequests;
    const requestsCount = sampleRequests.length;

    return (
        <ContentWithSidebar
            mainContent={
                <MainContent requests={requests} requestsCount={requestsCount}/>
            }
            sideContent={
                <SideContent/>
            }
        />
    )
}

export default AdminRequests;