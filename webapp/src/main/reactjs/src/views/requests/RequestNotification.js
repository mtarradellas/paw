import React from 'react';
import {useTranslation} from "react-i18next";

function RequestNotification(
    {creationDate, updateDate, status, user, userId, pet, petId, petStatus, newPetOwner, newPetOwnerId}
    ){
    const {t} = useTranslation("requests");

    return <div>
        {pet}
    </div>
}

export default RequestNotification;