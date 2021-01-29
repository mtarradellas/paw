import React, {useState, useCallback, useEffect} from 'react';
import {useParams, useHistory, Link} from 'react-router-dom';
import useLogin from "../../hooks/useLogin";
import {GET_USER_ERRORS} from "../../api/users";
import {ADMIN_EDIT_USER, ADMIN_HOME, ERROR_404_USER} from "../../constants/routes";
import {deleteUserAdmin, getUserAdmin, recoverUserAdmin} from "../../api/admin/users";
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button, Divider, List, Modal, Rate, Spin} from "antd";
import {useTranslation} from "react-i18next";
import useReviewsPagination from "../../hooks/useReviewsPagination";
import MakeAReview from "../user/MakeAReview";
import OwnedPets from "../user/OwnedPets";
import Reviews from "../user/Reviews";

const ListItem = List.Item;


function Content({user, id, email}) {

    const {state, promptLogin} = useLogin();

    const reviewsPagination = useReviewsPagination({userId: id});

    const {t} = useTranslation('userView');

    const {jwt} = state;

    return <>
        <h1><b>
            {t('rating')}:</b> {reviewsPagination.average === null ?
            <Spin/>
            :
            reviewsPagination.amount === 0 ?
                t('noReviews')
                :
                <Rate allowHalf disabled defaultValue={reviewsPagination.average}/>
        }
        </h1>

        <p>
            {
                reviewsPagination.amount !== 0 && reviewsPagination.amount !== null &&
                '(' + t('average', {
                    rating: (Math.floor(reviewsPagination.average * 10) / 10),
                    reviewCount: reviewsPagination.amount
                }) + ') '
            }
            {t('averageClarification')}
        </p>

        <MakeAReview userId={id} refreshReviews={reviewsPagination.refresh}/>

        <Divider/>

        {<List bordered={true}>
            <ListItem>
                <b>{t('email')}:</b> {email}
            </ListItem>
            <ListItem>
                <b>Id:</b> {id}
            </ListItem>
        </List>
        }

        <Divider/>

        <OwnedPets admin={true} userId={id} title={'offeredPetsTitle'} filters={{ownerId: id}}/>

        <OwnedPets admin={true} userId={id} title={'adoptedPetsTitle'} filters={{newOwnerId: id}}/>

        <Divider/>

        <div>
            <Link to={ADMIN_HOME}>
                {t('backToHome')}
            </Link>
        </div>

    </>
}

function AdminUserView() {
    const {t} = useTranslation('adminUserView');

    const id = parseInt(useParams().id)
    const [user, setUser] = useState({username: null, email: null, id, status: null});
    const {state, promptLogin} = useLogin();
    const history = useHistory();

    const {jwt} = state;

    const fetchUser = useCallback(async () => {
        try {
            const result = await getUserAdmin(id, jwt)
            setUser(result);
        } catch (e) {
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
    }, [setUser, id, history, jwt]);

    useEffect(() => {
        fetchUser();
    }, [fetchUser]);

    const statusLocale = [
        t("status.active"),
        t("status.inactive"),
        t("status.deleted")
    ]

    const {username} = user;

    const [modalState, setModalState] = useState({show: false, callbackMethod: null, modalMessage: ""});
    const showModal = (callback, message) => {
        setModalState({show: true, callbackMethod: callback, modalMessage: message});
    };
    const onOk = () => {
        modalState.callbackMethod()
        setModalState(false);
    };
    const onCancel = () => {
        setModalState(false);
    };

    let modalButton;
    if (user.status === 0 || user.status === 1) {
        const onConfirm = async () => {
            try {
                await deleteUserAdmin(id, jwt)
                fetchUser();
            } catch (e) {
                console.log(e)
            }
        }
        const modalMessage = t("modals.removeUser")

        modalButton = <Button type={"primary"} danger key={"remove"}
                              onClick={() => showModal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
    } else {
        const onConfirm = async () => {
            try {
                await recoverUserAdmin(id, jwt)
                fetchUser()
            } catch (e) {
                console.log(e)
            }
        }

        const modalMessage = t("modals.recoverUser")
        modalButton = <Button type={"primary"} danger key={"recover"}
                              onClick={() => showModal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
    }

    return <ContentWithHeader
        title={username ? username + " (" + statusLocale[user.status] + ")" : <Spin/>}
        actionComponents={
            [
                modalButton,
                <Link to={ADMIN_EDIT_USER + id}>
                    <Button key={"edit"}>{t("buttons.edit")}</Button>
                </Link>
            ]
        }
        content={
            <div>
                <Content user={user} id={id} email={user.mail}/>
                <Modal
                    title={t("modals.pleaseConfirm")}
                    visible={modalState.show}
                    onCancel={onCancel}
                    footer={[
                        <div key={"confirmation-modal-key"}>
                            <Button key="cancel" onClick={onCancel}>
                                {t("buttons.cancel")}
                            </Button>
                            <Button key="submit" type="primary" onClick={onOk}>
                                {t("buttons.imSure")}
                            </Button>
                        </div>
                    ]}
                >
                    <div>
                        {modalState.modalMessage}
                    </div>
                </Modal>
            </div>
        }
    />
}

export default AdminUserView;