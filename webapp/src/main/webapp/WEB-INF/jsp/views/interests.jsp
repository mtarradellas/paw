<%@ page import="ar.edu.itba.paw.models.constants.RequestStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="ACCEPTED" value="<%=RequestStatus.ACCEPTED.getValue()%>"/>
<c:set var="REJECTED" value="<%=RequestStatus.REJECTED.getValue()%>"/>
<c:set var="PENDING" value="<%=RequestStatus.PENDING.getValue()%>"/>
<c:set var="CANCELED" value="<%=RequestStatus.CANCELED.getValue()%>"/>
<c:set var="SOLD" value="<%=RequestStatus.SOLD.getValue()%>"/>


<spring:message code="interestsTitle" var="titleVar"/>
<spring:message code="areYouSure.title" var="sureTitle"/>
<spring:message code="areYouSure.body" var="sureBody"/>

<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>
            <div class="row">

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/interests">

                        <div class="card-body">
                            <div class="form-group">
                                <label for="filter-status"><spring:message code="request.status"/></label>
                                <select name="status" class="form-control" id="filter-status">
                                    <option value="any"
                                            <c:if test="${(not empty param.status) && (param.status eq 'any')}">
                                                selected
                                            </c:if>
                                    ><spring:message code="filter.any"/></option>

                                    <option value="${ACCEPTED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (ACCEPTED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.accepted"/></option>

                                    <option value="${REJECTED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (REJECTED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.rejected"/></option>

                                    <option value="${PENDING}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (PENDING eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.pending"/></option>

                                    <option value="${CANCELED}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (CANCELED eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.canceled"/></option>

                                    <option value="${SOLD}"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && (SOLD eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.sold"/></option>

                                </select>
                                <label for="filter-pet"><spring:message code="pet"/></label>
                                <select data-child="filter-pet" name="petId" class="selector-parent form-control" id="filter-pet">
                                    <option value="-1"><spring:message code="filter.any"/></option>
                                    <c:forEach items="${availablePets}" var="singlePet">
                                        <c:set var="petId">${singlePet.id}</c:set>
                                        <option value="${singlePet.id}"
                                                <c:if test="${(not empty param.petId)  && (petId eq param.petId)}">
                                                    selected
                                                </c:if>
                                        >
                                                ${singlePet.petName}
                                        </option>
                                    </c:forEach>
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
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/interests"><spring:message code="filter.clear"/></a>
                        </div>
                    </form>
                </div>


                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <h2 class="col"><spring:message code="interest.title"/> <spring:message code="showingResults"
                                                                                        arguments="${amount}"/></h2>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right mb-1"
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
                                <div class="col-lg-3">
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
                                        <fmt:parseDate  value="${req.creationDate}"  type="date" pattern="yyyy-MM-dd" var="parsedDate" />
                                        <fmt:formatDate value="${parsedDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <spring:message code="request.isInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.user.id},${req.user.username},${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">
                                        <spring:message code="request.pending"/>
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
                                                <button type="submit" name="newStatus" class="btn btn-success accept-request">
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

                            <c:choose>
                                <c:when test="${req.status.value eq SOLD}">
                                    <div class="row bg-light p-1 resolved">
                                </c:when>
                                <c:otherwise>
                                    <div class="row bg-light p-1 ">
                                </c:otherwise>
                            </c:choose>

                                    <div class=" col-lg-5">

                                        <fmt:parseDate  value="${req.creationDate}"  type="date" pattern="yyyy-MM-dd" var="parsedDate" />
                                        <fmt:formatDate value="${parsedDate}" var="creationDate" type="date" pattern="dd-MM-yyyy"/>

                                        <spring:message code="request.wasInterested"
                                                        arguments="${pageContext.request.contextPath}/user/${req.user.id},${req.user.username},${pageContext.request.contextPath}/pet/${req.pet.id},${req.pet.petName}"/>
                                        <small class="text-warning"> ${creationDate}</small>
                                    </div>
                                    <div class="col-lg-3">


                                        <c:choose>
                                            <c:when test="${req.status.value eq ACCEPTED}">
                                                <spring:message code="request.accepted"/> <spring:message code="pet.status.notSold"/>
                                            </c:when>

                                            <c:when test="${req.status.value eq SOLD}">
                                                <spring:message code="request.accepted"/>
                                                <spring:message code="pet.status.currentlySold.short" arguments="${pageContext.request.contextPath}/user/${req.pet.newOwner.id},${req.pet.newOwner.username}"/>
                                            </c:when>

                                            <c:when test="${req.status.value eq CANCELED}">
                                                <spring:message code="request.canceled"/>
                                            </c:when>

                                            <c:when test="${req.status.value eq REJECTED}">
                                                <spring:message code="request.rejected"/>
                                            </c:when>

                                        </c:choose>
                                    </div>
                                    <div class="col text-center button-container">

                                            <a href="${pageContext.request.contextPath}/pet/<c:out value="${req.pet.id}"/>"
                                               type="button" class="btn btn-secondary "><spring:message
                                                    code="visitPet"/></a>
                                            <a href="${pageContext.request.contextPath}/user/<c:out value="${req.user.id}"/>"
                                               type="button" class=" btn btn-secondary"><spring:message
                                                    code="visitUser"/></a>
                                            <c:if test="${req.status.value eq ACCEPTED}">
                                                <form action="${pageContext.request.contextPath}/pet/${req.pet.id}/sell-adopt"
                                                      method="post" enctype="multipart/form-data" >
                                                    <input type="hidden" name="newowner" value="${req.user.id}"/>
                                                    <spring:message code="sell" var="submitText"/>
                                                    <input type="submit" class="btn btn-success are-you-sure" value="${submitText}"/>
                                                </form>
                                            </c:if>

                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>
                        <div class="m-2">
                            <hr>
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/interests'}"/>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="modal fade" id="accept-request" tabindex="-1" role="dialog" aria-labelledby="acceptTitle"
             aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 class="modal-title" id="acceptTitle"><spring:message code="areYouSure.title"/></h3>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p><spring:message code="areYouSure.body.interests"/></p>
                    </div>
                    <div class="modal-footer button-container">
                        <button type="button" class="btn btn-danger"><spring:message code="areYouSure.accept"/></button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="areYouSure.decline"/></button>
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