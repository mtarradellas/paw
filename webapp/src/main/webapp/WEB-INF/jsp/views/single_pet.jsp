<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="cbreed" scope="application" value="${pet.breed}"/>
<c:set var="cspecies" scope="application" value="${pet.species}"/>
<c:set var="cprice" scope="application" value="${pet.price}"/>

<spring:message code="petTitle" var="titleVar"/>
<spring:message code="${cspecies}.${cbreed}" var="breed"/>
<spring:message code="argPrice" arguments="${cprice}" var="price"/>

<t:basicLayout title="${petTitle}">
    <div class="modal fade" id="image-modal" tabindex="-1" role="dialog" aria-labelledby="full-image" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <img src="" alt="Full sized image"/>
                </div>
            </div>
        </div>
    </div>

    <div class="shadow p-3">
        <div class="p-2">
            <h2><spring:message code="petCard.photos"/></h2>
            <t:photosList ids="${ids}"/>
        </div>
        <div class="p-2">
            <h2><spring:message code="data"/></h2>
            <ul class="list-group">
                <li class="list-group-item"><spring:message code="petCard.name"/> <c:out value="${pet.petName}"/></li>
                <li class="list-group-item"><spring:message code="petCard.dob"/> <c:out value="${pet.birthDate}"/></li>
                <li class="list-group-item"><spring:message code="petCard.species"/> <c:out value="${pet.species}"/></li>
                <li class="list-group-item"><spring:message code="petCard.breed"/> <c:out value="${breed}"/></li>
                <li class="list-group-item"><spring:message code="petCard.sex"/> <c:out value="${pet.gender}"/></li>
                <li class="list-group-item"><spring:message code="petCard.vaccinated"/> <c:out value="${pet.vaccinated}"/></li>
                <li class="list-group-item"><spring:message code="petCard.price"/> <c:out value="${price}"/></li>
                <li class="list-group-item"><spring:message code="petCard.location"/> <c:out value="${pet.location}"/></li>
                <li class="list-group-item"><spring:message code="petCard.description"/> <c:out value="${pet.description}"/></li>
            </ul>
        </div>
    </div>

    <div class="p-4">
        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
    </div>

    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>