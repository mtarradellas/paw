import React, {useState} from 'react'
import {useTranslation} from "react-i18next";
import EditUsernameForm from "./EditUsernameForm"
import EditPasswordForm from "./EditPasswordForm"
import ContentWithHeader from '../../components/ContentWithHeader';
import {editUsername, EDIT_USERNAME_ERRORS} from '../../api/users';
import {editPassword, EDIT_PASSWORD_ERRORS} from '../../api/users';
import useLogin from "../../hooks/useLogin";
import {message} from 'antd';

function EditUserView () {
    const {t} = useTranslation("EditUser");
    const {jwt} = useLogin().state;
    const {id} = useLogin().state;
    const {username} = useLogin().state;
    const {relog} = useLogin();
    const [submittingUsername, setSubmittingUsername] = useState(false);
    const [submittingPassword, setSubmittingPassword] = useState(false);
    
    const onEditUsername = async (username, {setErrors}) => {
        setSubmittingUsername(true);
        username = username.username;
        try {
            await editUsername({username, id}, jwt);
            relog();
        } catch (e) {
            switch (e) {
                case EDIT_USERNAME_ERRORS.DUPLICATED_USERNAME:
                    setErrors({username: t('form.username.errors.duplicated')});
                    break;
                default:
                    message.error(t('form.conError'));
                    break;
            }
        }

        setSubmittingUsername(false);
    }

    const onEditPassword = async ({oldPassword, newPassword}, {setErrors}) => {
        setSubmittingPassword(true);
        
        try {
            await editPassword({oldPassword, newPassword, id}, jwt);
        } catch (e) {
            switch (e) {
                case EDIT_PASSWORD_ERRORS.WRONG_PASS:
                    setErrors({username: t('form.password.errors.wrong')});
                    break;
                default:
                    message.error(t('form.conError'));
                    break;
            }
        }

        setSubmittingPassword(false);
    }

    return (
        <ContentWithHeader 
        title={t('title')}
        content={
            <div>
                <EditUsernameForm oldname={username} onSubmit={onEditUsername} submitting={submittingUsername}/>
                <hr/>
                <EditPasswordForm onSubmit={onEditPassword} submitting={submittingPassword}/>
            </div>
        }/>
    )
}

export default EditUserView;