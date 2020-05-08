<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="userTitle"/>
<t:adminLayout title="${userTitle}" item="user">
    <jsp:body>

        <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage' javaScriptEscape='true' />
        </span>
        <div class="row">
            <div class=" col-md-10 offset-md-1">

                <div class="bg-light shadow p-3">
                    <div class="p-2 row">
                        <h1><c:out value="${user.username}"/>
                                <%--            TODO: check if logged user equaks id from user page, if true, show button that redirects to edit user page
                                                TODO: when redirected, check again if logged user is the one from the id, if it is not then disable the page--%>
                                <%--            <a href="${pageContext.request.contextPath}/editUser/${user.id}"><spring:message code="editUser"/></button></h1>--%>
                        </h1>
                        <h1 class="mt-2 ml-4">
                            <a href="${pageContext.request.contextPath}/admi/user/<c:out value="${user.id}"/>/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                        </h1>
                        <h1 class="mt-2 ml-4">
                            <form method="POST" class="m-0" action="<c:url value="/admi/user/${user.id}/remove"/>">
                                <button type="submit" onclick="confirmDelete(event)" class="btn btn-danger"><spring:message code="petCard.remove"/></button>
                            </form>
                        </h1>

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
                                        <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/admi/user/${id}"/>
                                    </c:if>
                                </div>
                            </c:if>
                            <div class="card-deck row">

                                <c:forEach var="pet" items="${userPets}">
                                    <div class="col-auto mb-3">

                                        <t:animalCard pet="${pet}"/>

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
                                        <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/admi/user/${id}"/>
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

    </jsp:body>
</t:adminLayout>