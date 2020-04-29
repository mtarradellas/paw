<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:url value="/login" var="loginUrl"/>
<spring:message var="loginTitle" code="login.title"/>
<t:basicLayout title="${loginTitle}">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1>${loginTitle}</h1>
            <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
                <div class="form-group">
                    <spring:message code="login.username" var="usernameTxt"/>
                    <label for="username">${usernameTxt}:</label>
                    <input type="text" placeholder="${usernameTxt}" class="form-control" name="username" id="username">
                </div>
                <div class="form-group">
                    <spring:message code="login.password" var="passwordTxt"/>
                    <label for="password">${passwordTxt}:</label>
                    <input type="password" placeholder="${passwordTxt}" class="form-control" name="password" id="password">
                </div>
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="rememberme" name="rememberme">
                    <label class="form-check-label" for="rememberme"><spring:message code="login.rememberMe"/></label>
                </div>
                <div class="p-2">
                    <spring:message code="login.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" value="${submitText}"/>
                </div>
            </form>
            <spring:message code="login.forgotPassword" arguments="${pageContext.request.contextPath}/request-password-reset"/><br>
            <spring:message code="login.notRegistered" arguments="${pageContext.request.contextPath}/register"/>
        </div>
    </div>
</t:basicLayout>