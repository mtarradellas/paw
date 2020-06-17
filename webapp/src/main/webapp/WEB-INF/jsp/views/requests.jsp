<%@ page import="ar.edu.itba.paw.models.constants.RequestStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message code="areYouSure.cancel" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<spring:message code="requestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>
        <div class="container-fluid">
            <div class="row">

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/requests">

                        <div class="card-body">
                            <div class="form-group">
                                <label for="filter-status"><spring:message code="request.status"/></label>
                                <select name="status" class="form-control" id="filter-status">
                                    <option value="any"
                                            <c:if test="${(not empty param.status) && (param.status eq 'any')}">
                                                selected
                                            </c:if>
                                    ><spring:message code="filter.any"/></option>

                                    <c:set var="ACCEPTED" value="<%=RequestStatus.ACCEPTED.getValue()%>"/>
                                    <option value="${ACCEPTED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (ACCEPTED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.accepted"/></option>

                                    <c:set var="REJECTED" value="<%=RequestStatus.REJECTED.getValue()%>"/>
                                    <option value="${REJECTED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (REJECTED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.rejected"/></option>

                                    <c:set var="PENDING" value="<%=RequestStatus.PENDING.getValue()%>"/>
                                    <option value="${PENDING}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (PENDING eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.pending"/></option>

                                    <c:set var="CANCELED" value="<%=RequestStatus.CANCELED.getValue()%>"/>
                                    <option value="${CANCELED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (CANCELED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.canceled"/></option>

                                </select>
                            </div>
                            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
                            <select name="searchCriteria" class="form-control" id="search-criteria">
                                <option value="any"><spring:message code="filter.any"/></option>
                                <option value="date"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'date')}">selected</c:if>
                                ><spring:message code="request.date"/></option>
                                <option value="petName"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'petName')}">selected</c:if>
                                ><spring:message code="request.petName"/></option>
                            </select>
                            <label for="search-order"><spring:message code="filter.order"/></label>
                            <select name="searchOrder" class="form-control" id="search-order"
                                    <c:if test="${(empty param.searchCriteria) || (param.searchCriteria eq 'any')}">
                                        disabled
                                    </c:if>
                            >
                                <option value="asc"
                                        <c:if test="${(not empty param.searchOrder) && (param.searchOrder eq 'asc')}">selected</c:if>
                                ><spring:message code="filter.ascending"/></option>
                                <option value="desc"
                                        <c:if test="${(not empty param.searchOrder) && (param.searchOrder eq 'desc')}">selected</c:if>
                                ><spring:message code="filter.descending"/></option>
                            </select>
                        </div>
                        <div class="card-footer" id="search-tools-submit">
                            <button type="submit" class="btn btn-primary"><spring:message code="filter"/></button>
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/requests"><spring:message code="filter.clear"/></a>
                        </div>
                    </form>
                </div>


                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <h2 class="col"><spring:message code="request.title"/> <spring:message code="showingResults"
                                                                                       arguments="${amount}"/></h2>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right mb-1 "
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
                                <div class="col-lg-6">
                                    <h5 class="text-left"><b><spring:message code="request"/></b></h5>
                                </div>
                                <div class="col-lg-3">
                                    <h5 class="text-left"><b><spring:message code="request.status"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            <hr class="m-0">
                        </c:if>
                        <c:forEach var="req" items="${requestList}">
                            <c:set var="PENDING" value="<%=RequestStatus.PENDING.getValue()%>"/>
                            <c:if test="${req.status.value eq PENDING}">
                                <div class="row bg-light p-1">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.showedInterest"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <fmt:formatDate value="${req.creationDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">
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

                            <c:set var="ACCEPTED" value="<%=RequestStatus.ACCEPTED.getValue()%>"/>
                            <c:if test="${req.status.value eq ACCEPTED}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.wasAccepted"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <fmt:formatDate value="${req.creationDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">
                                        <spring:message code="request.accepted"/>
                                        <c:if test="${req.pet.newOwner eq null}">
                                            <spring:message code="pet.status.notSold"/>
                                        </c:if>
                                        <c:if test="${req.pet.newOwner ne null}">
                                            <spring:message code="pet.status.currentlySold.short" arguments="${req.pet.newOwner.id},${req.pet.newOwner.username}"/>
                                        </c:if>

                                    </div>
                                    <div class="col text-center button-container">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                    </div>
                                </div>
                            </c:if>

                            <c:set var="REJECTED" value="<%=RequestStatus.REJECTED.getValue()%>"/>
                            <c:if test="${req.status.value eq REJECTED}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.wasRejected"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <fmt:formatDate value="${req.creationDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">
                                        <spring:message code="request.rejected"/>
                                    </div>
                                    <div class="col text-center button-container">
                                        <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                           type="button" class="btn btn-secondary"><spring:message
                                                code="visitPet"/></a>
                                    </div>
                                </div>
                            </c:if>

                            <c:set var="CANCELED" value="<%=RequestStatus.CANCELED.getValue()%>"/>
                            <c:if test="${req.status.value eq CANCELED}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-lg-6">
                                        <spring:message code="request.wasCanceled"
                                                        arguments="${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <fmt:formatDate value="${req.creationDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">
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
                        <div class="m-2">
                            <hr>
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/requests'}"/>
                            </c:if>
                        </div>
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