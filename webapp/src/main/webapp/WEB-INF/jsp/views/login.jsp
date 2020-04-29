<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:url value="/login" var="loginUrl"/>

<t:basicLayout title="Login">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1><spring:message code="login.title"/></h1>
            <form action="${loginUrl}" method="post" enctype="application/x-www-form-urlencoded">
                <div class="form-group">
                    <label for="username"><spring:message code="login.username"/>:</label>
                    <input type="text" class="form-control" name="username" id="username">
                </div>

                <div class="form-group">
                    <label for="password"><spring:message code="login.password"/>:</label>
                    <input type="password" class="form-control" name="password" id="password">
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
            <spring:message code="login.notRegistered" arguments="${pageContext.request.contextPath}/register"/>
        </div>
    </div>
</t:basicLayout>
