<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<spring:message var="requestPasswordReset" code="resetPassword.title"/>

<t:basicLayout title="${requestPasswordReset}">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1>${requestPasswordReset}</h1>
            <p><spring:message code="resetPassword.descriptionPassword"/></p>
            <form:form modelAttribute="resetPasswordForm" action="${pageContext.request.contextPath}/password-reset" method="post" enctype="application/x-www-form-urlencoded">

                <spring:bind path="token">
                    <div class="form-group">
                        <form:input path="token" type="hidden" value="${param.token}" cssClass="${status.error}"/>
                        <form:errors path="token" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <spring:bind path="password">
                    <div class="form-group">
                        <spring:message var="passwordTxt" code="resetPassword.password"/>
                        <form:label path="password" for="password">${passwordTxt}: </form:label>
                        <form:input type="password" placeholder="${passwordTxt}" id="password" path="password" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <form:errors path="password" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <c:set var="classError"><form:errors element="div" cssClass="invalid-feedback"/></c:set>

                <spring:bind path="repeatPassword">
                    <div class="form-group">
                        <spring:message var="repeatPasswordTxt" code="resetPassword.repeatPassword"/>
                        <form:label path="repeatPassword" for="repeatPassword">${repeatPasswordTxt}: </form:label>
                        <form:input type="password" placeholder="${repeatPasswordTxt}" id="repeatPassword" cssClass="form-control ${status.error || (not empty classError) ? 'is-invalid' : ''}"
                                    path="repeatPassword"/>
                        <form:errors path="repeatPassword" element="div" cssClass="invalid-feedback"/>
                            ${classError}
                    </div>
                </spring:bind>

                <div class="p-2">
                    <spring:message code="resetPassword.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" value="${submitText}"/>
                </div>
            </form:form>
        </div>
    </div>

</t:basicLayout>