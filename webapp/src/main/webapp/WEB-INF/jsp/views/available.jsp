<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="availableTitle" var="titleVar"/>

<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="row">
            <div class="col-md-10 offset-md-1">
                <div class="shadow p-3 bg-white rounded">
                    <div class="shadow p-3 mb-5 rounded card-color">
                        <h1 class="title-style"><spring:message code="available"/></h1>
                    </div>
                    <div class="row mt-1">
            <div class="col-md-12">
                <div class="card">
                        <c:forEach items="${speciesList}" var="species">
                            <div class="card-body">
                            ${species.name}
                            <ul>
                                <c:forEach items="${breedList}" var="breed" varStatus="i">
                                    <c:if test="${breed.species.id eq species.id}">
                                        <li>${breed.name}</li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                            </div>
                        </c:forEach>
                </div>
            </div>
        </div>
                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                    </div>
                </div>
            </div>
        </div>

    </jsp:body>
</t:basicLayout>