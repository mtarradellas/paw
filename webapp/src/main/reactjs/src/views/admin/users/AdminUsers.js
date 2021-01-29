import React, {useState, useEffect} from 'react'
import {useTranslation} from "react-i18next";

import ContentWithSidebar from "../../../components/ContentWithSidebar";
import {Button, Col, Divider, Modal, Pagination, Row, Spin} from "antd";

import AdminFilterUsersForm from "./AdminFilterUsersForm";

import {ADMIN_ADD_USER, ADMIN_REQUESTS, ADMIN_USERS} from "../../../constants/routes";
import AdminUsersContainer from "./AdminUsersContainer";
import useAdminUsers from "../../../hooks/admin/useUsers";
import useLogin from "../../../hooks/useLogin";
import _ from "lodash";
import {getAdminUsersFilters} from "../../../api/admin/users";
import {Link} from "react-router-dom";

function SideContent({filters, changeFilters, setCurrentPage, fetchAdminUsers}) {
    return <AdminFilterUsersForm
        filters={filters}
        changeFilters={changeFilters}
        setCurrentPage={setCurrentPage}
        fetchAdminUsers={fetchAdminUsers}
    />
}

function MainContent({
                         users,
                         userCount,
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
                                !_.isNil(userCount) && t('usersList.title', {count: userCount})
                            }
                        </b>
                        </h1>
                        <Link to={ADMIN_ADD_USER}>
                            <Button
                                style={{marginTop: "0.5rem", marginLeft: "1rem"}}
                                type={"primary"}>{t('addUser')}</Button>
                        </Link>
                    </Row>
                </Col>
                <Col>
                    <Button type="primary" shape="circle" size={"large"} onClick={showModal}>?</Button>
                </Col>
            </Row>
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
            {
                userCount === 0 ?
                    (<p>{t("noResults")} <Link to={ADMIN_USERS} onClick={() => window.location.reload()}>{t("fetchAll")}</Link></p>)
                    :
                    (_.isNil(users) || fetching ?
                        <Spin/>
                        :
                        <AdminUsersContainer users={users} fetchFilters={fetchFilters}/>)
            }
            <Divider orientation={"left"}>
                {
                    pageSize && userCount &&
                    <Pagination showSizeChanger={false} current={currentPage} total={userCount} pageSize={pageSize}
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

function AdminUsers() {
    const {adminUsers, fetchAdminUsers, fetching, pages, amount, pageSize} = useAdminUsers();

    const {jwt} = useLogin().state;

    const [appliedFilters, setAppliedFilters] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);

    const fetchPage = page => {
        const params = {
            ...appliedFilters,
            page
        }
        fetchAdminUsers(params)
    };


    const [filters, setFilters] = useState(null);
    const fetchFilters = async () => {
        try {
            const newFilters = await getAdminUsersFilters(jwt);

            setFilters(newFilters);
        } catch (e) {
            //TODO: conn error
        }
    };

    useEffect(() => {
        fetchFilters();
    }, []);


    return (
        <ContentWithSidebar
            mainContent={
                <MainContent
                    users={adminUsers}
                    userCount={amount}
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
                    fetchAdminUsers={fetchAdminUsers}
                />
            }
        />
    )
}

export default AdminUsers;