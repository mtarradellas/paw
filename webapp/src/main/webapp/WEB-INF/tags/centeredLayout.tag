<%@tag description="Centered layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<t:basicLayout title="${title}">
    <div class="container-fluid h-100">
        <div class="row justify-content-center h-100">
            <div class="col-8 justify-content-center align-middle h-100">
                <div class="shadow p-3 bg-white rounded">
                    <jsp:doBody/>
                </div>
            </div>
        </div>
    </div>
</t:basicLayout>