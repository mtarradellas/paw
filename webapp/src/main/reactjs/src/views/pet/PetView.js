import React, {useEffect, useState, useContext} from 'react';
import ContentWithHeader from "../../components/ContentWithHeader";
import {useTranslation} from "react-i18next";
import {Button, Carousel, Divider, List, Modal, Spin} from "antd";
import Questions from "./Questions";
import {Link} from "react-router-dom";
import {EDIT_PET, ERROR_404_PET, HOME, USER} from "../../constants/routes";
import _ from 'lodash';
import {DELETE_PET_ERRORS, getPet, deletePet as deletePetApi} from "../../api/pets";
import {useParams} from 'react-router-dom';
import {petImageSrc} from "../../api/images";
import ConstantsContext from '../../constants/constantsContext';
import {petStatus} from '../../constants/petStatus';
import {CloseOutlined, CheckOutlined} from '@ant-design/icons';
import useLogin from "../../hooks/useLogin";
import {useHistory} from 'react-router-dom';
import '../../css/pet/petView.css';

const ListItem = List.Item;

function isMale(sex){
    return _.toLower(sex) === 'male';
}

function ListItemRow({name, value}){
    return <ListItem>
        <b>{name}</b>: {_.isNil(value) ? <Spin/> : value}
    </ListItem>;
}

function ImgModal({id, onClose}){
    console.log(id);

    return <Modal onCancel={onClose} visible={!_.isNil(id)} showOk={false} footer={null}>
        <img className={"pet-view__modal"} src={petImageSrc(id)} alt={""}/>
    </Modal>
}

function Content({pet, id, isLogged}){
    const {t} = useTranslation('petView');

    const [selectedImg, setSelectedImg] = useState(null);
    const isAvailable = pet.status === petStatus.AVAILABLE; 

    const onCloseModal = () => {
        setSelectedImg(null);
    };

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
            <ImgModal id={selectedImg} onClose={onCloseModal}/>

            {
                _.isNil(description) ?
                    <Spin/>
                    :
                    <>
                        <div className={"pet-view__images"}>
                            {
                                images.map(id => <img onClick={() => setSelectedImg(id)} className={"pet-view__images__image"} src={petImageSrc(id)} alt={""}/>)
                            }
                        </div>

                        <p>{description}</p>
                    </>
            }


            <Divider/>

            {
                isAvailable ? 
                    price === 0 ?
                        <h2>{t('status.onAdoption')}</h2>
                        :
                        <h2>{t('status.onSale')}: ${price}</h2>
                    :
                    <h2>{t("status.sold")}</h2>
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

            <Questions petId={id} ownerId={userId} isLogged={isLogged} isAvailable={isAvailable}/>

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
    const [maskClose, setMaskClose] = React.useState(true);
    const [iconClose, setIconClose] = React.useState(true);
    const [cancelProps, setCancelProps] = React.useState();

    const {t} = useTranslation('petView');

    const {id: userId, jwt} = state;

    const deletePet = async () => {
        setMaskClose(false);
        setIconClose(false);
        setCancelProps({disabled: true});
        setSubmitting(true);
        try {
            await deletePetApi({petId}, jwt);

            setMaskClose(true);
            setIconClose(true);
            setCancelProps({disabled: false});

            history.push(USER + userId);
        }catch (e) {
            setMaskClose(true);
            setIconClose(true);
            setCancelProps({disabled: false});
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
            <Button onClick={() => setIsModalVisible(true)} danger={true} loading={submitting}>
                {t('buttons.deletePet')}
            </Button>

            <Link to={EDIT_PET(petId)}>
                <Button>{t('buttons.editPet')}</Button>
            </Link>

            <Modal
                title={t('buttons.deleteModal.title')}
                visible={isModalVisible}
                cancelButtonProps={cancelProps}
                onOk={deletePet}
                okType='danger'
                onCancel={() => setIsModalVisible(false)}
                okText={t('buttons.deleteModal.confirm')}
                cancelTest={t('buttons.deleteModal.cancel')}
                confirmLoading={submitting}
                closable={iconClose}
                maskClosable={maskClose}
            >
                <p>{t('buttons.deleteModal.content', {name: petName})}</p>
            </Modal>
        </>;
}

function RequestButton(petId) {
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [submitting, setSubmitting] = useState(false);

    const {t} = useTranslation('petView');

    const {userId, jwt} =  useLogin().state;

    const requestPet = async () => {
        console.log('HIIII'); // TODO
    }

    return <>
        <Button onClick={requestPet}>Request</Button>
    </>
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

    const {isLoggedIn} = state;
    
    const fetchPet = async () => {
        try{
            const result = await getPet({petId: id});
            
            setPet(result);
        }catch (e) {
            //TODO: conn error
        }
    };

    const isAvailable = pet.status === petStatus.AVAILABLE;

    useEffect(()=>{
        fetchPet();
    }, []);

    const {petName, userId} = pet;

    const isOwner = loggedUserId === userId;

    return <ContentWithHeader
        content={
            <Content id={id} pet={pet} isLogged={isLoggedIn}/>
        }
        actionComponents={
            isOwner ?
                <IsOwnerButtons petId={id} petName={petName}/>
                :
                isLoggedIn && isAvailable ? <RequestButton petId={id}/> : <></>
        }
        title={petName ? t('title', {name: petName}) : <Spin/>}
    />
}

export default PetView;