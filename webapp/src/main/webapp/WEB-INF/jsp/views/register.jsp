<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:basicLayout title="Register">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container bg-white">
            <h1><spring:message code="register.title"/></h1>
            <form:form modelAttribute="registerForm" action="${pageContext.request.contextPath}/register" method="post" enctype="application/x-www-form-urlencoded">
                <spring:bind path="username">
                    <div class="form-group">
                        <form:label path="username" for="username"><spring:message code="register.username"/>: </form:label>
                        <form:input type="text" id="username" path="username" cssClass="form-control ${status.error || duplicatedUsername ? 'is-invalid' : ''}"/>
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

                <spring:bind path="mail">
                    <div class="form-group">
                        <form:label path="mail" for="mail"><spring:message code="register.email"/>: </form:label>
                        <form:input type="text" id="mail" cssClass="form-control ${status.error || duplicatedMail ? 'is-invalid' : ''}" path="mail"/>
                        <form:errors path="mail" element="div" cssClass="invalid-feedback"/>
                        <c:if test="${duplicatedMail}">
                            <div class="invalid-feedback">
                                <spring:message code="register.emailNotUnique"/>
                            </div>
                        </c:if>
                    </div>
                </spring:bind>

                <spring:bind path="phone">
                    <div class="form-group">
                        <form:label path="phone" for="phone"><spring:message code="register.phone"/>: </form:label>
                        <form:input type="text" id="phone" cssClass="form-control ${status.error ? 'is-invalid' : ''}" path="phone"/>
                        <form:errors path="phone" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <div class="p-2">
                    <spring:message code="register.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" value="${submitText}"/>
                </div>
            </form:form>
            <spring:message code="login.forgotPassword" arguments="${pageContext.request.contextPath}/request-password-reset"/><br>
            <spring:message code="register.alreadyDone" arguments="${pageContext.request.contextPath}/login"/><br>
            <spring:message code="login.linkAccount" arguments="${pageContext.request.contextPath}/request-link-account"/>
        </div>
    </div>

</t:basicLayout>