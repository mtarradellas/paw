<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="adminTitle.user" var="userTitle"/>
<t:adminLayout title="${userTitle}" item="users">
    <jsp:body>
        <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage' javaScriptEscape='true' />
        </span>
        <div class="container-fluid">
            <div class="row">

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get"
                          action="${pageContext.request.contextPath}/admin/users">
                        <div class="card-header">
                            <h5 class="card-title"><spring:message code="filter.options"/></h5>
                        </div>
                        <div class="card-body">
                            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter"/></h6>
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
                                    <option value="active"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('active' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.active"/></option>
                                    <option value="inactive"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('inactive' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.inactive"/></option>
                                    <option value="deleted"
                                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('deleted' eq param.status)}">
                                                selected
                                            </c:if>
                                    ><spring:message code="status.deleted"/></option>

                                </select>
                            </div>
                            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter.orderBy"/></h6>
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
                        </div>
                    </form>
                </div>

                <div class="col-lg-8">
                    <div class="shadow p-3 bg-white rounded">

                        <c:if test="${empty users_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/admin/users"><spring:message code="showFirst"/></a>
                            </div>
                        </c:if>
                        <c:if test="${not empty users_list}">
                            <div>
                                <h2><spring:message code="admin.usersListing" /> <spring:message code="showingResults" arguments="${users_list.size()}"/>
                                    <a type="button" class="btn btn-success"
                                       href="${pageContext.request.contextPath}/admin/upload-user">
                                        <i class="fas fa-plus mr-2"></i><spring:message code="addUser"/></a>
                                </h2>
                            </div>
                        </c:if>

                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admin/users/'}" />
                            </c:if>
                        </div>
                        <div>
                            <c:if test="${not empty users_list}">
                            <div class="row">
                                <div class="col-lg-8">
                                    <h5 class="text-left ml-4"><b><spring:message code="user"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            </c:if>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="user" items="${users_list}">
                                    <%--                                    Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li     <c:if test="${(user.status.id eq 1) or (user.status.id eq 2)}">
                                                class="list-group-item"
                                            </c:if>
                                            <c:if test="${ (user.status.id eq 3)}">
                                                class="list-group-item resolved"
                                            </c:if>
                                    >
                                        <div class="row ">
                                            <div class="col-lg-8">
                                                <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>">
                                                    <c:out value="${user.username}"/> - <c:out value="${user.mail}"/>
                                                </a>
                                            </div>
                                            <div class="col text-center ml-3">
                                                <c:if test="${(user.status.id eq 1) or (user.status.id eq 2)}">
                                                    <form method="POST" class="m-0" action="<c:url value="/admin/user/${user.id}/remove"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>" type="button" class="btn btn-secondary"><spring:message code="visitUser"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                                        <button type="submit" onclick="confirmDelete(event)" class="btn btn-danger"><spring:message code="petCard.remove"/></button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${ (user.status.id eq 3)}">
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
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <h4><b><spring:message code="guide.role"/></b></h4>
                        <p><spring:message code="guide.role.description"/></p>
                        <h4><b><spring:message code="guide.color"/></b></h4>
                        <p><spring:message code="guide.color.description"/></p>
                    </div>
                </div>
            </div>
        </div>

    </jsp:body>
</t:adminLayout>