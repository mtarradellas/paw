<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<t:basicLayout title="Informacion sobre la mascota">
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
                <li class="list-group-item"><spring:message code="petCard.name"/> ${pet.petName}</li>
                <li class="list-group-item"><spring:message code="petCard.dob"/> ${pet.birthDate}</li>
                <li class="list-group-item"><spring:message code="petCard.species"/> ${pet.species}</li>
                <li class="list-group-item"><spring:message code="petCard.breed"/> ${pet.breed}</li>
                <li class="list-group-item"><spring:message code="petCard.sex"/> ${pet.gender}</li>
                <li class="list-group-item"><spring:message code="petCard.vaccinated"/> ${pet.vaccinated}</li>
                <li class="list-group-item"><spring:message code="petCard.price"/> ${pet.price}</li>
                <li class="list-group-item"><spring:message code="petCard.location"/> ${pet.location}</li>
                <li class="list-group-item"><spring:message code="petCard.description"/> ${pet.description}</li>
            </ul>
        </div>
    </div>

    <div class="p-4">
        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
    </div>

    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>