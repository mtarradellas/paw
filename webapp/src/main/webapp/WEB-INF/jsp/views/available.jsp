<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="dogBreeds" value="<%=ar.edu.itba.paw.models.constants.DogBreeds.values()%>"/>
<c:set var="catBreeds" value="<%=ar.edu.itba.paw.models.constants.CatBreeds.values()%>"/>

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
                            <c:forEach items="${dogBreeds}" var="breed" varStatus="i">
                                <li><spring:message code="dog.${breed.name}"/></li>
                            </c:forEach>
                        </ul>
                        <spring:message code="pet.cats"/>
                        <ul>
                            <c:forEach items="${catBreeds}" var="breed" varStatus="i">
                                <li ><spring:message code="cat.${breed.name}"/></li>
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