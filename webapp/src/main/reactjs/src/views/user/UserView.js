import React, {useState, useEffect, useCallback} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button, Divider, List, Rate, Spin} from "antd";
import {useTranslation} from "react-i18next";
import {useParams, useHistory, useLocation} from 'react-router-dom';
import '../../css/user/userView.css';
import {Link} from "react-router-dom";
import {HOME, ERROR_404_USER, EDIT_USER} from "../../constants/routes";
import {GET_MAIL_ERRORS, GET_USER_ERRORS, getMail, getUser} from "../../api/users";
import useLogin from "../../hooks/useLogin";
import useReviewsPagination from "../../hooks/useReviewsPagination";
import Reviews from "./Reviews";
import OwnedPets from "./OwnedPets";
import MakeAReview from "./MakeAReview";


const ListItem = List.Item;


function Content({user, id}) {
    const [email, setEmail] = useState(null);

    const {state, promptLogin} = useLogin();

    const reviewsPagination = useReviewsPagination({userId: id});

    const {t} = useTranslation('userView');

    const {jwt} = state;

    const fetchEmail = async () => {
        try{
            const mail = await getMail({userId: id}, jwt);

            setEmail(mail);
        }catch (e) {
            switch (e) {
                case GET_MAIL_ERRORS.NOT_ALLOWED:
                    setEmail(false);
                    break;
                case GET_MAIL_ERRORS.FORBIDDEN:
                    promptLogin();
                    break;
                case GET_MAIL_ERRORS.CONN_ERROR:
                default:
                    //TODO: message error with retrying
                    break;
            }
        }
    };

    useEffect(()=>{
        setEmail(null);
        fetchEmail()
    }, [id]);

    return <>
        {
            reviewsPagination.average === null ? <Spin/> :
            reviewsPagination.amount === 0 ? <p><i>{t('noReviews')}</i></p> :
            <>
                <h2><b>{t('rating')}:</b>  <Rate allowHalf disabled defaultValue={reviewsPagination.average}/></h2>
                <p>
                    {
                        reviewsPagination.amount !== 0 && reviewsPagination.amount !== null &&
                            '(' + t('average', {rating: (Math.floor(reviewsPagination.average * 10) / 10), reviewCount: reviewsPagination.amount}) + ') '
                    }
                    {t('averageClarification')}
                </p>
            </>
        }

        <MakeAReview userId={id} refreshReviews={reviewsPagination.refresh}/>

        <Divider/>

        {
            email === null ?
                <Spin/>
                :
                email ?
                    <List bordered={true}>
                        <ListItem>
                            <b>{t('email')}:</b> {email}
                        </ListItem>
                    </List>
                    :
                    <i>{t('sensibleInformation')}</i>
        }

        <Divider/>

        <OwnedPets paramKey={"offeredPetsPage"} userId={id} title={'offeredPetsTitle'} error={'offeredPetsError'} filters={{ownerId: id}}/>

        <Divider/>

        <OwnedPets paramKey={"adoptedPetsPage"} userId={id} title={'adoptedPetsTitle'} error={'adoptedPetsError'} filters={{newOwnerId: id}}/>

        <Divider/>

        <Reviews userId={id} reviewsPagination={reviewsPagination}/>

        <Divider/>

        <div>
            <Link to={HOME}>
                {t('backToHome')}
            </Link>
        </div>

    </>
}

function UserView(){
    const id = parseInt(useParams().id);
    const [user, setUser] = useState({username: null, email: null, id});
    const {state, promptLogin} = useLogin();
    const history = useHistory();
    const {t} = useTranslation('userView');

    const {jwt, id: loggedUserId} = state;

    const fetchUser = useCallback(async ()=>{
        try{
            const result = await getUser(id, jwt);

            setUser(result);
        }catch (e) {
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
        setUser({username: null, email: null, id});
        fetchUser();
    }, [fetchUser]);

    const {username} = user;

    return <ContentWithHeader
            title={username ? username : <Spin/>}
            actionComponents={
                (loggedUserId === id) && <Link to={EDIT_USER}><Button key={"edit"}>{t('edit')}</Button></Link>
            }
            content={<Content user={user} id={id}/>}
        />
}

export default UserView;