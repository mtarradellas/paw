import React, {useState, useEffect} from "react";
import {Button, Modal, Row, Col, Divider, Pagination, Spin} from 'antd';

import {useTranslation} from "react-i18next";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterRequestsForm from "./FilterRequestsForm";

import RequestContainer from "./RequestContainer";

import "../../css/requests&interests/requests-interests.css"

import useRequests from "../../hooks/useRequests";
import _ from "lodash";

import {getRequestsFilter} from "../../api/requests";
import useLogin from "../../hooks/useLogin";

/*
*  REQUEST STATUSES: ACCEPTED, REJECTED, PENDING, CANCELED, SOLD
*  PET STATUSES: AVAILABLE, REMOVED, SOLD, UNAVAILABLE
* */

function SideContent({filters,fetchRequests}) {
    return (<div>
        <FilterRequestsForm
            filters={filters}
            fetchRequests={fetchRequests}
        />
    </div>)
}

function MainContent({requestsCount, requests, fetching, pages, pageSize, fetchPage, fetchFilters}) {
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

    const [currentPage, setCurrentPage] = useState(1);
    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPage(newValue);
    };

    const reloadPage = () => {
        fetchPage(currentPage);
    }

    return (<div>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={23}>
                <h1><b>
                    {
                        !_.isNil(requestsCount) && t("requests.title", {count: requestsCount})
                    }
                </b>
                </h1>
            </Col>
            <Col>
                <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
            </Col>
        </Row>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={12}>
                <h3><b>{t("requests.request")}</b></h3>
            </Col>
            <Col span={4}>
                <h3><b>{t("requests.requestStatus")}</b></h3>
            </Col>
            <Col span={8}>
                <div className={"centered"}>

                    <h3><b>{t("requests.actions")}</b></h3>
                </div>
            </Col>
        </Row>
        <Divider style={{margin: 0, padding: 0}}/>
        {
            _.isNil(requests) || fetching ?
                <Spin/>
                :
                <RequestContainer requests={requests} reloadPage={reloadPage} fetchFilters={fetchFilters}/>
        }

        <Divider orientation={"left"}>
            {
                pageSize && requestsCount &&
                <Pagination showSizeChanger={false} current={currentPage} total={requestsCount} pageSize={pageSize}
                            onChange={_onChangePagination}/>
            }        </Divider>
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


function RequestsView() {
    const {requests, fetching, fetchRequests, pages, amount, pageSize} = useRequests();

    const fetchPage = page => {
        fetchRequests({page})
    };

    const {jwt} = useLogin().state;
    const [filters, setFilters] = useState(null);

    const fetchFilters = async () => {
        try{
            const newFilters = await getRequestsFilter(jwt);

            setFilters(newFilters);
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchFilters();
    }, []);

    return <ContentWithSidebar
        sideContent={
            <SideContent
                filters={filters}
                fetchRequests={fetchRequests}
            />
        }
        mainContent={
            <MainContent
                requestsCount={amount}
                requests={requests}
                fetching={fetching}
                pages={pages}
                pageSize={pageSize}
                fetchPage={fetchPage}
                fetchFilters={fetchFilters}
            />
        }
    />;
}

export default RequestsView;