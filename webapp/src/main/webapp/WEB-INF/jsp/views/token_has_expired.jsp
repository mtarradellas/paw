<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message var="resetPasswordTxt" code="resetPassword.title"/>

<t:basicLayout title="${resetPasswordTxt}">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <svg class="bi bi-exclamation-circle" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                <path fill-rule="evenodd" d="M8 15A7 7 0 108 1a7 7 0 000 14zm0 1A8 8 0 108 0a8 8 0 000 16z" clip-rule="evenodd"/>
                <path d="M7.002 11a1 1 0 112 0 1 1 0 01-2 0zM7.1 4.995a.905.905 0 111.8 0l-.35 3.507a.552.552 0 01-1.1 0L7.1 4.995z"/>
            </svg>
            <p>
                <spring:message code="resetPassword.expiredToken"/>
                <c:url value="request_password_reset.jsp" var="requestPasswordResetUrl"/>
                <a href="${requestPasswordResetUrl}"><spring:message code="resetPassword.requestAgainEmail"/></a>
            </p>
        </div>
    </div>
</t:basicLayout>