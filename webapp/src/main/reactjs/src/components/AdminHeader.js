import React, {useContext, useEffect, useRef, useState} from 'react';

import {Link, useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {
    ADMIN_HOME,
    ADMIN_REQUESTS,
    ADMIN_PETS,
    ADMIN_USERS,
    ADMIN_USER,
    HOME,
    ADD_PET,
    LOGIN, REGISTER
} from "../constants/routes";
import {Badge, Button, Col, Drawer, Row} from "antd";
import useLogin from "../hooks/useLogin";

import '../css/admin/admin.css'
import FilterAndSearchContext from "../constants/filterAndSearchContext";
import {Formik} from "formik";
import * as Yup from "yup";
import {Form, Input} from "formik-antd";

import Logo from '../images/logo.png'
import {MenuOutlined, SearchOutlined} from "@ant-design/icons";


function SearchBar() {
    const history = useHistory();
    const {t} = useTranslation('header');
    const ref = useRef(null);

    const {
        onSubmitSearch,
        find
    } = useContext(FilterAndSearchContext);

    const onSubmit = values => {
        onSubmitSearch(values);

        if (history.location !== ADMIN_PETS)
            history.push(ADMIN_PETS);
    };

    useEffect(() => {
        if (ref.current) {
            ref.current.setFieldValue('find', find);
        }
    }, [find]);

    return <Formik
        innerRef={ref}
        onSubmit={onSubmit}
        initialValues={
            {
                find
            }
        }
        validationSchema={
            Yup.object().shape({
                find: Yup.string()
            })
        }
        validateOnBlur={false}
        validateOnChange={false}
    >
        <Form layout={'inline'}>
            <Form.Item name={'find'}>
                <Input name={'find'} placeholder={t('searchPlaceholder')} allowClear/>
            </Form.Item>

            <Form.Item name>
                <Button type="primary" htmlType="submit" icon={<SearchOutlined/>}/>
            </Form.Item>
        </Form>
    </Formik>;
}

function AdminHeader() {
    const {t} = useTranslation('admin');

    const {state, logout} = useLogin();

    const {username, id} = state;

    const {isLoggedIn} = state;

    const [drawerVisible, setDrawerVisible] = useState(false);

    const onOpen = () => setDrawerVisible(true);

    const onClose = () => setDrawerVisible(false);

    const _onLogout = () => {
        logout();
    };


    return (
        <header className={"admin__header"}>
            <nav className={"desktop__nav"}>
                <ul className={"header__nav"}>
                    <li>
                        <Link to={ADMIN_HOME} className={"header__logo"}>
                            <img src={Logo} alt={"logo"}/>
                        </Link>
                    </li>
                    <li>
                        <Link to={ADMIN_HOME} className={"header__title"}>
                            <span>PS ADMIN</span>
                        </Link>
                    </li>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_REQUESTS}>
                                {t('listRequests')}
                            </Link>
                        </div>
                    </li>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_USERS}>
                                {t('listUsers')}
                            </Link>
                        </div>
                    </li>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_PETS}>
                                {t('listPets')}
                            </Link>
                        </div>
                    </li>

                    <li className={"align__right searchbar"}>
                        <SearchBar/>
                    </li>

                    <li className={"align__right user__actions"}>
                        <div>
                            <Link className={"header__username"} to={ADMIN_USER + id}><strong>{username}</strong></Link>
                            <Button onClick={_onLogout}>
                                {t('logout')}
                            </Button>
                        </div>
                    </li>
                </ul>
            </nav>
            <nav className={"admin__header phone__nav"}>
                <Row>
                    <Col span={4}>
                        <Link to={HOME} className={"header__logo"}>
                            <img src={Logo} alt={"logo"}/>
                        </Link>
                    </Col>
                    <Col span={15}>
                        <div className={"searchbar"}>
                            <SearchBar/>
                        </div>
                    </Col>
                    <Col span={5}>
                        <Button icon={<MenuOutlined/>} className={"header__drawer"} onClick={onOpen}/>
                    </Col>
                </Row>
            </nav>
            <Drawer
                drawerStyle={{background: '#2b5a5d'}}
                key="left"
                placement="left"
                visible={drawerVisible}
                onClose={onClose}
                closable={true}
                title={
                    <div style={{display: 'block', fontSize: '1.2rem'}}>
                        <img src={Logo} alt={"logo"} width={35} height={35}/>
                        <span> PET SOCIETY </span>
                    </div>
                }>
                <ul className={"drawer__nav"}>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_REQUESTS}>
                                {t('listRequests')}
                            </Link>
                        </div>
                    </li>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_USERS}>
                                {t('listUsers')}
                            </Link>
                        </div>
                    </li>
                    <li>
                        <div className={"header__menu-items__item"}>
                            <Link to={ADMIN_PETS}>
                                {t('listPets')}
                            </Link>
                        </div>
                    </li>

                    <li className={"header__menu-items__item"}>
                        <Link to="#" onClick={_onLogout}><strong>{t('logout')}</strong></Link>
                    </li>

                </ul>

            </Drawer>

        </header>
    )
}

export default AdminHeader;