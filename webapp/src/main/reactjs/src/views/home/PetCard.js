import React from 'react';

import {List, Card, Button} from 'antd';
import {useTranslation} from "react-i18next";

import "../../css/home/petCard.css";

function PetCard({name, specie, breed, price, sex, owner, uploadDate}){
    const {t} = useTranslation(["petInformation", "home"]);

    return <Card
            className={"pet-card"}
            hoverable
            cover={<img alt="example" src="http://pawserver.it.itba.edu.ar/paw-2020a-7/img/1" />}
        >
            <List
                size={"small"}
            >
                <List.Item>{t("name")}: {name}</List.Item>
                <List.Item>{t("specie")}: {specie}</List.Item>
                <List.Item>{t("breed")}: {breed}</List.Item>
                <List.Item>{t("price")}: {price}</List.Item>
                <List.Item>{t("sex")}: {sex}</List.Item>
                <List.Item>{t("owner")}: {owner}</List.Item>
                <List.Item>{t("uploadDate")}: {uploadDate}</List.Item>
                <List.Item><Button type={"primary"}>{t("home:pets.petsCard.goToPage")}</Button></List.Item>
            </List>
        </Card>
}

export default PetCard;