import React from 'react';
import {List, Card, Button} from 'antd';
import {useTranslation} from "react-i18next";
import "../../css/home/petCard.css";
import {Link} from "react-router-dom";
import {PET, USER} from "../../constants/routes";
import {petImageSrc} from "../../api/images";
import _ from 'lodash';

function PetCard({pet}){
    const {petName, specie, breed, price, gender, username, uploadDate, id, images, userId} = pet;

    const {t} = useTranslation(["petInformation", "home"]);

    return <Card
            className={"pet-card"}
            cover={
                <Link to={PET + id}>
                    <img className={"pet-card--img"} alt="example" src={petImageSrc(images[0])}/>
                </Link>
            }
        >
            <List
                size={"small"}
            >
                <List.Item>{t("name")}: {petName}</List.Item>
                <List.Item>{t("specie")}: {specie}</List.Item>
                <List.Item>{t("breed")}: {breed}</List.Item>
                <List.Item>{t("price")}: ${price}</List.Item>
                <List.Item>{t("sex")}: {t(_.toLower(gender))}</List.Item>
                <List.Item>{t("owner")}: <Link to={USER + userId}>{username}</Link></List.Item>
                <List.Item>{t("uploadDate")}: {uploadDate}</List.Item>
                <List.Item>
                    <Link to={PET + id}>
                        <Button type={"primary"}>{t("home:pets.petsCard.goToPage")}</Button>
                    </Link>
                </List.Item>
            </List>
        </Card>
}

export default PetCard;