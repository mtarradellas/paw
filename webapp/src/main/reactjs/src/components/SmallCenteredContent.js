import React from 'react';
import '../css/components/smallCenteredContent.css';

function SmallCenteredContent({children}){

    return <div className={"small-centered-content"}>
        {children}
    </div>
}

export default SmallCenteredContent;