import React, {useState, useEffect} from "react";
import {Button, Modal, Row, Col, Divider, Pagination, Spin} from 'antd';
import {useTranslation} from "react-i18next";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import FilterRequestsForm from "./FilterRequestsForm";
import RequestContainer from "./RequestContainer";
import "../../css/requests&interests/requests-interests.css"
import useRequests from "../../hooks/useRequests";
import _ from "lodash";
import {getRequestsFilters} from "../../api/requests";
import useLogin from "../../hooks/useLogin";
import {HOME} from "../../constants/routes";
import {Link, useLocation, useHistory} from "react-router-dom";
import queryString from "query-string";

function SideContent({filters, fetchRequests, changeFilters, setCurrentPage, initialFilters}) {
    return (<div>
        <FilterRequestsForm
            filters={filters}
            fetchRequests={fetchRequests}
            changeFilters={changeFilters}
            setCurrentPage={setCurrentPage}
            initialFilters={initialFilters}
        />
    </div>)
}

function MainContent(
    {requestsCount, requests, fetching, pages, pageSize, fetchPage, fetchFilters, currentPage, setCurrentPage}
) {
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

    const _onChangePagination = newValue => {
        setCurrentPage(newValue);

        fetchPage(newValue);
    };

    return (<>{
        fetching ? <Spin/> :
            requestsCount === 0 ? <div className={"requests-interests__empty"}>
                    <div>{t('noResultsText')}</div>
                    <Link to={HOME}><Button type='primary'>{t('noResultsBtn')}</Button></Link>
                </div> :
                <div>
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
                            <RequestContainer requests={requests} fetchFilters={fetchFilters}/>
                    }

                    <Divider orientation={"left"}>
                        {
                            pageSize && requestsCount && requestsCount > pageSize &&
                            <Pagination showSizeChanger={false} current={currentPage} total={requestsCount}
                                        pageSize={pageSize}
                                        onChange={_onChangePagination}/>
                        }
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
                </div>}</>)
}

function parseQuery(location) {
    const params = queryString.parse(location.search);

    return Object.assign(params, {page: parseInt(params.page || 1)})
}

function RequestsView() {
    const location = useLocation();
    const history = useHistory();
    const {jwt} = useLogin().state;

    const params = parseQuery(location);

    const [appliedFilters, setAppliedFilters] = useState(Object.assign({
        searchCriteria: "date",
        searchOrder: "desc"
    }, params));
    const {requests, fetching, fetchRequests, pages, amount, pageSize} = useRequests(appliedFilters);

    const [currentPage, setCurrentPage] = useState(params.page);

    const onSetAppliedFilters = filters => {
        setAppliedFilters(filters);

        history.replace({search: '?' + queryString.stringify(Object.assign({}, filters, {page: 1}))});
    };

    const onSetPage = page => {
        setCurrentPage(page);

        history.replace({search: '?' + queryString.stringify(Object.assign({}, appliedFilters, {page}))});
    };

    const fetchPage = page => {
        const params = {
            ...appliedFilters,
            page
        }
        fetchRequests(params)
    };

    const [filters, setFilters] = useState(null);
    const fetchFilters = async () => {
        try {
            const newFilters = await getRequestsFilters(jwt);

            setFilters(newFilters);
        } catch (e) {
            //TODO: conn error
        }
    };

    useEffect(() => {
        fetchFilters();
    }, []);

    return <ContentWithSidebar
        sideContent={
            <SideContent
                filters={filters}
                fetchRequests={fetchRequests}
                changeFilters={onSetAppliedFilters}
                setCurrentPage={onSetPage}
                initialFilters={appliedFilters}
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
                currentPage={currentPage}
                setCurrentPage={onSetPage}
            />
        }
    />;
}

export default RequestsView;