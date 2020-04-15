<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<c:set var="cprice" scope="application" value="${pet.price}"/>
<c:set var="cgender" scope="application" value="${pet.gender}"/>

<spring:message code="${pet.species}.${pet.breed}" var="breed" />
<spring:message code="pet.${pet.species}" var="species" />
<spring:message code="argPrice" arguments="${cprice}" var="price"/>
<spring:message code="pet.${cgender}" var="gender"/>

<div class="card animal-list-card">
    <img src="<c:out value="${pet.images[0].url}"/>"
         class="card-img-top" alt="">
    <div class="card-body">

        <p class="card-text">
            <spring:message code="petCard.name"/> <c:out value="${pet.petName}"/><br>
            <spring:message code="petCard.species"/> <c:out value="${species}"/><br>
            <spring:message code="petCard.breed"/> <c:out value="${breed}"/><br>
            <spring:message code="petCard.price"/> <c:out value="${price}"/><br>
            <spring:message code="petCard.sex"/> <c:out value="${gender}"/>
        </p>

        <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link"><spring:message code="petCard.goToPage"/></a>

    </div>
</div>