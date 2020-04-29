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
            <p><spring:message code="resetPassword.descriptionEmail"/></p>
            <form:form modelAttribute="mailForm" action="${pageContext.request.contextPath}/request-password-reset" method="post" enctype="application/x-www-form-urlencoded">
                <spring:bind path="mail">
                    <div class="form-group">
                        <spring:message code="resetPassword.email" var="emailTxt"/>
                        <form:label path="mail" for="mail">${emailTxt}: </form:label>
                        <form:input placeholder="${emailTxt}" type="text" id="mail" cssClass="form-control ${status.error || invalid_mail ? 'is-invalid' : ''}" path="mail"/>
                        <form:errors path="mail" element="div" cssClass="invalid-feedback"/>
                        <c:if test="${invalid_mail}">
                            <div class="invalid-feedback">
                                <spring:message code="resetPassword.invalidEmail"/>
                            </div>
                        </c:if>
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