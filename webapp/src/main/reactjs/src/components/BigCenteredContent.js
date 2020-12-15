import React from 'react';
import '../css/components/bigCenteredContent.css';

function BigCenteredContent({children}){

    return <div className={"small-centered-content"}>
        {children}
    </div>
}

export default BigCenteredContent;