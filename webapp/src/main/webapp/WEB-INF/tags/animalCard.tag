<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<c:set var="cprice" scope="application" value="${pet.price}"/>

<spring:message code="argPrice" arguments="${cprice}" var="price"/>

<div class="card animal-list-card">
    <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link">

    <img src="<c:out value="${pageContext.request.contextPath}/img/${pet.images[0]}"/>"
         class="card-img-top" alt="">

    </a>
    <div class="card-body">

        <p class="card-text">
            <spring:message code="petCard.name"/> <c:out value="${pet.petName}"/><br>
            <spring:message code="petCard.species"/> <c:out value="${pet.species.name}"/><br>
            <spring:message code="petCard.breed"/> <c:out value="${pet.breed.name}"/><br>
            <spring:message code="petCard.price"/> <spring:message code="argPrice" arguments="${pet.price}"/><br>
            <spring:message code="petCard.sex"/> <spring:message code="pet.${pet.gender}"/>
        </p>

        <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link"><spring:message code="petCard.goToPage"/></a>

    </div>
    <div class="card-footer">
        <h6><spring:message code="petCard.uploadDate"/> <c:out value="${pet.uploadDate}"/></h6>
    </div>
</div>
