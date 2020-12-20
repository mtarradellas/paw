import React from 'react';
import {petImageSrc} from "../../api/images";
import ImageCheckbox from "../../components/ImageCheckbox";
import '../../css/editPet/deleteImagesInput.css';

function DeleteImagesInput({values, setFieldValue}){
    const {images} = values;

    const _onChange = id => newValue => {
        const newDeletedFiles = [...values['filesToDelete']];

        if(newValue){
            newDeletedFiles.push(id);
        }else{
            const index = newDeletedFiles.indexOf(id);
            newDeletedFiles.splice(index, 1);
        }

        setFieldValue('filesToDelete', newDeletedFiles);
    };

    return <div className={"delete-images-input"}>
        {
            images.map(id => {

                return <ImageCheckbox src={petImageSrc(id)} onChange={_onChange(id)}/>;
            })
        }
    </div>;
}

export default DeleteImagesInput;