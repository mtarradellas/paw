<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<c:set var="cprice" scope="application" value="${pet.price}"/>

<spring:message code="argPrice" arguments="${cprice}" var="price"/>


<div class="card animal-list-card">

    <img src="<c:out value="${pet.images[0].url}"/>"
         class="card-img-top" alt="">
    <div class="card-body">

        <p class="card-text">
            <c:if test="${not empty pet.petName}">
                <spring:message code="petCard.name"/> <c:out value="${pet.petName}"/><br>
            </c:if>
            <spring:message code="petCard.species"/> <c:out value="${pet.species.name}"/><br>
            <spring:message code="petCard.breed"/> <c:out value="${pet.breed.name}"/><br>
            <spring:message code="petCard.price"/> <spring:message code="argPrice" arguments="${pet.price}"/><br>
            <spring:message code="petCard.sex"/> <spring:message code="pet.${pet.gender}"/>
        </p>

        <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link"><spring:message code="petCard.goToPage"/></a>

    </div>
</div>