<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:basicLayout title="Register">
    <div class="container-fluid">
        <div class="shadow p-4 login-register-container">
            <h1><spring:message code="register.title"/></h1>
            <form:form modelAttribute="form" action="${pageContext.request.contextPath}/create" method="post">

                <spring:bind path="username">
                    <div class="form-group">
                        <form:label path="username" for="username"><spring:message code="register.username"/>: </form:label>
                        <form:input type="text" id="username" path="username" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <div class="invalid-feedback">
                                ${status.errorMessage}
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="password">
                    <div class="form-group">
                        <form:label path="password" for="password"><spring:message code="register.password"/>: </form:label>
                        <form:input type="password" id="password" path="password" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                        <div class="invalid-feedback">
                                ${status.errorMessage}
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="repeatPassword">
                    <div class="form-group">
                        <form:label path="repeatPassword" for="repeatPassword"><spring:message code="register.repeatPassword"/>: </form:label>
                        <form:input type="password" id="repeatPassword" cssClass="form-control ${status.error ? 'is-invalid' : ''}" path="repeatPassword"/>
                        <div class="invalid-feedback">
                                ${status.errorMessage}
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="mail">
                    <div class="form-group">
                        <form:label path="mail" for="mail"><spring:message code="register.email"/>: </form:label>
                        <form:input type="text" id="mail" cssClass="form-control ${status.error ? 'is-invalid' : ''}" path="mail"/>
                        <div class="invalid-feedback">
                            ${status.errorMessage}
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="phone">
                    <div class="form-group">
                        <form:label path="phone" for="phone"><spring:message code="register.phone"/>: </form:label>
                        <form:input type="text" id="phone" cssClass="form-control ${status.error ? 'is-invalid' : ''}" path="phone"/>
                        <div class="invalid-feedback">
                            ${status.errorMessage}
                        </div>
                    </div>
                </spring:bind>
                <div>
                    <input type="submit" class="btn btn-primary"/>
                </div>
            </form:form>
        </div>
    </div>

</t:basicLayout>