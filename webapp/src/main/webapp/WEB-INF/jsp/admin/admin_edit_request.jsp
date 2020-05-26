<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<spring:message code="request.edit" var="editRequestTitle"/>
<t:adminLayout title="${editRequestTitle}" item="requests">
    <jsp:body>
        <div class="row">
            <div class=" col-md-10 offset-md-1">

                <div class="bg-light shadow p-3">
                    <div class="p-2">
                        <form action="${pageContext.request.contextPath}/admin/request/${request.id}/edit" method="post">
                            <h1>${editRequestTitle}</h1>
                            <div class="form-row p-1">
                                <div class="col">

                                </div>
                            </div>
                            <label for="status"><spring:message code="request.status"/>:</label>
                            <select name="newStatus" class="form-control" id="status">
                                <c:if test="${request.status.id ne 1}">
                                    <option value="pending">
                                        <spring:message code="request.pending"/>
                                    </option>
                                </c:if>
                                <c:if test="${request.status.id ne 2}">
                                    <option value="accepted">
                                        <spring:message code="request.accepted"/>
                                    </option>
                                </c:if>
                                <c:if test="${request.status.id ne 3}">
                                    <option value="rejected">
                                        <spring:message code="request.rejected"/>
                                    </option>
                                </c:if>
                                <c:if test="${request.status.id ne 4}">
                                    <option value="canceled">
                                        <spring:message code="request.canceled"/>
                                    </option>
                                </c:if>
                            </select>

                            <div class="m-1 p-3 row">
                                <spring:message code="uploadPetForm.submit" var="submitText"/>
                                <input type="submit" class="btn btn-primary" value="${submitText}"/>
                            </div>
                        </form>
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