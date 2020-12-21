import React, {useEffect, useState} from "react";
import ContentWithSidebar from "../../components/ContentWithSidebar";
import {useTranslation} from "react-i18next";
import "../../css/requests&interests/requests-interests.css"
import FilterInterestsForm from "./FilterInterestsForm";
import {Button, Col, Divider, Modal, Pagination, Row, Spin} from "antd";
import InterestContainer from "./InterestContainer";
import useInterests from "../../hooks/useInterests";
import _ from "lodash";
import useLogin from "../../hooks/useLogin";
import {getInterestsFilters} from "../../api/interests";
import {ADD_PET} from "../../constants/routes";
import {Link} from "react-router-dom";

function SideContent({filters,fetchInterests,changeFilters,setCurrentPage,fetchFilters}) {
    return (<div>
        <FilterInterestsForm
            filters={filters}
            fetchInterests={fetchInterests}
            changeFilters={changeFilters}
            setCurrentPage={setCurrentPage}
            fetchFilters={fetchFilters}
        />
    </div>)
}

function MainContent(
    {interestsCount, interests, fetching, pages, pageSize, fetchPage, fetchFilters, currentPage, setCurrentPage}
) {
    const {t} = useTranslation('interests');

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

    return (<>{ fetching ? <Spin/> : 
        interestsCount === 0 ? <div className={"requests-interests__empty"}> 
        <div>{t('noResultsText')}</div> 
        <Link to={ADD_PET}><Button type='primary'>{t('noResultsBtn')}</Button></Link>
    </div> :
        <div>
        <Row style={{margin: 0, padding: 0}}>
            <Col span={23}>
                <h1><b>
                    {
                        !_.isNil(interestsCount) && t("interests.title", {count: interestsCount})
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
                <h3><b>{t("interests.interest")}</b></h3>
            </Col>
            <Col span={4}>
                <h3><b>{t("interests.status")}</b></h3>
            </Col>
            <Col span={8}>
                <div className={"centered"}>

                    <h3><b>{t("interests.actions")}</b></h3>
                </div>
            </Col>
        </Row>
        <Divider style={{margin: 0, padding: 0}}/>
        {
            _.isNil(interests) || fetching ?
                <Spin/>
                :
                <InterestContainer interests={interests} fetchFilters={fetchFilters} />
        }        <Divider orientation={"left"}>
            {
                pageSize && interestsCount &&
                <Pagination showSizeChanger={false} current={currentPage} total={interestsCount} pageSize={pageSize}
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

function InterestsView() {
    const {interests, fetching, fetchInterests, pages, amount, pageSize} = useInterests();
    const {jwt} = useLogin().state;

    const [appliedFilters, setAppliedFilters] = useState({searchCriteria:"date", searchOrder:"desc"});
    const [currentPage, setCurrentPage] = useState(1);

    const fetchPage = page => {
        const params = {
            ...appliedFilters,
            page
        }
        fetchInterests(params)
    };

    const [filters, setFilters] = useState(null);
    const fetchFilters = async params => {
        try{
            const newFilters = await getInterestsFilters(params,jwt);

            setFilters(newFilters);
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchFilters({});
    }, []);

    return <ContentWithSidebar
        sideContent={
            <SideContent
                filters={filters}
                fetchInterests={fetchInterests}
                changeFilters={setAppliedFilters}
                setCurrentPage={setCurrentPage}
                fetchFilters={fetchFilters}
            />
        }
        mainContent={
            <MainContent
                interestsCount={amount}
                interests={interests}
                fetching={fetching}
                pages={pages}
                pageSize={pageSize}
                fetchPage={fetchPage}
                fetchFilters={fetchFilters}
                currentPage={currentPage}
                setCurrentPage={setCurrentPage}
            />
        }
    />;
}

export default InterestsView;