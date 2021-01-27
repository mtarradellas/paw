import React, {useEffect, useState} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import {Button, Col, Divider, Modal, Pagination, Row, Spin} from "antd";
import {ADMIN_ADD_PET, ADMIN_PETS, ADMIN_REQUESTS} from "../../../constants/routes";

import AdminFilterPetsForm from "./AdminFilterPetsForm";
import AdminPetsContainer from "./AdminPetsContainer";
import useLogin from "../../../hooks/useLogin";
import useAdminPets from "../../../hooks/admin/usePets";
import _ from "lodash";
import {getAdminPetsFilters} from "../../../api/admin/pets";
import {Link} from "react-router-dom";

function SideContent({filters, changeFilters, setCurrentPage, fetchAdminPets, fetchFilters}) {
    return <AdminFilterPetsForm
        filters={filters}
        changeFilters={changeFilters}
        setCurrentPage={setCurrentPage}
        fetchAdminPets={fetchAdminPets}
        fetchFilters={fetchFilters}
    />
}

function MainContent({
                         pets,
                         petCount,
                         fetching,
                         pages,
                         pageSize,
                         fetchPage,
                         currentPage,
                         setCurrentPage,
                         fetchFilters
                     }) {
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

    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPage(newValue);
    };

    return (
        <div>
            <Row style={{margin: 0, padding: 0}}>
                <Col span={23}>
                    <Row>
                        <h1><b>
                            {
                                !_.isNil(petCount) && t('petsList.title', {count: petCount})
                            }
                        </b>
                        </h1>
                        <Link to={ADMIN_ADD_PET}>
                            <Button
                                style={{marginTop: "0.5rem", marginLeft: "1rem"}}
                                type={"primary"}>{t('addPet')}</Button>
                        </Link>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
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
            {
                petCount === 0 ?
                    (<p>{t("noResults")} <Link to={ADMIN_PETS} onClick={() => window.location.reload()}>{t("fetchAll")}</Link></p>)
                    :
                    (_.isNil(pets) || fetching ?
                        <Spin/>
                        :
                        <AdminPetsContainer pets={pets} fetchFilters={fetchFilters}/>)
            }
            <Divider orientation={"left"}>
                {
                    pageSize && petCount !== 0  &&
                    <Pagination showSizeChanger={false} current={currentPage} total={petCount} pageSize={pageSize}
                                onChange={_onChangePagination}/>
                }
            </Divider>
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
    const {adminPets, fetchAdminPets, fetching, pages, amount, pageSize} = useAdminPets();

    const {jwt} = useLogin().state;

    const [appliedFilters, setAppliedFilters] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);

    const fetchPage = page => {
        const params = {
            ...appliedFilters,
            page
        }
        fetchAdminPets(params)
    };

    const [filters, setFilters] = useState(null);
    const fetchFilters = async chosenFilters => {
        try {
            const newFilters = await getAdminPetsFilters(chosenFilters, jwt);
            setFilters(newFilters);
        } catch (e) {
            //TODO: conn error
        }
    };

    useEffect(() => {
        fetchFilters({});
    }, []);

    return (
        <ContentWithSidebar
            mainContent={
                <MainContent
                    pets={adminPets}
                    petCount={amount}
                    fetching={fetching}
                    pages={pages}
                    pageSize={pageSize}
                    fetchPage={fetchPage}
                    currentPage={currentPage}
                    setCurrentPage={setCurrentPage}
                    fetchFilters={fetchFilters}
                />
            }
            sideContent={
                <SideContent
                    filters={filters}
                    changeFilters={setAppliedFilters}
                    setCurrentPage={setCurrentPage}
                    fetchAdminPets={fetchAdminPets}
                    fetchFilters={fetchFilters}
                />
            }
        />
    )
}

export default AdminPets;