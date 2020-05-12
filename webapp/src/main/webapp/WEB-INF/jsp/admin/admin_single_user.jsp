<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="userTitle"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<t:adminLayout title="${userTitle}" item="user">
    <jsp:body>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

        <div class="row">
            <div class=" col-md-10 offset-md-1">

                <div class="bg-light shadow p-3">
                    <div class="p-2 row">
                        <h1><c:out value="${user.username}"/>
                            (<c:out value="${user.status.name}"/>)
                        </h1>
                        <c:if test="${(user.status.id eq 1) or (user.status.id eq 2)}">
                            <h1 class="mt-2 ml-4">
                                <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${user.id}"/>/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                            </h1>
                            <h1 class="mt-2 ml-2">
                                <form method="POST" class="m-0" action="<c:url value="/admin/user/${user.id}/remove"/>">
                                    <button type="submit" class="btn btn-danger are-you-sure"><spring:message code="petCard.remove"/></button>
                                </form>
                            </h1>
                        </c:if>
                        <c:if test="${user.status.id eq 3}">
                            <h1 class="mt-2 ml-2">
                                <form method="POST" class="m-0" action="<c:url value="/admin/user/${user.id}/recover"/>">
                                    <button type="submit" class="btn btn-success"><spring:message code="petCard.recover"/></button>
                                </form>
                            </h1>
                        </c:if>

                    </div>

                    <div class="p-2">
                        <ul class="list-group">
                            <li class="list-group-item"><spring:message code="admin.petCard.id"/> <c:out value="${user.id}"/></li>
                            <li class="list-group-item"><spring:message code="user.email"/> <c:out value="${user.mail}"/></li>
                            <li class="list-group-item"><spring:message code="user.phone"/> <c:out value="${user.phone}"/></li>
                        </ul>
                        <div class="p-2">
                            <h2><spring:message code="pets"/></h2>
                            <c:if test="${not empty userPets}">
                                <div class="m-2 ">
                                    <c:if test="${maxPage ne 1}">
                                        <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/admin/user/${id}"/>
                                    </c:if>
                                </div>
                            </c:if>
                            <div class="card-deck row">

                                <c:forEach var="pet" items="${userPets}">
                                    <c:if test="${pet.status.id eq 1}">
                                    <div class="col-auto mb-3">
                                        </c:if>
                                        <c:if test="${pet.status.id ne 1 }">
                                        <div class="col-auto mb-3 resolved">
                                            </c:if>
                                        <t:animalCard pet="${pet}" level="admin"/>

                                    </div>
                                </c:forEach>
                                <c:if test="${empty userPets}">
                                    <div class="col-auto">
                                        <spring:message code="noPetsFound"/>
                                    </div>
                                </c:if>

                            </div>
                            <c:if test="${not empty userPets}">
                                <div class="m-2 ">
                                    <c:if test="${maxPage ne 1}">
                                        <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/admin/user/${id}"/>
                                    </c:if>
                                </div>
                            </c:if>

                        </div>
                    </div>

                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                    </div>
                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>

    </jsp:body>
</t:adminLayout>