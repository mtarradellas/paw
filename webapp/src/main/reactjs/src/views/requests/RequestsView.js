import React, {useState} from "react";
import {Button, Modal, List, Row, Col, Divider, Pagination} from 'antd';

import {useTranslation, Trans} from "react-i18next";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterRequestsForm from "./FilterRequestsForm";

import RequestNotification from "./RequestNotification";

import "../../css/requests/requests.css"

/*
*  REQUEST STATUSES: ACCEPTED, REJECTED, PENDING, CANCELED, SOLD
*  PET STATUSES: AVAILABLE, REMOVED, SOLD, UNAVAILABLE
* */

const request = {
    creationDate: "02-03-2020",
    updateDate: "05-05-2020",
    status: "PENDING",
    user: "pedro",
    userId: 4,
    pet: "nairobi",
    petId: 41,
    petStatus: "AVAILABLE",
    newPetOwner: "",
    newPetOwnerId: 0

}

const sampleRequests = []
for (let i = 0; i < 14; i++) {
    sampleRequests.push(request)
}

function SideContent() {
    return <div>
        <FilterRequestsForm/>
    </div>
}

function MainContent({requests, requestsCount}) {
    const {t} = useTranslation("requests");

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

    return <div>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={23}>
                <h1>{t("requests.title", {count: requestsCount})}</h1>
            </Col>
            <Col>
                <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
            </Col>
        </Row>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={14}>
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
        <div className={"requests-container"}>
            <List split={false}>
                {
                    requests
                        .map(
                        (request) => (
                            <List.Item>
                                <RequestNotification {...request}/>
                            </List.Item>
                        )
                    )
                }
            </List>

        </div>
        <Divider orientation={"left"}>
            <Pagination defaultCurrent={1} total={50}/>
        </Divider>

        <Modal
            title="Help"
            visible={isModalVisible}
            onCancel={handleCancel}
            footer={[
                <Button key="submit" type="primary" onClick={handleOk}>
                    Close
                </Button>
            ]}
        >
            <div>
                <h2>{t("modal.firstTitle")} </h2>
                <p>{t("modal.firstDesc")}</p>
                <h2>{t("modal.secondTitle")}</h2>
                <p>{t("modal.secondDesc")}</p>
            </div>
        </Modal>
    </div>
}


function RequestsView() {
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