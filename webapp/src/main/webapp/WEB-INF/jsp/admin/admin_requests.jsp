<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>
<spring:message code="adminTitle.request" var="requestTitle"/>

<t:adminLayout title="${requestTitle}" item="requests">
    <jsp:body>
        <c:if test="${wrongSearch}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <spring:message code="wrongSearch"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

        <div class="container-fluid">
            <div class="row">
                    <%--                Filter Tools --%>

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get"
                          action="${pageContext.request.contextPath}/admin/requests">

                        <div class="card-body">
                            <div class="form-group">
                                <label for="filter-status"><spring:message code="request.status"/></label>
                                <select name="status" class="form-control" id="filter-status">
                                    <option value="any"
                                            <c:if test="${(not empty param.status) && (param.status eq 'any')}">
                                                selected
                                            </c:if>
                                    >
                                        <spring:message code="filter.any"/>
                                    </option>
                                    <option value="accepted"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('accepted' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.accepted"/></option>
                                    <option value="rejected"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('rejected' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.rejected"/></option>
                                    <option value="pending"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('pending' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="request.pending"/></option>
                                    <option value="canceled"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('canceled' eq param.status)}">
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
                        </div>
                    </form>
                </div>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <div class="col">
                                <c:if test="${empty requests_list }">
                                    <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                        <a href="${pageContext.request.contextPath}/admin/requests"><spring:message
                                                code="showFirst"/></a>
                                    </div>
                                </c:if>
                                <c:if test="${not empty requests_list}">
                                    <div>
                                        <h2><spring:message code="admin.requestsListing"/>
                                            <a type="button" class="btn btn-success"
                                               href="${pageContext.request.contextPath}/admin/upload-request">
                                                <i class="fas fa-plus mr-2"></i><spring:message code="addRequest"/></a>
                                        </h2>
                                    </div>
                                </c:if>
                            </div>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right "
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>
                        </div>


                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                              baseURL="${'/admin/requests/'}"/>
                            </c:if>
                        </div>
                        <div>
                            <c:if test="${not empty requests_list}">
                                <div class="row">
                                    <div class="col-lg-5">
                                        <h5 class="text-left ml-4"><b><spring:message code="request"/></b></h5>
                                    </div>
                                    <div class="col-lg-2">
                                        <h5 class="text-left"><b><spring:message code="request.status"/></b></h5>
                                    </div>
                                    <div class="col">
                                        <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                    </div>
                                </div>
                            </c:if>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="request" items="${requests_list}">
                                    <%--                                    Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li     <c:if
                                            test="${(request.status.id eq 1) or (request.status.id eq 2) or (request.status.id eq 3)}">
                                        class="list-group-item"
                                    </c:if>
                                            <c:if test="${(request.status.id eq 4)}">
                                                class="list-group-item resolved"
                                            </c:if>
                                    >
                                        <div class="row ">
                                            <div class="col-lg-5">
                                                <spring:message code="request.isInterested"
                                                                arguments="${pageContext.request.contextPath}/admin/user/${request.ownerId}, ${request.ownerUsername}, ${pageContext.request.contextPath}/admin/pet/${request.petId},${request.petName}"/>
                                                <small class="text-warning"> ${request.creationDate}</small>
                                            </div>
                                            <div class="col-lg-2">
                                                <c:if test="${request.status.id eq 1}">
                                                    <spring:message code="request.pending"/>
                                                </c:if>
                                                <c:if test="${request.status.id eq 2}">
                                                    <spring:message code="request.accepted"/>
                                                </c:if>
                                                <c:if test="${request.status.id eq 3}">
                                                    <spring:message code="request.rejected"/>
                                                </c:if>
                                                <c:if test="${request.status.id eq 4}">
                                                    <spring:message code="request.canceled"/>
                                                </c:if>
                                            </div>
                                            <div class="col text-center ml-3 ">
                                                <c:if test="${request.status.id eq 1 or request.status.id eq 2 or request.status.id eq 3}">
                                                    <form method="POST" class="m-0"
                                                          action="<c:url value="/admin/request/${request.id}/cancel"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${request.ownerId}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitUser"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${request.petId}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitPet"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/request/<c:out value="${request.id}"/>/edit"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="edit"/></a>
                                                        <button type="submit" class="btn btn-danger are-you-sure">
                                                            <spring:message code="cancel"/></button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${request.status.id eq 4}">
                                                    <form method="POST" class="m-0"
                                                          action="<c:url value="/admin/request/${request.id}/recover"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${request.ownerId}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitUser"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${request.petId}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitPet"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/request/<c:out value="${request.id}"/>/edit"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="edit"/></a>
                                                        <button type="submit"
                                                                class="btn btn-success"><spring:message
                                                                code="petCard.recover"/></button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="m-2">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                              baseURL="${'/admin/requests/'}"/>
                            </c:if>
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
                                <h4><b><spring:message code="guide.role"/></b></h4>
                                <p><spring:message code="guide.role.description"/></p>
                                <h4><b><spring:message code="guide.color"/></b></h4>
                                <p><spring:message code="guide.color.description"/></p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:adminLayout>