import React from 'react';

import '../css/components/contentWithHeader.css';

function ContentWithHeader({title, actionComponents, content}){

    return <div className={"content-with-header--container"}>
        <div className={"content-with-header--header"}>
            <div className={"content-with-header--header--title"}><h1>{title}</h1></div>
            <div className={"content-with-header--header--buttons"}>{actionComponents}</div>
        </div>
        <div className={"content-with-header--content"}>
            {content}
        </div>
    </div>;
}

export default ContentWithHeader;