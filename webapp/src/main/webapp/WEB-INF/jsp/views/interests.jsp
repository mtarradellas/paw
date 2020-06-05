<%@ page import="ar.edu.itba.paw.models.constants.RequestStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="interestsTitle" var="titleVar"/>
<spring:message code="areYouSure.title" var="sureTitle"/>
<spring:message code="areYouSure.body" var="sureBody"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>
            <jsp class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp">
                    <jsp:param name="destination" value="interests"/>
                </jsp:include>
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <h2 class="col"><spring:message code="interest.title"/> <spring:message code="showingResults"
                                                                                        arguments="${amount}"/></h2>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right "
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>
                        </div>

                        <c:if test="${empty interestList }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <c:if test="${(not empty param.status) or (not empty param.searchCriteria) or (not empty param.searchOrder) }">
                                    <a href="${pageContext.request.contextPath}/interests"><spring:message
                                            code="removeFilters"/></a>
                                </c:if>
                                <c:if test="${(empty param.status) and (empty param.searchCriteria) and (empty param.searchOrder) }">
                                    <a href="${pageContext.request.contextPath}/upload-pet">
                                        <spring:message code="request.addPets"/></a>
                                </c:if>
                            </div>
                        </c:if>
                        <c:if test="${not empty interestList}">
                            <div class="row">
                                <div class="col-lg-5">
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
                        <c:forEach var="req" items="${interestList}">
                            <c:set var="PENDING" value="<%=RequestStatus.PENDING.getValue()%>"/>
                            <c:if test="${req.status.value eq PENDING}">
                                <div class="row bg-light p-1">
                                    <div class=" col-lg-5">
                                        <spring:message code="request.isInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.user.id},${req.user.username},${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                            ${req.status}
                                    </div>
                                    <div class="col text-center ">
                                        <div class="button-container ">
                                            <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                               type="button" class="btn btn-secondary"><spring:message
                                                    code="visitPet"/></a>
                                            <a href="${pageContext.request.contextPath}/user/<c:out value="${req.user.id}"/>"
                                               type="button" class=" btn btn-secondary"><spring:message
                                                    code="visitUser"/></a>
                                            <form method="POST"
                                                  action="<c:url value="/interests/${req.id}/accept"/>">
                                                <button type="submit" name="newStatus" class="btn btn-success are-you-sure">
                                                    <spring:message code="accept"/></button>
                                            </form>
                                            <form method="POST"
                                                  action="<c:url value="/interests/${req.id}/reject"/>">
                                                <button type="submit" name="newStatus" class="btn btn-danger are-you-sure">
                                                    <spring:message code="reject"/></button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.value ne PENDING}">
                                <div class="row bg-light p-1 resolved">
                                    <div class=" col-lg-5">
                                        <spring:message code="request.wasInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.user.id},${req.user.username},${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                        <c:set var="ACCEPTED" value="<%=RequestStatus.ACCEPTED.getValue()%>"/>
                                        <c:set var="CANCELED" value="<%=RequestStatus.CANCELED.getValue()%>"/>
                                        <c:set var="REJECTED" value="<%=RequestStatus.REJECTED.getValue()%>"/>
                                        <c:choose>
                                            <c:when test="${req.status.value eq ACCEPTED}">
                                                <spring:message code="request.accepted"/>
                                            </c:when>

                                            <c:when test="${req.status.value eq CANCELED}">
                                                <spring:message code="request.canceled"/>
                                            </c:when>

                                            <c:when test="${req.status.value eq REJECTED}">
                                                <spring:message code="request.rejected"/>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                    <div class="col text-center">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                        <a href="${pageContext.request.contextPath}/user/<c:out value="${req.user.id}"/>"
                                           type="button" class=" btn btn-secondary"><spring:message
                                                code="visitUser"/></a>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
        </div>

        <div class="modal fade" id="help" tabindex="-1" role="dialog" aria-labelledby="helpTitle"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title" id="helpTitle"><spring:message code="help.title"/></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <h2><b><spring:message code="interests.help.title"/></b></h2>
                        <p><spring:message code="interests.help.body"/></p>
                        <h2><b><spring:message code="interests.help.filter.title"/></b></h2>
                        <p><spring:message code="interests.help.filter.body"/></p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>