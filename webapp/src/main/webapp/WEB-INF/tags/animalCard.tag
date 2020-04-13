<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<div class="card animal-list-card">
    <img src="<c:out value="${pet.images[0].url}"/>"
         class="card-img-top" alt="">
    <div class="card-body">

        <p class="card-text">
            <spring:message code="petCard.name"/> <c:out value="${pet.petName}"/><br>
            <spring:message code="petCard.breed"/> <c:out value="${pet.breed}"/><br>
            <spring:message code="petCard.price"/> $<c:out value="${pet.price}"/>
        </p>

        <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link"><spring:message code="petCard.goToPage"/></a>

    </div>
</div>