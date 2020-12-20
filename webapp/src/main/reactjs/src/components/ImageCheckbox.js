import React, {useState} from 'react';

import '../css/components/imageCheckbox.css';

function ImageCheckbox({src, onChange}) {
    const [checked, setChecked] = useState(false);

    const _onClick = () => {
        const newValue = !checked;

        setChecked(newValue);

        onChange && onChange(newValue);
    };

    const className = "image-checkbox " + (checked ? 'image-checkbox--checked' : '');

    return <div className={className} onClick={_onClick}>
            <img src={src} alt={""}/>
    </div>
}

export default ImageCheckbox;