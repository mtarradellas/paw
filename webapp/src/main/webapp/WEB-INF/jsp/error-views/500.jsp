<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="serverError" var="titleVar"/>

<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="center">
            <h2 class="align-content-center align"><b><spring:message code="serverError.page"/></b> </h2>
        </div>
        <div class="center">
            <h4><a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a></h4>
        </div>

    </jsp:body>
</t:basicLayout>