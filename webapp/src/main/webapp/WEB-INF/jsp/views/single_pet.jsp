<%@ page import="ar.edu.itba.paw.models.constants.PetStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatDate value="${pet.uploadDate}" var="uploadDate" type="date" pattern="dd-MM-yyyy"/>
<fmt:formatDate value="${pet.birthDate}" var="birthDate" type="date" pattern="dd-MM-yyyy"/>


<c:set var="petName" value="${pet.petName}"/>
<c:set var="cprice" value="${pet.price}"/>
<c:set var="owner" value="${pet.user.username}"/>
<c:if test="${pet.gender eq 'male'}"><spring:message var="pronoun" code="pet.him"/> </c:if>
<c:if test="${pet.gender eq 'female' }"><spring:message var="pronoun" code="pet.her"/> </c:if>

<spring:message code="petCard.someInfo" arguments="${pronoun}" var="someInfo"/>
<spring:message code="petCard.meet" arguments="${petName}" var="meet"/>
<spring:message code="argPrice" arguments="${cprice}" var="price"/>
<spring:message code="petTitle" var="titleVar"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<c:set var="AVAILABLE" value="<%=PetStatus.AVAILABLE.getValue()%>"/>
<c:set var="REMOVED" value="<%=PetStatus.REMOVED.getValue()%>"/>
<c:set var="SOLD" value="<%=PetStatus.SOLD.getValue()%>"/>
<c:set var="UNAVAILABLE" value="<%=PetStatus.UNAVAILABLE.getValue()%>"/>

