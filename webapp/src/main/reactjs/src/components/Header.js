import React, {useContext, useState, useEffect, useRef} from 'react';
import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER, HOME, REQUESTS, INTERESTS, ADD_PET, USER} from "../constants/routes";
import {useTranslation} from "react-i18next";
import {Badge, Button, Col, Drawer, Row} from "antd";
import useLogin from "../hooks/useLogin";
import * as Yup from 'yup';
import {Formik} from "formik";
import {Form, Input} from "formik-antd";
import FilterAndSearchContext from "../constants/filterAndSearchContext";
import {useHistory} from 'react-router-dom';
import {getNotifications} from "../api/requests";
import _ from 'lodash';
import Logo from '../images/logo.png';
import DrawerIcon from '../images/drawer_icon.svg';
import {MenuOutlined, SearchOutlined} from "@ant-design/icons";


function LoggedInMenuItems() {
    const {t} = useTranslation('header');
    const {state} = useLogin();

    const [notifications, setNotifications] = useState({interests: null, requests: null});

    const {id, jwt} = state;

    const {interests, requests} = notifications;

    const fetchNotifications = async () => {
        try {
            const {interests, requests} = await getNotifications(jwt);

            setNotifications({interests, requests});
        } catch (e) {
            //TODO: conn error
        }
    };

    useEffect(() => {
        fetchNotifications()
    }, []);

    return <>

        <li className={"header__menu-items__item"}>
            <Badge className={"badge"} count={_.isNil(requests) ? 0 : requests}>
                <Link to={REQUESTS}>
                    {t('requests')}
                </Link>
            </Badge>
        </li>

        <li className={"header__menu-items__item"}>
            <Badge className={"badge"} count={_.isNil(interests) ? 0 : interests}>
                <Link to={INTERESTS}>
                    {t('interests')}
                </Link>
            </Badge>
        </li>

        <li className={"header__menu-items__item"}>
            <Badge className={"badge"} count={0}>
                    <Link to={USER + id}>
                        {t('profile')}
                    </Link>
            </Badge>
        </li>

    </>
}

function RegisterAndLogin() {
    const {t} = useTranslation('header');

    return <>
        <div>
            <Link className={"header__register"} to={REGISTER}>
                <strong>{t('register')}</strong>
            </Link>

            <Link className={"header__login"} to={LOGIN}>
                <strong>{t('login')}</strong>
            </Link>
        </div>
    </>
}

function UsernameAndLogout() {
    const {t} = useTranslation('header');
    const {state, logout} = useLogin();

    const {username} = state;

    const {id} = useLogin().state;

    const _onLogout = () => {
        logout();
    };

    return <>
        <Link className={"header__username"} to={USER + id}><strong>{username}</strong></Link>
        <Button onClick={_onLogout}>
            {t('logout')}
        </Button>
    </>
}

function SearchBar() {
    const history = useHistory();
    const {t} = useTranslation('header');
    const ref = useRef(null);

    const {
        onSubmitSearch,
        fetching,
        find
    } = useContext(FilterAndSearchContext);

    const onSubmit = values => {
        onSubmitSearch(values);
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
                <Button type="primary" htmlType="submit" loading={fetching} icon={<SearchOutlined/>}/>
            </Form.Item>
        </Form>
    </Formik>;
}

function Header() {
    const {t} = useTranslation('header');

    const {state, logout} = useLogin();

    const {isLoggedIn} = state;

    const [drawerVisible, setDrawerVisible] = useState(false);

    const onOpen = () => setDrawerVisible(true);

    const onClose = () => setDrawerVisible(false);

    const _onLogout = () => {
        logout();
    };


    return <header>
        <nav className={"desktop__nav"}>
            <ul className={"header__nav"}>
                <li>
                    <Link to={HOME} className={"header__logo"}>
                        <img src={Logo} alt={"logo"}/>
                    </Link>
                </li>
                <li>
                    <Link to={HOME} className={"header__title"}>
                        <span> PET SOCIETY </span>
                    </Link>
                </li>
                <li>
                    <div className={"header__menu-items__item"}>
                        <Badge className={"badge"} count={0}>
                            <Link to={ADD_PET}>
                                {t('addPet')}
                            </Link>
                        </Badge>
                    </div>
                </li>

                {isLoggedIn && <LoggedInMenuItems/>}

                <li className={"align__right searchbar"}>
                    <SearchBar/>
                </li>
                <li className={"align__right user__actions"}>
                    {isLoggedIn ? <UsernameAndLogout/> : <RegisterAndLogin/>}
                </li>
            </ul>
        </nav>
        <nav className={"phone__nav"}>
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
            drawerStyle={{background: '#222831'}}
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
                        <Badge className={"badge"} count={0}>
                            <Link to={ADD_PET}>
                                {t('addPet')}
                            </Link>
                        </Badge>
                    </div>
                </li>

                {isLoggedIn && <LoggedInMenuItems/>}

                {isLoggedIn ?
                    (
                        <li className={"header__menu-items__item"}>
                            <Link to="#" onClick={_onLogout && onClose}><strong>{t('logout')}</strong></Link>
                        </li>
                    )
                    :
                    (
                        <>
                            <li className={"header__menu-items__item"}>
                                <Link to={LOGIN} onClick={onClose}>
                                    <strong>{t('login')}</strong>
                                </Link>
                            </li>
                            <li className={"header__menu-items__item"}>
                                <Link to={REGISTER} onClick={onClose}>
                                    <strong>{t('register')}</strong>
                                </Link>
                            </li>
                        </>

                    )}
            </ul>

        </Drawer>
    </header>;
}

export default Header;