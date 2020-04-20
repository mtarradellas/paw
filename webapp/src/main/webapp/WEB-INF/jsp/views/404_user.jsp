<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="notFound" var="titleVar"/>

<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="center">
            <img src="<c:url value="/resources/images/user_not_found.png"/>" alt="user_not_found" height="600" width="800"/>
        </div>
        <div class="center">
            <h1 class="align-content-center align"><b><spring:message code="notFound.user"/></b> </h1>
        </div>
        <div class="center">
            <h3><a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a></h3>
        </div>

    </jsp:body>
</t:basicLayout>
