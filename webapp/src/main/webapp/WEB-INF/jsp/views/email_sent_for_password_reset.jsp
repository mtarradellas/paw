<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/login" var="loginUrl"/>

<spring:message var="loginTitle" message="login.title"/>

<t:basicLayout title="${loginTitle}">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1>${loginTitle}</h1>
            <svg class="bi bi-check" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M13.854 3.646a.5.5 0 010 .708l-7 7a.5.5 0 01-.708 0l-3.5-3.5a.5.5 0 11.708-.708L6.5 10.293l6.646-6.647a.5.5 0 01.708 0z" clip-rule="evenodd"></path>
            </svg>
            <p>
                <spring:message code="resetPassword.emailSent"/>
            </p>
        </div>
    </div>
</t:basicLayout>