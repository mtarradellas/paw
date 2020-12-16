import React from 'react';
import AdminHeader from "./AdminHeader";

import '../css/admin/admin.css'

function AdminLayout(props){
    const {children} = props;

    return <div className={"adminLayout"}>
        <AdminHeader/>
        {children}
    </div>;
}

export default AdminLayout;