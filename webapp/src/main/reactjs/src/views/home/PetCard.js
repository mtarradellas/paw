import React, { useContext } from "react";
import { List, Card, Button } from "antd";
import { useTranslation } from "react-i18next";
import "../../css/home/petCard.css";
import { Link } from "react-router-dom";
import { PET, USER, ADMIN_PET, ADMIN_USER } from "../../constants/routes";
import { petImageSrc } from "../../api/images";
import _ from "lodash";
import { petStatus, petStatusToString } from "../../constants/petStatus";
import moment from "moment";
import ConstantsContext from "../../constants/constantsContext";

function PetCard({ pet, admin }) {
    const { breeds, species } = useContext(ConstantsContext);

    const {
        petName,
        speciesId,
        breedId,
        price,
        gender,
        username,
        uploadDate,
        id,
        images,
        userId,
        status,
    } = pet;

    const { t } = useTranslation(["petInformation", "home"]);

    const isAvailable = status === petStatus.AVAILABLE;

    const isAdmin = !(admin === null || !admin);

    const petPath = isAdmin ? ADMIN_PET + id : PET + id;
    const userPath = isAdmin ? ADMIN_USER + userId : USER + userId;

    const breed = _.get(breeds, "[" + breedId + "].name", null);
    const specie = _.get(species, "[" + speciesId + "].name", null);

    return (
        <Card
            className={
                "pet-card" + (!isAvailable ? " pet-card--not-available" : "")
            }
            cover={
                <Link to={petPath}>
                    <img
                        className={"pet-card--img"}
                        alt="example"
                        src={petImageSrc(images[0])}
                    />
                </Link>
            }
        >
            <List size={"small"}>
                <List.Item>
                    {t("name")}: {petName}
                </List.Item>
                <List.Item>
                    {t("species")}: {specie}
                </List.Item>
                <List.Item>
                    {t("breed")}: {breed}
                </List.Item>
                <List.Item>
                    {t("price")}: ${price}
                </List.Item>
                <List.Item>
                    {t("sex")}: {t(_.toLower(gender))}
                </List.Item>
                <List.Item>
                    {t("owner")}: <Link to={userPath}>{username}</Link>
                </List.Item>
                <List.Item>
                    {t("uploadDate")}: {moment(uploadDate).format("DD/MM/YYYY")}
                </List.Item>
                <List.Item>
                    <Link to={petPath}>
                        <Button type={"primary"}>
                            {t("home:pets.petsCard.goToPage")}
                        </Button>
                    </Link>
                    {!isAvailable && (
                        <span>
                            ({t("statuses." + petStatusToString(status))})
                        </span>
                    )}
                </List.Item>
            </List>
        </Card>
    );
}

export default PetCard;
