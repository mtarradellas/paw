import React, {useState} from 'react'
import {useTranslation} from "react-i18next";
import EditUsernameForm from "./EditUsernameForm"
import EditPasswordForm from "./EditPasswordForm"
import ContentWithHeader from '../../components/ContentWithHeader';
import {editUsername, EDIT_USERNAME_ERRORS} from '../../api/users';
import {editPassword, EDIT_PASSWORD_ERRORS} from '../../api/users';
import {deleteAccount} from '../../api/users';
import useLogin from "../../hooks/useLogin";
import {message, Button, Modal} from 'antd';

function EditUserView () {
    const {t} = useTranslation("EditUser");
    const {jwt} = useLogin().state;
    const {id} = useLogin().state;
    const {username} = useLogin().state;
    const {relog, logout} = useLogin();
    const [submittingUsername, setSubmittingUsername] = useState(false);
    const [submittingPassword, setSubmittingPassword] = useState(false);
    const [visible, setVisible] = React.useState(false);
    const [submittingRemove, setSubmittingRemove] = React.useState(false);
    const [modalText, setModalText] = React.useState(t('deleteModal'));
    const [maskClose, setMaskClose] = React.useState(true);
    const [iconClose, setIconClose] = React.useState(true);
    const [cancelProps, setCancelProps] = React.useState();
    
    const onEditUsername = async (username, {setErrors}) => {
        setSubmittingUsername(true);
        username = username.username;
        try {
            await editUsername({username, id}, jwt);
            relog();
        } catch (e) {
            switch (e) {
                case EDIT_USERNAME_ERRORS.DUPLICATED_USERNAME:
                    setErrors({username: t('usernameForm.username.errors.duplicated')});
                    break;
                default:
                    message.error(t('usernameForm.conError'));
                    break;
            }
        }

        setSubmittingUsername(false);
    }

    const onEditPassword = async ({oldPassword, newPassword}, {setErrors, resetForm}) => {
        setSubmittingPassword(true);
        
        try {
            await editPassword({oldPassword, newPassword, id}, jwt);
        } catch (e) {
            switch (e) {
                case EDIT_PASSWORD_ERRORS.WRONG_PASS:
                    setErrors({oldPassword: t('passwordForm.oldPassword.errors.wrong')});
                    break;
                default:
                    message.error(t('passwordForm.conError'));
                    break;
            }
            setSubmittingPassword(false);
            return;
        }

        resetForm();
        setSubmittingPassword(false);
        message.success(t('passwordForm.success'));
    }

    const showModal = () => {
        setVisible(true);
    }

    const handleOk = async () => {
        setMaskClose(false);
        setIconClose(false);
        setCancelProps({disabled: true});
        setModalText(t('deletingModal'));
        setSubmittingRemove(true);
        try {
            await deleteAccount(id, jwt);
        } catch (e) {
            setSubmittingRemove(false);
            message.error(t('deleteError'));
            return;
        }
        setSubmittingRemove(false);
        logout();
    }

    const handleCancel = () => {
        setVisible(false);
    }

    return (
        <ContentWithHeader 
        title={t('title')}
        actionComponents={<>
            <Button danger={true} type={"primary"} onClick={showModal}>{t('delete')}</Button>
            <Modal
              title={t('delete')}
              visible={visible}
              cancelButtonProps={cancelProps}
              onOk={handleOk}
              okType='danger'
              okText={t('deleteConfirmBtn')}
              confirmLoading={submittingRemove}
              onCancel={handleCancel}
              closable={iconClose}
              maskClosable={maskClose}
            >
              <p>{modalText}</p>
            </Modal>
            
        </>}
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