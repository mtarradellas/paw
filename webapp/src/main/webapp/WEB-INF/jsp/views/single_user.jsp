<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="title"/>

<t:basicLayout title="${title}">
    <div class="row ">
        <div class=" col-md-10 offset-md-1">
            <div class="bg-light shadow p-3">
                <div class="p-2">
                    <h1>
                        <c:out value="${user.username}"/></h1>
                        <%--            TODO: check if logged user equaks id from user page, if true, show button that redirects to edit user page
                                        TODO: when redirected, check again if logged user is the one from the id, if it is not then disable the page--%>
                        <%--            <a href="${pageContext.request.contextPath}/editUser/${user.id}"><spring:message code="editUser"/></button></h1>--%>

                </div>
                <div class="p-2">
                    <c:if test="${loggedUser.id eq user.id}">
                        <ul class="list-group">
                            <li class="list-group-item"><spring:message code="user.email"/> <c:out value="${user.mail}"/></li>
                            <li class="list-group-item"><spring:message code="user.phone"/> <c:out value="${user.phone}"/></li>
                        </ul>
                        <a class="edit-anchor" href="<c:url value="/edit-user/${loggedUser.id}"/>">
                            <spring:message code="editUserForm.edit"/>
                            <svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"></path>
                                <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"></path>
                            </svg>
                        </a>
                    </c:if>
                    <div class="p-2">
                        <h2><spring:message code="pets"/></h2>
                        <c:if test="${not empty userPets}">
                            <div class="m-2 ">
                                <c:if test="${maxPage ne 1}">
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/user/${id}"/>
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
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/user/${id}"/>
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
    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>