<%@ page import="ar.edu.itba.paw.models.constants.PetStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<fmt:parseDate  value="${pet.uploadDate}"  type="date" pattern="yyyy-MM-dd" var="parsedUpload" />
<fmt:formatDate value="${parsedUpload}" var="uploadDate" type="date" pattern="dd-MM-yyyy"/>

<fmt:parseDate  value="${pet.birthDate}"  type="date" pattern="yyyy-MM-dd" var="parsedBirth" />
<fmt:formatDate value="${parsedBirth}" var="birthDate" type="date" pattern="dd-MM-yyyy"/>

<c:if test="${pet.gender eq 'male'}"><spring:message var="pronoun" code="pet.him"/> </c:if>
<c:if test="${pet.gender eq 'female' }"><spring:message var="pronoun" code="pet.her"/> </c:if>

<spring:message code="petCard.someInfo" arguments="${pronoun}" var="someInfo"/>
<spring:message code="petTitle" var="petTitle"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>
<c:set var="owner" value="${pet.user.username}"/>

<c:set var="AVAILABLE" value="<%=PetStatus.AVAILABLE.getValue()%>"/>
<c:set var="REMOVED" value="<%=PetStatus.REMOVED.getValue()%>"/>
<c:set var="SOLD" value="<%=PetStatus.SOLD.getValue()%>"/>
<c:set var="UNAVAILABLE" value="<%=PetStatus.UNAVAILABLE.getValue()%>"/>

<t:adminLayout title="${petTitle}" item="pets">
    <jsp:body>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

        <div class="container-fluid">
            <div class=" col-md-10 offset-md-1">
                <div class="bg-light shadow">
                    <div class="p-2 bg-dark">
                        <div class="row text-whitesmoke ">
                            <c:if test="${not empty pet.petName}">
                                <h1 class="ml-4 ">
                                    <c:out value="${pet.petName}"/>
                                </h1>
                            </c:if>
                            <c:if test="${empty pet.petName}">
                                <h1 class="ml-4 ">
                                    <spring:message code="pet.unnamed"/>
                                </h1>
                            </c:if>
                            <c:if test="${pet.status.value eq SOLD}">
                                <h1 class="ml-1 "> (<spring:message code="status.sold"/>) </h1>
                            </c:if>
                            <c:if test="${pet.status.value eq REMOVED}">
                                <h1 class="ml-1"> (<spring:message code="status.deleted"/>) </h1>
                            </c:if>
                            <c:if test="${pet.status.value eq AVAILABLE}">

                                <div class="col p-2">
                                    <div class="row float-right mr-4">
                                        <form method="POST" class="m-0"
                                              action="<c:url value="/admin/pet/${id}/sell-adopt" />">
                                            <button type="submit" name="action" class="btn btn-success">
                                                <spring:message code="petCard.reserve"/>
                                            </button>
                                        </form>
                                        <form method="POST" class="m-0 ml-2"
                                              action="<c:url value="/admin/pet/${pet.id}/remove"/>">
                                            <button type="submit" class="btn btn-danger are-you-sure">
                                                <spring:message code="petCard.remove"/></button>
                                        </form>
                                        <a class="btn btn-link bg-light ml-2" href="<c:url value="/admin/pet/${id}/edit-pet"/>">
                                            <i class="fa fa-pencil-square-o"></i>
                                            <spring:message code="pet.edit"/>

                                        </a>
                                    </div>
                                </div>

                            </c:if>
                            <c:if test="${(pet.status.value eq REMOVED) or (pet.status.value eq SOLD)}">
                                <div class="col p-2">
                                    <div class="row float-right mr-4">
                                        <form method="POST" class="m-0"
                                              action="<c:url value="/admin/pet/${pet.id}/recover"/>">
                                            <button type="submit" class="btn btn-success">
                                                <spring:message code="petCard.recover"/></button>
                                        </form>
                                    </div>
                                </div>
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
                            <li class="list-group-item"><spring:message code="admin.petCard.id"/> <c:out
                                    value="${pet.id}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.name"/> <c:out
                                    value="${pet.petName}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.dob"/> <c:out
                                    value="${birthDate}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.species"/> <c:out
                                    value="${pet.species.name}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.breed"/> <c:out
                                    value="${pet.breed.name}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.sex"/> <spring:message
                                    code="pet.${pet.gender}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.vaccinated"/> <spring:message
                                    code="yesNo.${pet.vaccinated}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.price"/> <spring:message
                                    code="argPrice" arguments="${pet.price}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.province"/> <c:out
                                    value="${pet.province.name}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.department"/> <c:out
                                    value="${pet.department.name}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.uploadDate"/> <c:out
                                    value="${uploadDate}"/></li>
                            <li class="list-group-item"><spring:message code="admin.petCard.status"/> <c:out
                                    value="${pet.status}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.owner"/>
                                <a href="${pageContext.request.contextPath}/admin/user/${pet.user.id}"> <c:out value="${owner}"/>
                                </a>
                            </li>
                            <c:if test="${pet.newOwner.username ne null}">
                                <li class="list-group-item">
                                    <spring:message code="pet.status.currentlySold"
                                                    arguments="${pageContext.request.contextPath}/user/${pet.newOwner.id},
                                                 ${pet.newOwner.username}"/>
                                </li>
                            </c:if>

                        </ul>


                    </div>
                    <hr>
                    <c:set var="ownerId" value="${pet.user.id}"/>
                    <a href="${pageContext.request.contextPath}/admin/user/${ownerId}"
                       class="btn darkblue-action p-2 m-3"><spring:message code="petCard.gotoOwnerPage"/></a>


                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/admin"><spring:message code="backToHome"/></a>
                    </div>
                </div>
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
                        <img src="" alt="Full sized image"/>
                    </div>
                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
    </jsp:body>
</t:adminLayout>