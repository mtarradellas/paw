import React, {useEffect, useState, useContext} from 'react';
import _ from 'lodash';
import {useTranslation} from "react-i18next";
import useLogin from "../../hooks/useLogin";
import {Link, useParams} from "react-router-dom";
import {getPetAdmin, recoverPetAdmin, removePetAdmin} from "../../api/admin/pets";
import {GET_PET_ERRORS} from "../../api/pets";
import ContentWithHeader from "../../components/ContentWithHeader";
import {Button, Divider, List, Modal, Spin} from "antd";
import {ADMIN_EDIT_PET, ADMIN_HOME, ADMIN_USER} from "../../constants/routes";
import ConstantsContext from "../../constants/constantsContext";
import {petImageSrc} from "../../api/images";
import {CheckOutlined, CloseOutlined} from "@ant-design/icons";
import moment from "moment";


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
            <ListItemRow name={t('details.dateOfBirth')} value={moment(birthDate).format("DD/MM/YYYY")}/>
            <ListItemRow name={t('details.specie')} value={speciesId && species[speciesId].name}/>
            <ListItemRow name={t('details.breed')} value={breedId && breeds[breedId].name}/>
            <ListItemRow name={t('details.sex')} value={t('details.' + _.toLower(gender))}/>
            <ListItemRow name={t('details.province')} value={provinceId && provinces[provinceId].name}/>
            <ListItemRow name={t('details.department')} value={departmentId && departments[departmentId].name}/>
            <ListItemRow name={t('details.uploadDate')} value={moment(uploadDate).format("DD/MM/YYYY")}/>
            <ListItemRow name={t('details.owner')} value={<Link to={ADMIN_USER + userId}>{username}</Link>}/>
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


        <div>
            <Link to={ADMIN_USER + userId}>
                <Button>{t('goToOwnersPage')}</Button>
            </Link>
        </div>

        <div>
            <Link to={ADMIN_HOME}>
                {t('backToHome')}
            </Link>
        </div>
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


function AdminPetView(){
    const {state} = useLogin();
    const {id} = useParams();

    const [pet, setPet] = useState(initialStatePet);

    const {t} = useTranslation('adminPetView');

    const {jwt} = state;

    const fetchPet = async () => {
        try {
            const result = await getPetAdmin(id,jwt);
            setPet(result);
        }catch (e){
            throw GET_PET_ERRORS.CONN_ERROR
        }
    };

    useEffect(()=>{
        fetchPet();
    },[]);

    const statusLocale = [
        t("status.available"),
        t("status.removed"),
        t("status.sold"),
        t("status.unavailable")
    ]

    const {petName} = pet;

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

    if (pet.status === 0 || pet.status === 3) {
        const onConfirm = async () => {
            try {
                await removePetAdmin(id, jwt)
                fetchPet();
            } catch (e) {
                console.log(e)
            }
        }
        const modalMessage = t("modals.removePet")

        modalButton = <Button type={"primary"} danger key={"remove"}
                              onClick={() => showModal(onConfirm, modalMessage)}>{t("buttons.remove")}</Button>
    } else {
        const onConfirm = async () => {
            try {
                await recoverPetAdmin(id, jwt)
                fetchPet()
            } catch (e) {
                console.log(e)
            }
        }

        const modalMessage = t("modals.recoverPet")
        modalButton = <Button type={"primary"} danger key={"recover"}
                              onClick={() => showModal(onConfirm, modalMessage)}>{t("buttons.recover")}</Button>
    }

    return <ContentWithHeader
        content={
            <div>
                <Content id={id} pet={pet}/>
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
        actionComponents={
            [
                modalButton,
                <Button href={ADMIN_EDIT_PET + id} key={"edit"}>{t("buttons.edit")}</Button>
            ]
        }
        title={petName ? petName + " (" + statusLocale[pet.status] + ")" : <Spin/>}
    />
}

export default AdminPetView;