import React, {useState, useEffect} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import AdminFilterRequestsForm from "./AdminFilterRequestsForm";
import {Button, Col, Divider, Modal, Pagination, Row, Spin} from "antd";
import AdminRequestsContainer from "./AdminRequestsContainer";

import {ADMIN_ADD_REQUEST} from "../../../constants/routes";
import useAdminRequests from "../../../hooks/admin/useRequests";
import useLogin from "../../../hooks/useLogin";
import _ from "lodash";
import {getAdminRequestsFilters} from "../../../api/admin/requests";


function SideContent({filters, changeFilters, setCurrentPage, fetchAdminRequests}) {
    return (<div>
        <AdminFilterRequestsForm
            filters={filters}
            changeFilters={changeFilters}
            setCurrentPage={setCurrentPage}
            fetchAdminRequests={fetchAdminRequests}
        />
    </div>)
}

function MainContent(
    {requestsCount, requests, fetching, pages, pageSize, fetchPage, currentPage, setCurrentPage, fetchFilters}
    ) {
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
                                !_.isNil(requestsCount) && t('requestsList.title', {count: requestsCount})
                            }
                        </b>
                        </h1>
                        <Button style={{marginTop: "0.5rem", marginLeft: "1rem"}} type={"primary"} href={ADMIN_ADD_REQUEST}>{t('addRequest')}</Button>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
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
            {
                _.isNil(requests) || fetching ?
                    <Spin/>
                    :
                    <AdminRequestsContainer requests={requests} fetchFilters={fetchFilters}/>
            }
            <Divider orientation={"left"}>
                {
                    pageSize && requestsCount &&
                    <Pagination showSizeChanger={false} current={currentPage} total={requestsCount} pageSize={pageSize}
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

function AdminRequests() {
    const {adminRequests,fetchAdminRequests,fetching, pages, amount, pageSize} = useAdminRequests();

    const {jwt} = useLogin().state;

    const [appliedFilters, setAppliedFilters] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);

    const fetchPage = page => {
        const params = {
            ...appliedFilters,
            page
        }
        fetchAdminRequests(params)
    };

    const [filters, setFilters] = useState(null);
    const fetchFilters = async () => {
        try{
            const newFilters = await getAdminRequestsFilters(jwt);

            setFilters(newFilters);
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchFilters();
    },[]);


    return (
        <ContentWithSidebar
            mainContent={
                <MainContent
                    requestsCount={amount}
                    requests={adminRequests}
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
                    fetchAdminRequests={fetchAdminRequests}
                />
            }
        />
    )
}

export default AdminRequests;