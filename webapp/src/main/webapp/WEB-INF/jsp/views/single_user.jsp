<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="user.title" var="title"/>

<t:centeredLayout title="${title}">

    <div class="p-2">
        <h1><c:out value="${user.username}"/></h1>
    </div>
    <div class="p-2">
        <h2><spring:message code="data"/></h2>
        <ul class="list-group">
            <li class="list-group-item"><spring:message code="user.username"/>: <c:out value="${user.username}"/></li>
            <li class="list-group-item"><spring:message code="user.password"/> <c:out value="${user.password}"/></li>
            <li class="list-group-item"><spring:message code="user.email"/> <c:out value="${user.mail}"/></li>
            <li class="list-group-item"><spring:message code="user.phone"/> <c:out value="${user.phone}"/></li>
        </ul>
    </div>

    <div class="p-4">
        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
    </div>

    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:centeredLayout>