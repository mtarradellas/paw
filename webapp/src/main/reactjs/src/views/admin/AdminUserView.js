import React, {useState, useCallback, useEffect} from 'react';
import {useParams, useHistory} from 'react-router-dom';
import useLogin from "../../hooks/useLogin";
import {GET_USER_ERRORS, getUser} from "../../api/users";
import {ERROR_404_USER} from "../../constants/routes";
import {getUserAdmin} from "../../api/admin/users";
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button, Spin} from "antd";
import {useTranslation} from "react-i18next";


function AdminUserView(){
    const {t} = useTranslation('adminUserView');

    const id = parseInt(useParams().id)
    const [user,setUser] = useState({username:null, email:null,id});
    const {state, promptLogin} = useLogin();
    const history = useHistory();

    const {jwt, id: loggedUserId} = state;

    const fetchUser = useCallback(async () => {
        try{
            const result = await getUserAdmin(id, jwt)
            setUser(result);
        }catch(e){
            switch (e) {
                case GET_USER_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_USER);
                    break;
                case GET_USER_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case GET_USER_ERRORS.CONN_ERROR:
                default:
                    //TODO: message error with retrying
                    break;
            }
        }
    }, [setUser,id,history,jwt]);

    useEffect(()=>{
        fetchUser();
    },[fetchUser]);

    const {username} = user;

    return <ContentWithHeader
        title={username ? username : <Spin/>}
        actionComponents={
            loggedUserId === id ?
                [
                    <Button key={"remove"}>remove</Button>,
                    <Button key={"edit"}>{t("edit")}</Button>
                ]
                :
                []
        }
        content={<div>gola</div>}
        // content={<Content user={user} id={id}/>}
    />}

export default AdminUserView;