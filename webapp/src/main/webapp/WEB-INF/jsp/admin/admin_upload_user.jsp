<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:message code="addUser" var="addUserTitle"/>
<t:adminLayout title="${addUserTitle}" item="user">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1>${addUserTitle}</h1>

            <form:form modelAttribute="registerForm"
                       action="${pageContext.request.contextPath}/admin/upload-user"
                       method="post" enctype="application/x-www-form-urlencoded">
                <spring:bind path="username">
                    <div class="form-group">
                        <spring:message code="register.username" var="usernameTxt"/>
                        <form:label path="username" for="username">${usernameTxt}: </form:label>
                        <form:input placeholder="${usernameTxt}" type="text" id="username" path="username" cssClass="form-control ${status.error || duplicatedUsername ? 'is-invalid' : ''}"/>
                        <form:errors path="username" element="div" cssClass="invalid-feedback"/>
                        <c:if test="${duplicatedUsername}">
                            <div class="invalid-feedback">
                                <spring:message code="register.usernameNotUnique"/>
                            </div>
                        </c:if>
                    </div>
                </spring:bind>
                <spring:bind path="password">
                    <div class="form-group">
                        <spring:message code="register.password" var="passwordTxt"/>
                        <form:label path="password" for="password">${passwordTxt}: </form:label>
                        <form:input type="password" placeholder="${passwordTxt}" id="password" path="password" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <form:errors path="password" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>
                <c:set var="classError"><form:errors element="div" cssClass="invalid-feedback"/></c:set>
                <spring:bind path="repeatPassword">
                    <div class="form-group">
                        <spring:message code="register.repeatPassword" var="repeatPasswordTxt"/>
                        <form:label path="repeatPassword" for="repeatPassword">${repeatPasswordTxt}: </form:label>
                        <form:input placeholder="${repeatPasswordTxt}" type="password" id="repeatPassword" cssClass="form-control ${status.error || (not empty classError) ? 'is-invalid' : ''}"
                                    path="repeatPassword"/>
                        <form:errors path="repeatPassword" element="div" cssClass="invalid-feedback"/>
                            ${classError}
                    </div>
                </spring:bind>
                <spring:bind path="mail">
                    <div class="form-group">
                        <spring:message code="register.email" var="emailTxt"/>
                        <form:label path="mail" for="mail">${emailTxt}: </form:label>
                        <form:input placeholder="${emailTxt}" type="text" id="mail" cssClass="form-control ${status.error || duplicatedMail ? 'is-invalid' : ''}" path="mail"/>
                        <form:errors path="mail" element="div" cssClass="invalid-feedback"/>
                        <c:if test="${duplicatedMail}">
                            <div class="invalid-feedback">
                                <spring:message code="register.emailNotUnique"/>
                            </div>
                        </c:if>
                    </div>
                </spring:bind>
                <div class="p-2">
                    <spring:message code="register.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" value="${submitText}"/>
                </div>
            </form:form>
            <div class="p-3">
                <a href="${pageContext.request.contextPath}/admin/requests"><spring:message code="goBackLists"/></a>
            </div>
        </div>

    </div>
</t:adminLayout>