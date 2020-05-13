<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<spring:message code="addRequest" var="addRequestTitle"/>
<t:adminLayout title="${addRequestTitle}" item="requests">
    <jsp:body>
        <div class="row">
            <div class=" col-md-10 offset-md-1">

                <div class="bg-light shadow p-3">
                    <div class="p-2">
                        <form:form modelAttribute="adminUploadRequestForm"
                                   action="${pageContext.request.contextPath}/admin/upload-request" method="post"
                                   enctype="multipart/form-data">
                            <h1>${addRequestTitle}</h1>

                            <div class="form-row p-1">

                                <div class="col">
                                    <spring:bind path="userId">
                                        <div class="form-group">
                                            <spring:message code="user" var="userText"/>
                                            <form:label path="userId" for="userId">${userText}: </form:label>
                                            <form:select id="userId" path="userId"
                                                         cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                                <form:options items="${users_list}" itemValue="id"
                                                              itemLabel="username"/>
                                            </form:select>
                                            <form:errors path="userId" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>

                                </div>
                                <div class="col">
                                    <spring:bind path="petId">
                                        <div class="form-group">
                                            <spring:message code="pet" var="petText"/>
                                            <form:label path="petId" for="petId">${petText}: </form:label>
                                            <form:select id="petId" path="petId"
                                                         cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                                <form:options items="${pets_list}" itemValue="id"
                                                              itemLabel="petName"/>
                                            </form:select>
                                            <form:errors path="petId" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>

                                </div>
                            </div>
                            <c:if test="${request_error eq true}">
                                <p class="error"><spring:message code="Request.wrongRequest"/></p>
                            </c:if>

                            <div class="m-1 p-3 row">
                                <spring:message code="uploadPetForm.submit" var="submitText"/>
                                <input type="submit" class="btn btn-primary" value="${submitText}"/>
                            </div>
                        </form:form>
                    </div>
                    <div class="p-3">
                        <a href="${pageContext.request.contextPath}/admin/requests"><spring:message code="goBackLists"/></a>
                    </div>
                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/selector_dependency.js"/>"></script>
    </jsp:body>
</t:adminLayout>