import React, {useContext, useState, useEffect} from 'react';
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


function LoggedIn() {
    const {t} = useTranslation('header');
    const {state, logout} = useLogin();

    const [notifications, setNotifications] = useState({interests: null, requests: null});

    const {username, id, jwt} = state;

    const {interests, requests} = notifications;

    const _onLogout = () => {
        logout();
    };

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

            <Badge className={"header__subtitle"} count={_.isNil(requests) ? 0 : requests}>
                <Link to={REQUESTS}>
                    {t('requests')}
                </Link>
            </Badge>

            <Badge className={"header__subtitle"} count={_.isNil(interests) ? 0 : interests}>
                <Link to={INTERESTS}>
                    {t('interests')}
                </Link>
            </Badge>

            <Link className={"header__subtitle"} to={USER + id}>
                {t('profile')}
            </Link>

            <div className={"header__right header--session"}>
                <SearchBar/>

                <p className={"header--session--username"}>
                    {username}
                </p>

                <Button className={"header--session--logout"} onClick={_onLogout}>
                    {t('logout')}
                </Button>

            </div>
        </>
}

function NotLoggedIn(){
    const {t} = useTranslation('header');

    return <>
        <SearchBar/>

        <div className={"header__right"}>


                <Link className={"header__register"} to={REGISTER}>
                    {t('register')}
                </Link>

                <Link className={"header__login"} to={LOGIN}>
                    {t('login')}
                </Link>
        </div>
    </>
}

function SearchBar() {
    const history = useHistory();
    const {t} = useTranslation('header');

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

    return <Formik
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

function Common(){
    const {t} = useTranslation('header');

    return <>
        <Link className={"header__subtitle"} to={ADD_PET}>
            {t('addPet')}
        </Link>
        </>
}


function Header() {
    const {state} = useLogin();

    const {isLoggedIn} = state;

    return <header>
        <Link to={HOME}>
            <img className={"header__logo"} src={"/logo.png"} alt={"logo"} width={70} height={70}/>
        </Link>

        <Link to={HOME}>
            <span className={"header__title"}>
                PET SOCIETY
            </span>
        </Link>

        <Common/>
        {isLoggedIn ? <LoggedIn/> : <NotLoggedIn/>}

    </header>;
}

export default Header;