import React, {useState, useEffect, useCallback, useContext} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {Table, Button, Divider, List, Rate, Spin} from "antd";
import {useTranslation} from "react-i18next";
import PetCard from "../home/PetCard";
import {useParams, useHistory} from 'react-router-dom';
import '../../css/user/userView.css';
import {Link} from "react-router-dom";
import {HOME, LOGIN} from "../../constants/routes";
import {GET_USER_ERRORS, getUser} from "../../api/users";
import LoginContext from '../../constants/loginContext';

const sleep = (time) => {
    return new Promise(accept => {
        setTimeout(accept, time);
    });
};

const ListItem = List.Item;

const nairobi =  {
    id: 1,
    name: "Nairobi",
    specie: "Perro",
    breed: "Border Collie",
    price: 3000,
    sex: "Female",
    owner: "lenny",
    uploadDate: "05-05-2019",
    description: "Es muy gentil con los chicos, muy pacifica.",
    dateOfBirth: "05-05-2019",
    province: 'Buenos Aires',
    department: 'Departamento',
    vaccinated: true,
    onAdoption: false,
    onSale: true,
    ownerId: 1
};

const sampleReviews = [
    {username: 'jason', rating: 5, description: 'Excelente!', userId: 1},
    {username: 'juan', rating: 3.3, description: 'No tan excelente!', userId: 1}
];

function Content({user}){
    const {id, email} = user;

    const [avgRating, setAvgRating] = useState(null);
    const [reviewCount, setReviewsCount] = useState(null);
    const [pets, setPets] = useState(null);
    const [reviews, setReviews] = useState(null);

    const {t} = useTranslation('userView');

    const fetchRatingAndTotalReviews = useCallback(async ()=>{
        await sleep(5000);
        setAvgRating(5);
        setReviewsCount(1);
    }, [id]);

    const fetchPets = useCallback(async ()=> {
        await sleep(5000);
        setPets([nairobi, Object.assign({}, nairobi, {id: 2})]);
    }, [id]);

    const fetchReviews = useCallback(async ()=> {
        await sleep(5000);
        sampleReviews.forEach((review, index) => (Object.assign(review, {key: index})))
        setReviews(sampleReviews);
    }, [id]);

    useEffect(()=>{
        fetchRatingAndTotalReviews();
        fetchPets();
        fetchReviews();
    }, []);

    const reviewColumns = [
        {
            title: t('reviews.user'),
            dataIndex: 'username'
        },
        {
            title: t('reviews.rating'),
            dataIndex: 'rating',
            render: rating => (
                <Rate allowHalf disabled defaultValue={rating}/>
            )
        },
        {
            title: t('reviews.description'),
            dataIndex: 'description'
        }
    ];

    return <>
        <h1><b>
            {t('rating')}:</b>  {avgRating === null ?
                <Spin/>
                :
                reviewCount === 0 ?
                    t('noReviews')
                    :
                    <Rate allowHalf disabled defaultValue={avgRating}/>
            }
        </h1>

        <p>
            {
                reviewCount !== 0 && reviewCount !== null &&
                    '(' + t('average', {avgRating, reviewCount}) + ') '
            }
            {t('averageClarification')}
        </p>

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

        <h1><b>{t('petsTitle')}</b> {
            pets === null ?
                <Spin/>
                :
                '(' + t('totalResults', {count: pets.length}) + ')'
        }</h1>

        <div className={"user-view--pets-container"}>
            {
                pets === null ?
                    <Spin/>
                    :
                    pets.length > 0 ?
                        pets.map(pet => (<PetCard key={pet.id} pet={pet}/>))
                        :
                        <p>No tiene pets</p>
            }
        </div>

        {
            reviews !== null && reviews.length > 0 &&
                <>
                    <Divider/>

                    <h1><b>{t('reviewsTitle')}:</b></h1>

                    <Table
                        bordered={false}
                        columns={reviewColumns}
                        dataSource={reviews} size="small"
                    />
                </>
        }

        <Divider/>

        <div>
            <Link to={HOME}>
                {t('backToHome')}
            </Link>
        </div>

    </>
}

function UserView(){
    const {id} = useParams();
    const [user, setUser] = useState({username: null, email: null, id});
    const {state} = useContext(LoginContext);
    const history = useHistory();

    const {jwt, isLoggedIn} = state;

    if(!isLoggedIn)
        history.push(LOGIN);

    const fetchUser = useCallback(async ()=>{
        try{
            const result = await getUser(id, jwt);

            setUser(result);
        }catch (e) {
            switch (e) {
                case GET_USER_ERRORS.NOT_FOUND:
                    //TODO: redirect to not found
                    break;
                case GET_USER_ERRORS.CONN_ERROR:
                default:
                    //TODO: message error with retrying
                    break;
            }
        }
    }, [setUser, id]);

    useEffect(() => {
        if(isLoggedIn) fetchUser();
    }, []);

    const {username} = user;

    return <ContentWithHeader
            title={username ? username : <Spin/>}
            actionComponents={[
                <Button>Remover</Button>,
                <Button>Editar perfil</Button>
            ]}
            content={<Content user={user}/>}
        />
}

export default UserView;