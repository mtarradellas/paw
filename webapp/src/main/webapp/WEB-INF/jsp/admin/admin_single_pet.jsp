<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${pet.gender eq 'male'}"><spring:message var="pronoun" code="pet.him"/> </c:if>
<c:if test="${pet.gender eq 'female' }"><spring:message var="pronoun" code="pet.her"/> </c:if>

<spring:message code="petCard.someInfo" arguments="${pronoun}" var="someInfo"/>
<spring:message code="petTitle" var="petTitle"/>

<t:adminLayout title="${petTitle}" item="pet">
    <jsp:body>
        <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage' javaScriptEscape='true'/>
        </span>
        <div class="row">
            <div class=" col-md-10 offset-md-1">
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

                <div class="bg-light shadow p-3">
                    <div class="p-2">
                        <div class="row">
                            <c:if test="${not empty pet.petName}">
                                <h1>
                                    <c:out value="${pet.petName}"/>
                                </h1>
                            </c:if>
                            <c:if test="${empty pet.petName}">
                                <h1>
                                    <spring:message code="pet.unnamed"/>
                                </h1>
                            </c:if>

                                <%--                                            TODO:falta--%>
                            <c:if test="${pet.status.id eq 1}">
                                <h1 class="mt-2 ml-4">
                                    <a href="${pageContext.request.contextPath}/admi/pet/<c:out value="${pet.id}"/>/edit"
                                       type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                </h1>
                                <h1 class="mt-2 ml-2">
                                    <form method="POST" class="m-0"
                                          action="<c:url value="/admi/pet/${id}/sell-adopt" />">
                                        <button type="submit" name="action" class="btn btn-success">
                                            <spring:message code="petCard.reserve"/>
                                        </button>
                                    </form>
                                </h1>
                                <h1 class="mt-2 ml-2">
                                    <form method="POST" class="m-0"
                                          action="<c:url value="/admi/pet/${pet.id}/remove"/>">
                                        <button type="submit" onclick="confirmDelete(event)" class="btn btn-danger">
                                            <spring:message code="petCard.remove"/></button>
                                    </form>
                                </h1>
                            </c:if>
                            <c:if test="${(pet.status.id eq 2) or (pet.status.id eq 3)}">
                                <h1 class="mt-2 ml-4">
                                    <form method="POST" class="m-0"
                                          action="<c:url value="/admi/pet/${pet.id}/recover"/>">
                                        <button type="submit" class="btn btn-success">
                                            <spring:message code="petCard.recover"/></button>
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
                            <li class="list-group-item"><spring:message code="admin.petCard.id"/> <c:out
                                    value="${pet.id}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.name"/> <c:out
                                    value="${pet.petName}"/></li>
                            <li class="list-group-item"><spring:message code="petCard.dob"/> <c:out
                                    value="${pet.birthDate}"/></li>
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
                            <li class="list-group-item"><spring:message code="petCard.location"/> <c:out
                                    value="${pet.location}"/></li>
                                <%--                                        TODO:Falta la location BIEN cuando lo hayamos implementado en backend--%>
                            <li class="list-group-item"><spring:message code="petCard.uploadDate"/> <c:out
                                    value="${pet.uploadDate}"/></li>
                            <li class="list-group-item"><spring:message code="admin.petCard.status"/> <c:out
                                    value="${pet.status.name}"/></li>
                        </ul>


                    </div>
                    <c:set var="ownerId" value="${pet.ownerId}"/>
                    <a href="${pageContext.request.contextPath}/admi/user/${ownerId}" class="p-2 m-3"><spring:message
                            code="petCard.gotoOwnerPage"/></a>

                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/admi"><spring:message code="backToHome"/></a>
                    </div>
                </div>
            </div>
        </div>


    </jsp:body>
</t:adminLayout>