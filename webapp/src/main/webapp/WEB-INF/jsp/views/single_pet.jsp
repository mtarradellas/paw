<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="petName" value="${pet.petName}" />
<c:set var="cprice" value="${pet.price}" />
<c:if test="${pet.gender eq 'male'}"><spring:message var="pronoun" code="pet.him"/>  </c:if>
<c:if test="${pet.gender eq 'female' }"><spring:message var="pronoun" code="pet.her"/>  </c:if>

<spring:message code="petCard.someInfo" arguments="${pronoun}" var="someInfo"/>
<spring:message code="petCard.meet" arguments="${petName}" var="meet"/>
<spring:message code="argPrice" arguments="${cprice}" var="price"/>
<spring:message code="petTitle" var="titleVar"/>

<t:basicLayout title="${titleVar}">
    <div class="row ">
        <div class=" col-md-10 offset-md-1">
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

            <div class="bg-light shadow p-3">
                <div class="p-2">
                    <div class="row">
                        <c:if test="${not empty pet.petName}">
                            <h1>
                                <c:out value="${meet}" />
                            </h1>
                        </c:if>
                        <c:if test="${empty pet.petName}">
                            <h1>
                                <spring:message code="petCard.giveName" arguments="${pronoun}"/>
                            </h1>
                        </c:if>
                        <c:if test="${(pet.ownerId ne loggedUser.id)}">

                            <c:if test="${not requestExists}">
                                <h1 class="mt-2 ml-4">
                                    <form method="POST" class="m-0" action="<c:url value="/pet/${id}/request" />">
                                        <button type="submit" name="action" class="btn btn-success">
                                            <i class="fas fa-plus mr-2"></i>
                                            <spring:message code="petCard.showInterest"/>
                                        </button>
                                    </form>
                                </h1>
                            </c:if>
                            <c:if test="${requestExists}">
                                <h1 class="mt-2 ml-4">
                                    <button type="button" class="btn btn-success" disabled>
                                        <spring:message code="petCard.alreadyRequested"/>
                                    </button>
                                </h1>
                            </c:if>
                        </c:if>
                        <c:if test="${(pet.ownerId eq loggedUser.id)}">
                            <h1 class="mt-2 ml-4">
                                <form method="POST" class="m-0" action="<c:url value="/pet/${id}/reserve" />">
                                    <button type="submit" name="action" class="btn btn-success">
                                        <spring:message code="petCard.reserve"/>
                                    </button>
                                </form>
                            </h1>
                            <h1 class="mt-2 ml-4">
                                <form method="POST" class="m-0" action="<c:url value="/pet/${id}/remove" />">
                                    <button type="submit" name="action" class="btn btn-danger">
                                        <i class="fas fa-times mr-2"></i>
                                        <spring:message code="petCard.remove"/>
                                    </button>
                                </form>
                            </h1>
                        </c:if>
                    </div>
                </div>
                <t:photosList images="${pet.images}"/>
                <div class="p-2">
                    <c:out value="${pet.description}"/>
                </div>
                <div class="p-2">
                    <h2><c:out value="${someInfo}"/></h2>

                    <ul class="list-group">
                        <li class="list-group-item"><spring:message code="petCard.name"/> <c:out value="${pet.petName}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.dob"/> <c:out value="${pet.birthDate}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.species"/> <c:out value="${pet.species.name}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.breed"/> <c:out value="${pet.breed.name}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.sex"/> <spring:message code="pet.${pet.gender}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.vaccinated"/> <spring:message code="yesNo.${pet.vaccinated}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.price"/> <spring:message code="argPrice" arguments="${pet.price}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.location"/> <c:out value="${pet.location}"/></li>
                        <li class="list-group-item"><spring:message code="petCard.uploadDate"/> <c:out value="${pet.uploadDate}"/></li>
                    </ul>

                </div>
                <c:set var="ownerId" value="${pet.ownerId}"/>
                <a href="${pageContext.request.contextPath}/user/${ownerId}" class="p-2 m-3"><spring:message code="petCard.gotoOwnerPage"/></a>

                <div class="p-4">
                    <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>