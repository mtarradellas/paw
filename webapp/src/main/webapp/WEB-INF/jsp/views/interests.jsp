<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="interestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
         <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage.request.action' javaScriptEscape='true'/>
        </span>
        <div class="container-fluid">
            <jsp class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp">
                    <jsp:param name="destination" value="interests"/>
                </jsp:include>
                <div class="col-lg-8">
                    <div class="shadow p-3 bg-white rounded">
                        <h2>Users interested in your pets:</h2>
                        <c:if test="${empty interests_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <c:if test="${(not empty param.status) or (not empty param.searchCriteria) or (not empty param.searchOrder) }">
                                    <a href="${pageContext.request.contextPath}/interests"><spring:message
                                            code="removeFilters"/></a>
                                </c:if>
                            </div>
                        </c:if>
                        <c:if test="${not empty interests_list}">
                            <div class="row">
                                <div class="col-lg-6">
                                    <h5 class="text-left"><b><spring:message code="request"/></b></h5>
                                </div>
                                <div class="col-lg-2">
                                    <h5 class="text-left"><b><spring:message code="request.status"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            <hr class="m-0">

                        </c:if>
                        <c:forEach var="req" items="${interests_list}">
                            <c:if test="${req.status.id eq 1}">
                                <div class="row bg-light p-1">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.isInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.ownerId},${req.ownerUsername},${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                            ${req.status.name}
                                    </div>
                                    <div class="col text-center ">
                                        <div class="button-container ">
                                            <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.petId}"/>"
                                               type="button" class="btn btn-secondary"><spring:message
                                                    code="visitPet"/></a>
                                            <a href="${pageContext.request.contextPath}/user/<c:out value="${req.ownerId}"/>"
                                               type="button" class=" btn btn-secondary"><spring:message
                                                    code="visitUser"/></a>
                                            <form method="POST"
                                                  action="<c:url value="/interests/${req.id}/accept"/>"
                                                  onclick="confirmDelete(event)">
                                                <button type="submit" name="newStatus" class="btn btn-success">
                                                    <spring:message code="accept"/></button>
                                            </form>
                                            <form method="POST"
                                                  action="<c:url value="/interests/${req.id}/reject"/>">
                                                <button type="submit" name="newStatus" class="btn btn-danger"
                                                        onclick="confirmDelete(event)">
                                                    <spring:message code="reject"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.id ne 1}">
                                <div class="row bg-light p-1 resolved">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.wasInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.ownerId},${req.ownerUsername},${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                            ${req.status.name}
                                    </div>
                                    <div class="col text-center">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.petId}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                        <a href="${pageContext.request.contextPath}/user/<c:out value="${req.ownerId}"/>"
                                           type="button" class=" btn btn-secondary"><spring:message
                                                code="visitUser"/></a>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <h4><b><spring:message code="interests.help.title"/></b></h4>
                        <p><spring:message code="interests.help.body"/></p>
                        <h4><b><spring:message code="interests.help.filter.title"/></b></h4>
                        <p><spring:message code="interests.help.filter.body"/></p>
                    </div>
                </div>
        </div>
        </div>
        <script src="<c:url value="/resources/js/admin_control.js"/>"></script>
        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>