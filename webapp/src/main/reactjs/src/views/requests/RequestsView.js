import React, {useState} from "react";
import {Button, Modal, Row, Col, Divider, Pagination} from 'antd';

import {useTranslation} from "react-i18next";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterRequestsForm from "./FilterRequestsForm";

import RequestContainer from "./RequestContainer";

import "../../css/requests/requests.css"

/*
*  REQUEST STATUSES: ACCEPTED, REJECTED, PENDING, CANCELED, SOLD
*  PET STATUSES: AVAILABLE, REMOVED, SOLD, UNAVAILABLE
* */

const request = {
    id: 0,
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

const sampleRequests = []
for (let i = 0; i < 15; i++) {
    let aux = {...request}
    aux.id = i
    sampleRequests.push(aux)
}

function SideContent() {
    return (<div>
        <FilterRequestsForm/>
    </div>)
}

function MainContent({requests, requestsCount}) {
    const {t} = useTranslation('requests');

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

    return (<div>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={23}>
                <h1><b>{t("requests.title", {count: requestsCount})}</b></h1>
            </Col>
            <Col>
                <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
            </Col>
        </Row>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={12}>
                <h3><b>{t("requests.request")}</b></h3>
            </Col>
            <Col span={7}>
                <h3><b>{t("requests.requestStatus")}</b></h3>
            </Col>
            <Col>
                <h3><b>{t("requests.actions")}</b></h3>
            </Col>
        </Row>
        <Divider style={{margin: 0, padding: 0}}/>
        <RequestContainer requests={requests}/>
        <Divider orientation={"left"}>
            <Pagination defaultCurrent={1} total={50}/>
        </Divider>
        <Modal
            title={t("modals.helpModal.title")}
            visible={isModalVisible}
            onCancel={handleCancel}
            footer={[
                <Button key="submit" type="primary" onClick={handleOk}>
                    {t("buttons.close")}
                </Button>
            ]}
        >
            <div>
                <h2>{t("modals.helpModal.firstTitle")} </h2>
                <p>{t("modals.helpModal.firstDesc")}</p>
                <h2>{t("modals.helpModal.secondTitle")}</h2>
                <p>{t("modals.helpModal.secondDesc")}</p>
            </div>
        </Modal>
    </div>)
}


function RequestsView()
{
    const requests = sampleRequests;
    const requestsCount = sampleRequests.length;

    return <ContentWithSidebar
        sideContent={
            <SideContent/>
        }
        mainContent={
            <MainContent requestsCount={requestsCount} requests={requests}/>
        }
    />;
}

export default RequestsView;