import React from 'react';
import "../css/components/contentWithSidebar.css";

function ContentWithSidebar({sideContent, mainContent}){
    return <div className={"content-with-sidebar__container"}>
        <div className={"content-region content-with-sidebar__sidebar"}>
            {sideContent}
        </div>

        <div className={"content-region content-with-sidebar__content"}>
            {mainContent}
        </div>
    </div>;
}

export default ContentWithSidebar;