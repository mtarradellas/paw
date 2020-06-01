<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="areYouSure.cancel" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<spring:message code="requestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp">
                    <jsp:param name="destination" value="requests"/>
                </jsp:include>
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <h2 class="col"><spring:message code="request.title"/> <spring:message code="showingResults"
                                                                                       arguments="${amount}"/></h2>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right "
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>
                        </div>
                        <c:if test="${empty requestList}">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <c:choose>
                                    <c:when test="${(empty param.status || param.status eq 'any')
                                        && (empty param.searchCriteria || param.searchCriteria eq 'any')}">
                                        <c:url var="homeUrl" value="/"/>
                                        <spring:message code="request.emptyRequests"/>
                                        <a href="${homeUrl}"><spring:message code="request.goToHome"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/requests"><spring:message
                                                code="removeFilters"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        <c:if test="${not empty requestList}">
                            <div class="row">
                                <div class="col-lg-7">
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
                        <c:forEach var="req" items="${requestList}">
                            <c:if test="${req.status.value eq 1}">
                                <div class="row bg-light p-1">
                                    <div class=" col-lg-7">
                                        <spring:message code="request.showedInterest"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                        <spring:message code="request.pending"/>
                                    </div>

                                    <div class="col text-center">
                                        <form method="POST" class="m-0"
                                              action="<c:url value="/requests/${req.id}/cancel"/>">
                                            <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                               type="button" class="btn btn-secondary"><spring:message
                                                    code="visitPet"/></a>
                                            <button type="submit" name="newStatus" value="cancel"
                                                    class="btn btn-danger are-you-sure"><spring:message code="cancel"/></button>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.value eq 2}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-7">
                                        <spring:message code="request.wasAccepted"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                        <spring:message code="request.accepted"/>
                                    </div>
                                    <div class="col text-center button-container">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.value eq 3}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-7">
                                        <spring:message code="request.wasRejected"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                        <spring:message code="request.rejected"/>
                                    </div>
                                    <div class="col text-center button-container">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.value eq 4}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-7">
                                        <spring:message code="request.wasCanceled"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${req.creationDate}</small>
                                    </div>
                                    <div class="col-lg-2">
                                        <spring:message code="request.canceled"/>
                                    </div>
                                    <div class="col text-center button-container">
                                        <form method="POST" class="m-0"
                                              action="<c:url value="/requests/${req.id}/recover"/>">
                                            <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                               type="button" class="btn btn-secondary"><spring:message
                                                    code="visitPet"/></a>
                                            <button type="submit" name="newStatus" value="recover"
                                                    class="btn btn-success"><spring:message code="petCard.recover"/></button>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
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
                        <h2><b><spring:message code="requests.help.title"/></b></h2>
                        <p><spring:message code="requests.help.body"/></p>
                        <h2><b><spring:message code="requests.help.filter.title"/></b></h2>
                        <p><spring:message code="requests.help.filter.body"/></p>
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