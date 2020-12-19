import React, {useContext} from 'react';
import '../css/header.css';
import {Link} from "react-router-dom";
import {LOGIN, REGISTER, HOME, REQUESTS, INTERESTS, ADD_PET, USER} from "../constants/routes";
import {useTranslation} from "react-i18next";
import {Button} from "antd";
import useLogin from "../hooks/useLogin";
import * as Yup from 'yup';
import {Formik} from "formik";
import {Form, Input} from "formik-antd";
import FilterAndSearchContext from "../constants/filterAndSearchContext";
import {useHistory} from 'react-router-dom';


function LoggedIn(){
    const {t} = useTranslation('header');
    const {state, logout} = useLogin();

    const {username, id} = state;

    const _onLogout = () => {
        logout();
    };

    return <>

            <Link className={"header__subtitle"} to={REQUESTS}>
                {t('requests')}
            </Link>

            <Link className={"header__subtitle"} to={INTERESTS}>
                {t('interests')}
            </Link>

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

            <div className={"header__right"}>
                <SearchBar/>

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