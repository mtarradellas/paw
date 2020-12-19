import React, {useEffect, useState, useContext} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {useTranslation} from "react-i18next";
import {Button, Divider, List, Modal, Spin} from "antd";
import Questions from "./Questions";
import {Link} from "react-router-dom";
import {EDIT_PET, ERROR_404_PET, HOME, USER} from "../../constants/routes";
import _ from 'lodash';
import {DELETE_PET_ERRORS, getPet, deletePet as deletePetApi} from "../../api/pets";
import {useParams} from 'react-router-dom';
import {petImageSrc} from "../../api/images";
import ConstantsContext from '../../constants/constantsContext';
import {CloseOutlined, CheckOutlined} from '@ant-design/icons';
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';

const ListItem = List.Item;

function isMale(sex){
    return _.toLower(sex) === 'male';
}

function ListItemRow({name, value}){
    return <ListItem>
        <b>{name}</b>: {_.isNil(value) ? <Spin/> : value}
    </ListItem>;
}

function Content({pet, id}){
    const {t} = useTranslation('petView');

    const {breeds, species, provinces, departments} = useContext(ConstantsContext);

    const {
        petName,
        birthDate,
        gender,
        vaccinated,
        price,
        uploadDate,
        description,
        username,
        userId,
        speciesId,
        breedId,
        provinceId,
        departmentId,
        images
    } = pet;

    return <>
            {
                _.isNil(description) ?
                    <Spin/>
                    :
                    <>
                        <img alt="example" src={petImageSrc(images[0])}/>

                        <p>{description}</p>
                    </>
            }


            <Divider/>

            {
                price === 0 ?
                    <h2>{t('status.onAdoption')}</h2>
                    :
                    <h2>{t('status.onSale')}: ${price}</h2>
            }

            <Divider/>


            <h2>
                { isMale(gender) ? t('someInformationAboutHim') : t('someInformationAboutHer') }:
            </h2>

            <List bordered={true}>
                <ListItemRow name={t('details.name')} value={petName}/>
                <ListItemRow name={t('details.dateOfBirth')} value={birthDate}/>
                <ListItemRow name={t('details.specie')} value={speciesId && species[speciesId].name}/>
                <ListItemRow name={t('details.breed')} value={breedId && breeds[breedId].name}/>
                <ListItemRow name={t('details.sex')} value={t('details.' + _.toLower(gender))}/>
                <ListItemRow name={t('details.province')} value={provinceId && provinces[provinceId].name}/>
                <ListItemRow name={t('details.department')} value={departmentId && departments[departmentId].name}/>
                <ListItemRow name={t('details.uploadDate')} value={uploadDate}/>
                <ListItemRow name={t('details.owner')} value={<Link to={USER + userId}>{username}</Link>}/>
                <ListItemRow name={t('status.vaccinated')}
                             value={
                                 vaccinated ?
                                     <CheckOutlined />
                                     :
                                     <CloseOutlined />
                             }
                />
            </List>

            <Divider/>

            <h2>{t('questions.header')}:</h2>

            <Questions petId={id} ownerId={userId}/>

            <Divider/>

            <div>
                <Link to={USER + userId}>
                    <Button>{t('goToOwnersPage')}</Button>
                </Link>
            </div>

            <div>
                <Link to={HOME}>
                    {t('backToHome')}
                </Link>
            </div>
        </>;
}

function IsOwnerButtons({petId, petName}){
    const history = useHistory();
    const {promptLogin, state} = useLogin();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    const {t} = useTranslation('petView');

    const {id: userId, jwt} = state;

    const deletePet = async () => {
        setSubmitting(true);
        try {
            await deletePetApi({petId}, jwt);

            history.push(USER + userId);
        }catch (e) {
            switch (e) {
                case DELETE_PET_ERRORS.NOT_LOGGED_IN:
                    promptLogin();
                    break;
                case DELETE_PET_ERRORS.NO_PERMISSION:
                    history.push(HOME);
                    break;
                case DELETE_PET_ERRORS.NOT_FOUND:
                    history.push(ERROR_404_PET);
                    break;
                default:
                //TODO: con error
                    setSubmitting(false);
            }
        }

    };

    return <>
            <Button onClick={() => setIsModalVisible(true)} loading={submitting}>{t('buttons.deletePet')}</Button>
            <Link to={EDIT_PET(petId)}>
                <Button>{t('buttons.editPet')}</Button>
            </Link>

            <Modal
                title={t('buttons.deleteModal.title')}
                visible={isModalVisible}
                onOk={deletePet}
                onCancel={() => setIsModalVisible(false)}
                okText={t('buttons.deleteModal.confirm')}
                cancelTest={t('buttons.deleteModal.cancel')}
                confirmLoading={submitting}
            >
                <p>{t('buttons.deleteModal.content', {name: petName})}</p>
            </Modal>
        </>;
}

const initialStatePet = {
    petName: null,
    birthDate: null,
    gender: null,
    vaccinated: null,
    price: null,
    uploadDate: null,
    description: null,
    status: null,
    username: null,
    userId: null,
    speciesId: null,
    breedId: null,
    provinceId: null,
    departmentId: null,
    images: null
};

function PetView(){
    const {state} = useLogin();
    const {id} = useParams();

    const [pet, setPet] = useState(initialStatePet);

    const {t} = useTranslation('petView');

    const {id: loggedUserId} = state;

    const fetchPet = async () => {
        try{
            const result = await getPet({petId: id});

            setPet(result);
        }catch (e) {
            //TODO: conn error
        }
    };

    useEffect(()=>{
        fetchPet();
    }, []);

    const {petName, userId} = pet;

    const isOwner = loggedUserId === userId;

    return <ContentWithHeader
        content={
            <Content id={id} pet={pet}/>
        }
        actionComponents={
            isOwner ?
                <IsOwnerButtons petId={id} petName={petName}/>
                :
                <></>
        }
        title={petName ? t('title', {name: petName}) : <Spin/>}
    />
}

export default PetView;