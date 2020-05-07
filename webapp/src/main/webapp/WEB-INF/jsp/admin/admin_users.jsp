<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="adminTitle.user" var="userTitle"/>
<t:adminLayout title="${userTitle}">
    <jsp:body>
        <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage' javaScriptEscape='true' />
        </span>
        <div class="container-fluid">
            <div class="row">
                <t:search-tools-pet breeds_list="${breeds_list}" species_list="${species_list}"/>
                <div class="col-lg-8">
                    <div class="shadow p-3 bg-white rounded">

                        <c:if test="${empty users_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/admi/users"><spring:message code="showFirst"/></a>
                            </div>
                        </c:if>
                        <c:if test="${not empty users_list}">
                            <div>
                                <h2><spring:message code="admin.usersListing" /> <spring:message code="showingResults" arguments="${users_list.size()}"/>
                                    <button type="button" class="btn btn-success"><i class="fas fa-plus mr-2"></i><spring:message code="addUser"/></button>
                                </h2>
                            </div>
                        </c:if>

                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/users/'}" />
                            </c:if>
                        </div>
                        <div>
                            <div class="row">
                                <div class="col-lg-8">
                                    <h5 class="text-left ml-4"><b><spring:message code="user"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="user" items="${users_list}">
                                    <%--                                    Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li class="list-group-item ">
                                        <div class="row ">
                                            <div class="col-lg-8">
                                                <a href="${pageContext.request.contextPath}/admi/user/{${user.id}}">
                                                        ${user.username} - ${user.mail}
                                                </a>
                                            </div>
                                            <div class="col text-center ml-3">
                                                <form method="POST" class="m-0" action="<c:url value="/admi/user/${user.id}/delete"/>">
                                                    <a href="${pageContext.request.contextPath}/admi/user/{${user.id}}" type="button" class="btn btn-secondary"><spring:message code="visitUser"/></a>
                                                    <a href="${pageContext.request.contextPath}/admi/user/{${user.id}}/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                                    <button type="submit" onclick="confirmDelete(event)" class="btn btn-danger"><spring:message code="delete"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="m-2">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/users/'}" />
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