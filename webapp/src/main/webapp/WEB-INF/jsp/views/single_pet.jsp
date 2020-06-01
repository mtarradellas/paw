<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="petName" value="${pet.petName}"/>
<c:set var="cprice" value="${pet.price}"/>
<c:if test="${pet.gender eq 'male'}"><spring:message var="pronoun" code="pet.him"/> </c:if>
<c:if test="${pet.gender eq 'female' }"><spring:message var="pronoun" code="pet.her"/> </c:if>

<spring:message code="petCard.someInfo" arguments="${pronoun}" var="someInfo"/>
<spring:message code="petCard.meet" arguments="${petName}" var="meet"/>
<spring:message code="argPrice" arguments="${cprice}" var="price"/>
<spring:message code="petTitle" var="titleVar"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<t:basicLayout title="${titleVar}">
    <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

    <div class="container-fluid">
        <div class=" col-md-10 offset-md-1 ">
            <div class="bg-light shadow ">
                <div class="p-2 bg-dark">
                    <div class="row text-whitesmoke">
                        <c:if test="${not empty pet.petName}">
                            <h1 class="ml-4 ">
                                <c:out value="${meet}"/>
                            </h1>
                        </c:if>
                        <c:if test="${empty pet.petName}">
                            <h1 class="ml-4 ">
                                <spring:message code="petCard.giveName" arguments="${pronoun}"/>
                            </h1>
                        </c:if>
                        <c:if test="${pet.status.value eq 3}">
                            <h1 class="ml-1 "> <spring:message code="pet.status.sold"/></h1>
                        </c:if>
                        <c:if test="${pet.status.value eq 2}">
                            <h1 class="ml-1"> <spring:message code="pet.status.removed"/> </h1>
                        </c:if>
                        <c:if test="${(pet.user.id ne loggedUser.id)}">
                            <c:if test="${not requestExists}">
                                <c:if test="${pet.status.value eq 1}">
                                    <h1 class="mt-2 ml-2">
                                        <form method="POST" class="m-0" action="<c:url value="/pet/${id}/request" />">
                                            <button type="submit" name="action" class="btn btn-success">
                                                <i class="fas fa-plus mr-2"></i>
                                                <spring:message code="petCard.showInterest"/>
                                            </button>
                                        </form>
                                    </h1>
                                </c:if>
                            </c:if>
                            <c:if test="${requestExists}">
                                <c:if test="${pet.status.value eq 1}">
                                    <h1 class="mt-2 ml-2">
                                        <button type="button" class="btn btn-success" disabled>
                                            <spring:message code="petCard.alreadyRequested"/>
                                        </button>
                                    </h1>
                                </c:if>
                            </c:if>
                        </c:if>
                        <c:if test="${(pet.user.id eq loggedUser.id)}">
                            <c:if test="${pet.status.value eq 1}">
                                <h1 class="mt-2 ml-4">
                                    <form method="POST" class="m-0" action="<c:url value="/pet/${id}/sell-adopt" />">
                                        <button type="submit" name="action" class="btn btn-success">
                                            <spring:message code="petCard.reserve"/>
                                        </button>
                                    </form>
                                </h1>
                                <h1 class="mt-2 ml-2">
                                    <form method="POST" class="m-0" action="<c:url value="/pet/${id}/remove" />">
                                        <button type="submit"  name="action"
                                                class="btn btn-danger are-you-sure">
                                            <i class="fas fa-times mr-2"></i>
                                            <spring:message code="petCard.remove"/>
                                        </button>
                                    </form>
                                </h1>
                                <a class="edit-anchor" href="<c:url value="/edit-pet/${id}"/>">
                                    <spring:message code="pet.edit"/>
                                    <svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"></path>
                                        <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"></path>
                                    </svg>
                                </a>
                            </c:if>
                            <c:if test="${pet.status.value ne 1}">
                                <h1 class="mt-2 ml-2">
                                    <form method="POST" class="m-0" action="<c:url value="/pet/${id}/recover" />">
                                        <button type="submit" name="action" class="btn btn-success">
                                            <spring:message code="petCard.recover"/>
                                        </button>
                                    </form>
                                </h1>
                            </c:if>
                        </c:if>
                    </div>
                </div>
                <hr>
                <div class="p-3">
                    <t:photosList images="${pet.images}"/>
                </div>
                <div class="p-3">
                    <c:out value="${pet.description}"/>
                </div>
                <hr>
                <div class="p-3">
                    <h2><c:out value="${someInfo}"/></h2>

                    <ul class="list-group">
                        <li class="list-group-item"><b><spring:message code="petCard.name"/></b> <c:out
                                value="${pet.petName}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.dob"/></b> <c:out
                                value="${pet.birthDate}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.species"/></b> <c:out
                                value="${pet.species.name}"/></
                        >
                        <li class="list-group-item"><b><spring:message code="petCard.breed"/></b> <c:out
                                value="${pet.breed.name}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.sex"/></b> <spring:message
                                code="pet.${pet.gender}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.province"/></b> <c:out
                                value="${pet.province.name}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.department"/></b> <c:out
                                value="${pet.department.name}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.uploadDate"/></b> <c:out
                                value="${pet.uploadDate}"/></li>
                    </ul>
                    <hr>
                    <h2><spring:message code="status"/></h2>
                    <ul class="list-group">
                        <c:if test="${pet.vaccinated eq true}">
                            <li class="list-group-item"><b><spring:message code="petCard.vaccinated"/><i
                                    class="fas fa-check ml-2 "></i>
                                (<spring:message code="yesNo.${pet.vaccinated}"/>)
                            </li>
                        </c:if>
                        <c:if test="${pet.vaccinated eq false}">
                            <li class="list-group-item"><b><spring:message code="petCard.vaccinated"/><i
                                    class="fas fa-times ml-2 "></i>
                                (<spring:message code="yesNo.${pet.vaccinated}"/>)
                            </li>
                        </c:if>

                        <c:if test="${pet.price eq 0}">
                            <li class="list-group-item"><b><spring:message code="petCard.forAdoption"/></b><i
                                    class="fas fa-check ml-2 "></i></li>
                            <li class="list-group-item"><b><spring:message code="petCard.forSale"/></b><i
                                    class="fas fa-times ml-2"></i></li>
                        </c:if>
                        <c:if test="${pet.price gt 0}">
                            <li class="list-group-item"><b><spring:message code="petCard.forAdoption"/></b><i
                                    class="fas fa-times ml-2"></i></li>
                            <li class="list-group-item"><b><spring:message code="petCard.forSale"/></b><i
                                    class="fas fa-check ml-2 "></i>
                                (<spring:message code="petCard.price"/> <spring:message code="argPrice"
                                                                                        arguments="${pet.price}"/>)
                            </li>
                        </c:if>
                    </ul>

                </div>
                <hr>
                <c:set var="ownerId" value="${pet.user.id}"/>
                <a href="${pageContext.request.contextPath}/user/${ownerId}"
                   class="btn darkblue-action p-2 m-3"><spring:message code="petCard.gotoOwnerPage"/></a>


                <div class="p-4">
                    <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                </div>
            </div>

            <div class="modal fade" id="image-modal" tabindex="-1" role="dialog" aria-labelledby="full-image"
                 aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <img id="img-preview" src="" alt="Full sized image"/>
                            <div id="arrows-wrapper-img">
                                <a class="prev-img">
                                    <svg class="bi bi-arrow-left-circle-fill" width="3rem" height="3rem" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-7.646 2.646a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L6.207 7.5H11a.5.5 0 0 1 0 1H6.207l2.147 2.146z"/>
                                    </svg>
                                </a>
                                <a class="next-img">
                                    <svg class="bi bi-arrow-right-circle-fill" width="3rem" height="3rem" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd" d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-8.354 2.646a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L9.793 7.5H5a.5.5 0 0 0 0 1h4.793l-2.147 2.146z"/>
                                    </svg>
                                </a>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>