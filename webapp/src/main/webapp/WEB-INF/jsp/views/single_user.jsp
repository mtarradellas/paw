<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="title"/>

<t:centeredLayout title="${title}">

    <div class="p-2">
        <h1><c:out value="${user.username}"/>
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
        </c:if>
        <div class="p-2">
            <h2><spring:message code="pets"/></h2>
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
                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="'/user/${id}'"/>
            </c:if>

        </div>
    </div>

    <div class="p-4">
        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
    </div>

    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:centeredLayout>