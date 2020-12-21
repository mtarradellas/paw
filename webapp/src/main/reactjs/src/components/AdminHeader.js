import React, {useContext, useEffect, useRef} from 'react';

import {Link, useHistory} from "react-router-dom";
import {useTranslation} from "react-i18next";
import {ADMIN_HOME, LOGIN, REGISTER, ADMIN_REQUESTS, ADMIN_PETS, ADMIN_USERS, HOME} from "../constants/routes";
import {Button} from "antd";
import useLogin from "../hooks/useLogin";

import '../css/admin/admin.css'
import FilterAndSearchContext from "../constants/filterAndSearchContext";
import {Formik} from "formik";
import * as Yup from "yup";
import {Form, Input} from "formik-antd";


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

function AdminHeader() {
    const {t} = useTranslation('admin');

    const {state, logout} = useLogin();

    const {username, id} = state;

    const _onLogout = () => {
        logout();
    };

    return (
        <header className={"admin-header"}>
            <Link to={ADMIN_HOME} className={"header__logo"} >
                <img src={"/logo.png"} alt={"logo"}/>
            </Link>

            <Link to={ADMIN_HOME} className={"header__title"}>
            <span>
                PET SOCIETY
            </span>
            </Link>

            <div className={"header__menu-items"}>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_REQUESTS}>
                        {t('listRequests')}
                    </Link>
                </div>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_USERS}>
                        {t('listUsers')}
                    </Link>
                </div>
                <div className={"header__menu-items__item"}>
                    <Link to={ADMIN_PETS}>
                        {t('listPets')}
                    </Link>
                </div>

            </div>

            <div className={"header__search-bar"}>
                <SearchBar/>
            </div>

            <div className={"header__username-and-logout"}>
                <p className={"header__username-and-logout__username"}>
                    {username}(Admin)
                </p>

                <Button className={"header__username-and-logout__logout"} onClick={_onLogout}>
                    {t('logout')}
                </Button>

            </div>
        </header>
    )
}

export default AdminHeader;