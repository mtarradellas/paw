import React, {useContext, useState, useEffect, useRef} from 'react';
import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER, HOME, REQUESTS, INTERESTS, ADD_PET, USER} from "../constants/routes";
import {useTranslation} from "react-i18next";
import {Badge, Button} from "antd";
import useLogin from "../hooks/useLogin";
import * as Yup from 'yup';
import {Formik} from "formik";
import {Form, Input} from "formik-antd";
import FilterAndSearchContext from "../constants/filterAndSearchContext";
import {useHistory} from 'react-router-dom';
import {getNotifications} from "../api/requests";
import _ from 'lodash';
import Logo from '../images/logo.png';


function LoggedInMenuItems() {
    const {t} = useTranslation('header');
    const {state} = useLogin();

    const [notifications, setNotifications] = useState({interests: null, requests: null});

    const {id, jwt} = state;

    const {interests, requests} = notifications;

    const fetchNotifications = async () => {
        try{
            const {interests, requests} = await getNotifications(jwt);

            setNotifications({interests, requests});
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(() => {
        fetchNotifications()
    }, []);

    return <>

        <div className={"header__menu-items__item"}>
            <Badge count={_.isNil(requests) ? 0 : requests}>
                <Link to={REQUESTS}>
                    {t('requests')}
                </Link>
            </Badge>
        </div>

        <div className={"header__menu-items__item"}>
            <Badge count={_.isNil(interests) ? 0 : interests}>
                <Link to={INTERESTS}>
                    {t('interests')}
                </Link>
            </Badge>
        </div>

        <div className={"header__menu-items__item"}>
            <Link to={USER + id}>
                {t('profile')}
            </Link>
        </div>

    </>
}

function RegisterAndLogin(){
    const {t} = useTranslation('header');

    return <>
        <div className={"header__register-and-login"}>
            <Link className={"header__register"} to={REGISTER}>
                {t('register')}
            </Link>

            <Link className={"header__login"} to={LOGIN}>
                {t('login')}
            </Link>
        </div>
    </>
}

function UsernameAndLogout(){
    const {t} = useTranslation('header');
    const {state, logout} = useLogin();

    const {username} = state;

    const {id} = useLogin().state;

    const _onLogout = () => {
        logout();
    };

    return <>
        <div className={"header__username-and-logout"}>

            <p className={"header__username-and-logout__username"}>
                <Link to={USER + id}>{username}</Link>
            </p>

            <Button className={"header__username-and-logout__logout"} onClick={_onLogout}>
                {t('logout')}
            </Button>

        </div>
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

        if(history.location !== HOME)
            history.push(HOME);
    };

    useEffect(()=>{
        if(ref.current){
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
                    <Button type="primary" htmlType="submit" loading={fetching}>
                        {t('searchButton')}
                    </Button>
                </Form.Item>
            </Form>
    </Formik>;
}

function Header() {
    const {t} = useTranslation('header');

    const {state} = useLogin();

    const {isLoggedIn} = state;

    return <header>
        <Link to={HOME} className={"header__logo"}>
            <img src={Logo} alt={"logo"} width={70} height={70}/>
        </Link>

        <Link to={HOME} className={"header__title"}>
            <span>
                PET SOCIETY
            </span>
        </Link>

        <div className={"header__menu-items"}>
            <div className={"header__menu-items__item"}>
                <Link to={ADD_PET}>
                    {t('addPet')}
                </Link>
            </div>

            {isLoggedIn && <LoggedInMenuItems/>}
        </div>

        <div className={"header__search-bar"}>
            <SearchBar/>
        </div>

        {isLoggedIn ? <UsernameAndLogout/> : <RegisterAndLogin/>}

    </header>;
}

export default Header;