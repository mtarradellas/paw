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
            <p><spring:message code="resetPassword.descriptionPassword" </p>
            <form:form modelAttribute="requestMailForm" action="${pageContext.request.contextPath}/register" method="post" enctype="application/x-www-form-urlencoded">

                <spring:bind path="password">
                    <div class="form-group">
                        <form:label path="password" for="password"><spring:message code="register.password"/>: </form:label>
                        <form:input type="password" id="password" path="password" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <form:errors path="password" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <c:set var="classError"><form:errors element="div" cssClass="invalid-feedback"/></c:set>

                <spring:bind path="repeatPassword">
                    <div class="form-group">
                        <form:label path="repeatPassword" for="repeatPassword"><spring:message code="register.repeatPassword"/>: </form:label>
                        <form:input type="password" id="repeatPassword" cssClass="form-control ${status.error || (not empty classError) ? 'is-invalid' : ''}"
                                    path="repeatPassword"/>
                        <form:errors path="repeatPassword" element="div" cssClass="invalid-feedback"/>
                            ${classError}
                    </div>
                </spring:bind>

                <div class="p-2">
                    <spring:message code="resetPassword.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" name="${submitText}"/>
                </div>
            </form:form>
        </div>
    </div>

</t:basicLayout>