<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<t:basicLayout title="Login">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container">
            <h1><spring:message code="login.title"/></h1>
            <form:form modelAttribute="form" action="${pageContext.request.contextPath}/login" method="post">

                <spring:bind path="username">
                    <div class="form-group">
                        <form:label path="username" for="username"><spring:message code="register.username"/>: </form:label>
                        <form:input type="text" id="username" path="username" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <form:errors path="username" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <spring:bind path="password">
                    <div class="form-group">
                        <form:label path="password" for="password"><spring:message code="register.password"/>: </form:label>
                        <form:input type="password" id="password" path="password" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <form:errors path="password" element="div" cssClass="invalid-feedback"/>
                    </div>
                </spring:bind>

                <div>
                    <spring:message code="login.submit" var="submitText"/>
                    <input type="submit" class="btn btn-primary" name="${submitText}"/>
                </div>
            </form:form>
        </div>
    </div>
</t:basicLayout>