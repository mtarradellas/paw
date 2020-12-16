import React, {useState, useEffect, useCallback} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button, Divider, List, Rate, Spin} from "antd";
import {useTranslation} from "react-i18next";
import {useParams, useHistory} from 'react-router-dom';
import '../../css/user/userView.css';
import {Link} from "react-router-dom";
import {HOME, ERROR_404_USER} from "../../constants/routes";
import {GET_USER_ERRORS, getUser} from "../../api/users";
import useLogin from "../../hooks/useLogin";
import useReviewsPagination from "../../hooks/useReviewsPagination";
import Reviews from "./Reviews";
import OwnedPets from "./OwnedPets";
import MakeAReviewForm from "./MakeAReviewForm";
import MakeAReview from "./MakeAReview";


const ListItem = List.Item;


function Content({user, id}){
    const {email} = user;

    const reviewsPagination = useReviewsPagination({userId: id});

    const {t} = useTranslation('userView');

    return <>
        <h1><b>
            {t('rating')}:</b>  {reviewsPagination.average === null ?
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
                    '(' + t('average', {rating: reviewsPagination.average, reviewCount: reviewsPagination.amount}) + ') '
            }
            {t('averageClarification')}
        </p>

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
                    t('sensibleInformation')
        }

        <Divider/>

        <OwnedPets userId={id}/>

        //TODO: pets bought by user

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
        fetchUser();
    }, [fetchUser]);

    const {username} = user;

    return <ContentWithHeader
            title={username ? username : <Spin/>}
            actionComponents={
                loggedUserId === id ?
                    [
                        <Button key={"remove"}>Remover</Button>,
                        <Button key={"edit"}>Editar perfil</Button>
                    ]
                    :
                    []
            }
            content={<Content user={user} id={id}/>}
        />
}

export default UserView;