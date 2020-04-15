<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="breeds" value="<%=ar.edu.itba.paw.models.constants.BreedTypes.values()%>"/>

<spring:message code="availableTitle" var="titleVar"/>

<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="shadow p-3 mb-5 rounded card-color">
            <h1 class="title-style"><spring:message code="available"/></h1>
        </div>
        <div class="row mt-1">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-body">
                        <spring:message code="pet.dogs"/>
                        <ul>
                            <c:forEach items="${breeds}" var="breed" varStatus="i">
                                <c:if test="${breed.speciesType.name eq 'dog'}">
                                    <li><spring:message code="dog.${breed.name}"/></li>
                                </c:if>
                            </c:forEach>
                        </ul>
                        <spring:message code="pet.cats"/>
                        <ul>
                            <c:forEach items="${breeds}" var="breed" varStatus="i">
                                <c:if test="${breed.speciesType.name eq 'cat'}">
                                    <li ><spring:message code="cat.${breed.name}"/></li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="p-4">
            <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
        </div>
        </div>

    </jsp:body>
</t:basicLayout>