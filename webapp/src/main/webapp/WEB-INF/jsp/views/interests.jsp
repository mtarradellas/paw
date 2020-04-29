<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="interestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <jsp class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp" >
                    <jsp:param name="destination" value="interests"/>
                </jsp:include>
                <div class="col ">
                    <div class="shadow p-3 bg-white rounded">
                        <h2>Users interested in your pets:</h2>
                            <c:if test="${empty interests_list }">
                                <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                    <c:if test="${(not empty param.status) or (not empty param.searchCriteria) or (not empty param.searchOrder) }">
                                        <a href="${pageContext.request.contextPath}/interests"><spring:message code="showAll"/></a>
                                    </c:if>
                                </div>
                            </c:if>
                            <c:forEach var="req" items="${interests_list}">
                                <c:if test="${req.status.id eq 1}">
                                    <div class="row bg-light p-1">
                                        <div class=" col-sm-10">
                                            <spring:message code="request.isInterested" arguments="${pageContext.request.contextPath}/user/${req.ownerId},${req.ownerUsername},${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                            <small class="text-warning">    ${req.creationDate}</small>
                                        </div>
                                        <div class="col-sm-2 ">
                                            <form method="POST" class="m-0" action="<c:url value="/interests-accept-reject/${req.id}"/>">
                                                <button type="submit" name="newStatus" value="accept" class="btn btn-success"><spring:message code="accept"/></button>
                                                <button type="submit" name="newStatus" value="reject" class="btn btn-danger" ><spring:message code="reject"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${req.status.id ne 1}">
                                    <div class="row bg-light p-1 resolved">
                                        <div class=" col-sm-10">
                                            <spring:message code="request.wasInterested" arguments="${pageContext.request.contextPath}/user/${req.ownerId},${req.ownerUsername},${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                            <small class="text-warning">    ${req.creationDate}</small>
                                        </div>
                                        <div class="col-sm-2 ">
                                            <p>${req.status.name}</p>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>