<t:basicLayout title="${titleVar}">
    <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

    <div class="container-fluid">
        <div class=" col-md-10 offset-md-1 ">
            <div class="bg-light shadow ">
                <div class="p-2 bg-dark">
                    <div class="row text-whitesmoke">
                        <c:if test="${not empty pet.petName}">
                            <div class="col pet-name pet-name-title h1 ml-4"><c:out value="${meet}"/></div>
                        </c:if>
                        <c:if test="${empty pet.petName}">
                            <h1 class="col ml-4">
                                <spring:message code="petCard.giveName" arguments="${pronoun}"/>
                            </h1>
                        </c:if>
                        <c:if test="${(pet.user.id ne loggedUser.id)}">
                            <c:if test="${not requestExists}">
                                <c:if test="${pet.status.value eq AVAILABLE}">
                                    <div class="col p-2">
                                        <div class="row float-right mr-4">
                                            <form method="POST" class="m-0"
                                                  action="<c:url value="/pet/${id}/request" />">
                                                <button type="submit" name="action" class="btn btn-success">
                                                    <i class="fas fa-plus mr-2"></i>
                                                    <spring:message code="petCard.showInterest"/>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </c:if>
                            </c:if>
                            <c:if test="${requestExists}">
                                <c:if test="${pet.status.value eq AVAILABLE}">
                                    <div class="col p-2">
                                        <div class="row float-right mr-4">
                                            <button type="button" class="btn btn-success" disabled>
                                                <spring:message code="petCard.alreadyRequested"/>
                                            </button>
                                        </div>
                                    </div>

                                </c:if>
                            </c:if>
                        </c:if>
                        <c:if test="${(pet.user.id eq loggedUser.id)}">
                            <c:if test="${pet.status.value eq AVAILABLE}">
                                <div class="col p-2">
                                    <div class="row float-right mr-4">
                                        <button type="button" class="btn btn-success"
                                                data-toggle="modal" data-target="#sell-adopt">
                                            <spring:message code="petCard.reserve"/></button>
                                        <form method="POST" class="m-0 ml-2" action="<c:url value="/pet/${id}/remove" />">
                                            <button type="submit" name="action"
                                                    class="btn btn-danger are-you-sure">
                                                <i class="fas fa-times mr-2"></i>
                                                <spring:message code="petCard.remove"/>
                                            </button>
                                        </form>
                                        <a class="btn btn-link bg-light ml-2" href="<c:url value="/edit-pet/${id}"/>">
                                            <i class="fa fa-pencil-square-o"></i>
                                            <spring:message code="pet.edit"/>

                                        </a>
                                    </div>
                                </div>

                            </c:if>

                            <c:if test="${pet.status.value ne AVAILABLE}">
                                <div class="col p-2">
                                    <div class="row float-right mr-4">
                                        <form method="POST" class="m-0" action="<c:url value="/pet/${id}/recover" />">
                                            <button type="submit" name="action" class="btn btn-success">
                                                <spring:message code="petCard.recover"/>
                                            </button>
                                        </form>
                                    </div>
                                </div>

                            </c:if>
                        </c:if>
                    </div>
                </div>
                <hr>
                <div class="p-3">
                    <t:photosList images="${pet.images}"/>
                </div>
                <div class="p-3">
                    <p class="break-text">
                        <c:out value="${pet.description}"/>
                    </p>
                </div>
                <hr>
                <div class="p-3">
                    <h2><c:out value="${someInfo}"/></h2>

                    <ul class="list-group">
                        <li class="list-group-item"><b><spring:message code="petCard.name"/></b> <c:out
                                value="${pet.petName}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.dob"/></b> <c:out
                                value="${birthDate}"/></li>
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
                                value="${uploadDate}"/></li>
                        <li class="list-group-item"><b><spring:message code="petCard.owner"/></b>
                            <a href="${pageContext.request.contextPath}/user/${pet.user.id}"> <c:out value="${owner}"/>
                            </a>
                        </li>
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

                        <c:if test="${pet.price eq 0 and pet.status.value eq AVAILABLE}">
                            <li class="list-group-item"><b><spring:message code="petCard.forAdoption"/></b><i
                                    class="fas fa-check ml-2 "></i></li>
                            <li class="list-group-item"><b><spring:message code="petCard.forSale"/></b><i
                                    class="fas fa-times ml-2"></i></li>
                        </c:if>
                        <c:if test="${pet.price gt 0 and pet.status.value eq AVAILABLE}">
                            <li class="list-group-item"><b><spring:message code="petCard.forAdoption"/></b><i
                                    class="fas fa-times ml-2"></i></li>
                            <li class="list-group-item"><b><spring:message code="petCard.forSale"/></b><i
                                    class="fas fa-check ml-2 "></i>
                                (<spring:message code="petCard.price"/> <spring:message code="argPrice"
                                                                                        arguments="${pet.price}"/>)
                            </li>
                        </c:if>
                        <c:if test="${pet.status.value eq SOLD}">
                            <li class="list-group-item"><b>
                                <spring:message code="status.sold"/>:</b><i class="fas fa-check ml-2 "></i>
                                (<spring:message code="pet.status.currentlySold"
                                                 arguments="${pageContext.request.contextPath}/user/${pet.newOwner.id},${pet.newOwner.username}"/>)
                            </li>
                        </c:if>
                        <c:if test="${pet.status.value eq UNAVAILABLE or pet.status.value eq REMOVED}">
                            <li class="list-group-item"><b><spring:message code="pet.status.notAvailable"/></b></li>
                        </c:if>
                    </ul>

                </div>
                <hr>


                <div class="p-3">
                    <h2 class="mb-4"><spring:message code="questions"/></h2>

                    <spring:message var="ASK_SOMETHING_TXT" code="questions.askSomething"/>
                    <spring:message var="SEND_QUESTION_TXT" code="questions.send"/>

                    <c:if test="${pet.user.id ne loggedUser.id}">
                        <form class="form" method="post" action="${pageContext.request.contextPath}/pet/${pet.id}/question">
                            <div class="form-group mr-sm-3 mb-2">
                                <label for="questionInput" class="sr-only">${ASK_SOMETHING_TXT}</label>
                                <textarea name="content" class="form-control input-max-value" id="questionInput" placeholder="${ASK_SOMETHING_TXT}"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary mb-2">${SEND_QUESTION_TXT}</button>
                        </form>
                    </c:if>

                    <ul class="questions input-max-value-delegator">

                    </ul>
                    <button type="button" class="btn btn-outline-secondary btn-sm load-more">Cargar mas</button>
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
                                    <svg class="bi bi-arrow-left-circle-fill" width="3rem" height="3rem"
                                         viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd"
                                              d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-7.646 2.646a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L6.207 7.5H11a.5.5 0 0 1 0 1H6.207l2.147 2.146z"/>
                                    </svg>
                                </a>
                                <a class="next-img">
                                    <svg class="bi bi-arrow-right-circle-fill" width="3rem" height="3rem"
                                         viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                        <path fill-rule="evenodd"
                                              d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0zm-8.354 2.646a.5.5 0 0 0 .708.708l3-3a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L9.793 7.5H5a.5.5 0 0 0 0 1h4.793l-2.147 2.146z"/>
                                    </svg>
                                </a>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <div class="modal fade" id="sell-adopt" tabindex="-1" role="dialog" aria-labelledby="sell-adoptTitle"
                 aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h3 class="modal-title" id="sell-adoptTitle"><spring:message code="sellAdopt.title"/></h3>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="${pageContext.request.contextPath}/pet/${pet.id}/sell-adopt"
                                  method="post" enctype="multipart/form-data">

                                <div class="form-group">
                                    <spring:message code="user" var="userTxt"/>
                                    <label for="user">${userTxt}: </label>
                                    <select id="user" class="form-control" name="newowner">
                                        <c:forEach var="user" items="${availableUsers}">
                                            <option value="${user.id}">${user.username}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <small><spring:message code="sellAdopt.disclaimer"/></small>

                                <div class="text-right">
                                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                    </button>
                                    <spring:message code="uploadPetForm.submit" var="submitText"/>
                                    <input type="submit" class="btn btn-primary" value="${submitText}"/>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <spring:message var="NO_ANSWER_YET_TXT" code="questions.noAnswerYet"/>
    <spring:message var="WRITE_AN_ANSWER" code="questions.writeAnAnswer"/>
    <spring:message var="SEND_ANSWER" code="questions.sendAnswer"/>
    <spring:message var="NO_QUESTIONS_YET_TXT" code="questions.noQuestionsYet"/>

    <script>
        const NO_ANSWER_YET_TXT = '${NO_ANSWER_YET_TXT}';
        const NO_QUESTIONS_YET_TXT = '${NO_QUESTIONS_YET_TXT}';
        const WRITE_AN_ANSWER_TXT = '${WRITE_AN_ANSWER}';
        const SEND_ANSWER_TXT = '${SEND_ANSWER}';
        const SERVER_URL = "${pageContext.request.contextPath}";
        const PET_ID = ${pet.id};
        const IS_OWNER = ${pet.user.id eq loggedUser.id};
    </script>

    <script src="<c:url value="/resources/js/max_value_input.js"/>"></script>
    <script src="<c:url value="/resources/js/load_more_questions.js"/>"></script>
    <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>