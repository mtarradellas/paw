<%@ page import="ar.edu.itba.paw.models.constants.UserStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>
<spring:message code="adminTitle.user" var="userTitle"/>

<c:set var="ACTIVE" value="<%=UserStatus.ACTIVE.getValue()%>"/>
<c:set var="INACTIVE" value="<%=UserStatus.INACTIVE.getValue()%>"/>
<c:set var="DELETED" value="<%=UserStatus.DELETED.getValue()%>"/>

<t:adminLayout title="${userTitle}" item="users">
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

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get"
                          action="${pageContext.request.contextPath}/admin/users">

                        <div class="card-body">
                            <div class="form-group">
                                <label for="filter-status"><spring:message code="status"/></label>
                                <select name="status" class="form-control" id="filter-status">
                                    <option value="any"
                                            <c:if test="${(not empty param.status) && (param.status eq 'any') ||
                                            nanStatus}">
                                                selected
                                            </c:if>
                                    >
                                        <spring:message code="filter.any"/>
                                    </option>
                                    <option value="${ACTIVE}"
                                            <c:if test="${(not empty param.status) && !nanStatus && (param.status ne 'any') && (param.status eq ACTIVE)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.active"/></option>
                                    <option value="${INACTIVE}"
                                            <c:if test="${(not empty param.status) && !nanStatus && (param.status ne 'any') && ( param.status eq INACTIVE)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.inactive"/></option>
                                    <option value="${DELETED}"
                                            <c:if test="${(not empty param.status) && !nanStatus && (param.status ne 'any') && ( param.status eq DELETED)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.deleted"/></option>

                                </select>
                            </div>
                            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
                            <select name="searchCriteria" class="form-control" id="search-criteria">
                                <option value="any"><spring:message code="filter.any"/></option>
                                <option value="mail"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'mail')}">selected</c:if>
                                ><spring:message code="register.email"/></option>
                                <option value="username"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'username')}">selected</c:if>
                                ><spring:message code="request.username"/></option>
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
                            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/users">
                                <spring:message code="filter.clear"/>
                            </a>
                        </div>
                    </form>
                </div>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <div class="col">
                                <c:if test="${empty userList }">
                                    <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                        <a href="${pageContext.request.contextPath}/admin/users"><spring:message code="showFirst"/></a>
                                    </div>
                                </c:if>
                                <c:if test="${not empty userList}">
                                    <div>
                                        <h2><spring:message code="admin.usersListing" /> <spring:message code="totalResults" arguments="${amount}"/>
                                            <a type="button" class="btn btn-success"
                                               href="${pageContext.request.contextPath}/admin/upload-user">
                                                <i class="fas fa-plus mr-2"></i><spring:message code="addUser"/></a>
                                        </h2>
                                    </div>
                                </c:if>
                            </div>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right mb-1"
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>
                        </div>


                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admin/users/'}" />
                            </c:if>
                        </div>
                        <div>
                            <c:if test="${not empty userList}">
                            <div class="row">
                                <div class="col-lg-7">
                                    <h5 class="text-left ml-4"><b><spring:message code="user"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            </c:if>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="user" items="${userList}">
                                    <%--                  :)                  Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li     <c:if test="${(user.status.value eq ACTIVE) or (user.status.value eq INACTIVE)}">
                                                class="list-group-item"
                                            </c:if>
                                            <c:if test="${ (user.status.value eq DELETED)}">
                                                class="list-group-item resolved"
                                            </c:if>
                                    >
                                        <div class="row ">
                                            <div class="col-lg-7">
                                                <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>">
                                                    <c:out value="${user.username}"/> - <c:out value="${user.mail}"/>
                                                </a>
                                            </div>
                                            <div class="col text-center ml-3">
                                                <c:if test="${(user.status.value eq ACTIVE) or (user.status.value eq INACTIVE)}">
                                                    <form method="POST" class="m-0" action="<c:url value="/admin/user/${user.id}/remove"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>" type="button" class="btn btn-secondary"><spring:message code="visitUser"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                                        <button type="submit" class="btn btn-danger are-you-sure"><spring:message code="petCard.remove"/></button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${ (user.status.value eq DELETED)}">
                                                    <form method="POST" class="m-0" action="<c:url value="/admin/user/${user.id}/recover"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>" type="button" class="btn btn-secondary"><spring:message code="visitUser"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                                        <button type="submit" class="btn btn-success"><spring:message code="petCard.recover"/></button>
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
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admin/users/'}" />
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
    </jsp:body>
</t:adminLayout>