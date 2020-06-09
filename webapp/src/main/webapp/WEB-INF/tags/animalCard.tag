<%@ tag import="ar.edu.itba.paw.models.constants.PetStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8" %>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet" %>
<%@attribute name="level" required="true" type="java.lang.String" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="AVAILABLE" value="<%=PetStatus.AVAILABLE.getValue()%>"/>
<c:set var="REMOVED" value="<%=PetStatus.REMOVED.getValue()%>"/>
<c:set var="SOLD" value="<%=PetStatus.SOLD.getValue()%>"/>
<c:set var="UNAVAILABLE" value="<%=PetStatus.UNAVAILABLE.getValue()%>"/>

<c:set var="cprice" scope="application" value="${pet.price}"/>
<fmt:formatDate value="${pet.uploadDate}" var="date" type="date" pattern="dd-MM-yyyy"/>

<spring:message code="argPrice" arguments="${cprice}" var="price"/>

<div class="card animal-list-card">
    <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>" class="card-link">
        <img src="<c:out value="${pageContext.request.contextPath}/img/${pet.images[0].id}"/>"
             class="card-img-top" alt="">
    </a>

    <div class="card-body">
        <p class="card-text">
            <c:if test="${not empty pet.petName}">
                <spring:message code="petCard.name"/> <c:out value="${pet.petName}"/><br>
            </c:if>
            <spring:message code="petCard.species"/> <c:out value="${pet.species.name}"/><br>
            <spring:message code="petCard.breed"/> <c:out value="${pet.breed.name}"/><br>
            <spring:message code="petCard.price"/> <spring:message code="argPrice" arguments="${pet.price}"/><br>
            <spring:message code="petCard.sex"/> <spring:message code="pet.${pet.gender}"/><br>
            <spring:message code="petCard.owner"/> <a href="${pageContext.request.contextPath}/user/${pet.user.id}">
            <c:out value="${pet.user.username}"/></a>
        </p>

        <c:if test="${level eq 'admin'}">
            <div class="row">
                <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${pet.id}"/>"
                   class="darkblue-action ml-2"><spring:message code="petCard.goToPage"/></a>
                <c:if test="${pet.status.value eq REMOVED}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.removed"/>)</h5>
                </c:if>
                <c:if test="${pet.status.value eq SOLD}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.sold"/>)</h5>
                </c:if>
                <c:if test="${pet.status.value eq UNAVAILABLE}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.unavailable"/>)</h5>
                </c:if>


            </div>
        </c:if>
        <c:if test="${level eq 'user'}">
            <div class="row">
                <a href="${pageContext.request.contextPath}/pet/<c:out value="${pet.id}"/>"
                   class="darkblue-action ml-2"><spring:message code="petCard.goToPage"/></a>
                <c:if test="${pet.status.value eq REMOVED}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.removed"/>)</h5>
                </c:if>
                <c:if test="${pet.status.value eq SOLD}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.sold"/>)</h5>
                </c:if>
                <c:if test="${pet.status.value eq UNAVAILABLE}">
                    <h5 class="mt-2 ml-1">(<spring:message code="status.unavailable"/>)</h5>
                </c:if>
            </div>
        </c:if>
    </div>
    <div class="card-footer">
        <h6><spring:message code="petCard.uploadDate"/> <c:out value="${date}"/></h6>
    </div>
</div>
