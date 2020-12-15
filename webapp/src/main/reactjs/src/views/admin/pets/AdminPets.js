import React, {useState} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import {Button, Col, Divider, Modal, Pagination, Row} from "antd";
import {ADMIN_ADD_PET} from "../../../constants/routes";

import AdminFilterPetsForm from "./AdminFilterPetsForm";
import AdminPetsContainer from "./AdminPetsContainer";
const dog = {
    id: 0,
    name: "Nairobi",
    specie: "Dog",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    ownerId: 0,
    uploadDate: "05-05-2019",
    status: "AVAILABLE"
};

const dog2 = {
    id: 1,
    name: "Balto",
    specie: "Dog",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "asdad",
    ownerId: 4,
    uploadDate: "05-05-2019",
    status: "REMOVED"
};

const dog3 = {
    id: 2,
    name: "Skeri",
    specie: "Dog",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    ownerId: 0,
    uploadDate: "05-05-2019",
    status: "SOLD"

};

const dog1 = {
    id: 3,
    name: "Hawk",
    specie: "Dog",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    ownerId: 32,
    uploadDate: "05-05-2019",
    status: "UNAVAILABLE"
};

const samplePets = [];
samplePets.push(dog);
samplePets.push(dog1);
samplePets.push(dog2);
samplePets.push(dog3);

function SideContent() {
    return <AdminFilterPetsForm/>
}

function MainContent({pets, petsCount}) {
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
                        <h1><b>{t('petsList.title', {count: petsCount})}</b></h1>
                        <Button style={{marginTop: "0.5rem", marginLeft: "1rem"}} type={"primary"}
                                href={ADMIN_ADD_PET}>{t('addPet')}</Button>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
            <Pagination defaultCurrent={1} total={50}/>
            <Row style={{margin: 0, padding: 0}}>
                <Col span={12}>
                    <h3><b>{t("petsList.pet")}</b></h3>
                </Col>
                <Col span={4}>
                </Col>
                <Col span={8}>
                    <div className={"centered"}>

                        <h3><b>{t("petsList.actions")}</b></h3>
                    </div>
                </Col>
            </Row>
            <Divider style={{margin: 0, padding: 0}}/>
            <AdminPetsContainer pets={pets}/>
            <Divider/>
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

function AdminPets() {
    const pets = samplePets;
    const petsCount = samplePets.length;

    return (
        <ContentWithSidebar
            mainContent={
                <MainContent pets={pets} petsCount={petsCount}/>
            }
            sideContent={
                <SideContent/>
            }
        />
    )
}

export default AdminPets;