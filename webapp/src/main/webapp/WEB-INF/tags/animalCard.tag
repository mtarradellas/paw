<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="card animal-list-card" style="width: 18rem;">
    <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
         class="card-img-top" alt="" width="200" height="200">
    <div class="card-body">
        <p class="card-text">
            <spring:message code="pet.name"/>  ${pet.petName}<br>
            <spring:message code="pet.breed"/> ${pet.breed}<br>
            <spring:message code="pet.price"/> $${pet.price}
        </p>
    </div>
    <div class="card-body">
        <a href="#" class="card-link"><spring:message code="pet.goToPage"/></a>
    </div>
</div>