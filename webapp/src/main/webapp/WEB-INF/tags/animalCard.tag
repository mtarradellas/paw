<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<div class="card animal-list-card">
    <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
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