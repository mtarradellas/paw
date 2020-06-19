<%@ page import="ar.edu.itba.paw.models.constants.UserStatus" %>
<%@ page import="ar.edu.itba.paw.models.constants.PetStatus" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:formatNumber type="number" maxFractionDigits="2" var="score" value="${user.averageScore}"/>

<spring:message code="userTitle" var="userTitle"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<c:if test="${showAllAdopted eq 'true'}">
    <c:set var="adoptedLimit" value="${user.newPets.size()}"/>
</c:if>
<c:if test="${showAllAdopted eq 'false' and user.newPets.size() > 4}">
    <c:set var="adoptedLimit" value="4"/>
</c:if>
<c:if test="${showAllAdopted eq 'false' and user.newPets.size() <= 4}">
    <c:set var="adoptedLimit" value="${user.newPets.size()}"/>
</c:if>

<c:set var="ACTIVE" value="<%=UserStatus.ACTIVE.getValue()%>"/>
<c:set var="INACTIVE" value="<%=UserStatus.INACTIVE.getValue()%>"/>
<c:set var="DELETED" value="<%=UserStatus.DELETED.getValue()%>"/>

<c:set var="AVAILABLE" value="<%=PetStatus.AVAILABLE.getValue()%>"/>
<c:set var="REMOVED" value="<%=PetStatus.REMOVED.getValue()%>"/>
<c:set var="SOLD" value="<%=PetStatus.SOLD.getValue()%>"/>
<c:set var="UNAVAILABLE" value="<%=PetStatus.UNAVAILABLE.getValue()%>"/>

<c:if test="${showAllReviews eq 'true'}">
    <c:set var="limit" value="${user.targetReviews.size()}"/>
</c:if>
<c:if test="${showAllReviews eq 'false' and user.targetReviews.size() > 5}">
    <c:set var="limit" value="5"/>
</c:if>
<c:if test="${showAllReviews eq 'false' and user.targetReviews.size() <= 5}">
    <c:set var="limit" value="${user.targetReviews.size()}"/>
</c:if>

<t:adminLayout title="${userTitle}" item="user">
    <jsp:body>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

        <div class="container-fluid ">
        <div class=" col-md-10 offset-md-1">
            <div class="bg-light shadow ">
                <div class="p-2 bg-dark ">
                    <div class="row text-whitesmoke">
                        <h1 class="ml-4 col"><c:out value="${user.username}"/>
                            (<c:out value="${user.status}"/>)
                        </h1>
                        <div class="col p-2">
                            <div class="row float-right mr-4">
                                <c:if test="${(user.status.value eq ACTIVE) or (user.status.value eq INACTIVE)}">
                                    <form method="POST" class="m-0" action="<c:url value="/admin/user/${id}/remove" />">
                                        <button type="submit" name="action"
                                                class="btn btn-danger are-you-sure">
                                            <i class="fas fa-times mr-2"></i>
                                            <spring:message code="petCard.remove"/>
                                        </button>
                                    </form>
                                </c:if>
                                <c:if test="${user.status.value eq DELETED}">
                                    <form method="POST" class="m-0"
                                          action="<c:url value="/admin/user/${user.id}/recover"/>">
                                        <button type="submit" class="btn btn-success"><spring:message
                                                code="petCard.recover"/></button>
                                    </form>
                                </c:if>

                                <a class="btn btn-link bg-light ml-2"
                                   href="<c:url value="/admin/user/${user.id}/edit"/>">
                                    <i class="fa fa-pencil-square-o"></i>
                                    <spring:message code="editUserForm.edit"/>

                                </a>
                            </div>
                        </div>
                    </div>


                </div>
                <hr>
                <c:choose>
                    <c:when test="${user.averageScore == -1}">
                        <h3 class="p-2">
                            <b><spring:message code="user.rating"/>:</b>
                            <spring:message code="user.noReviews"/>
                        </h3>
                    </c:when>
                    <c:otherwise>
                        <h3 class="p-2"><b><spring:message code="user.rating"/>:</b>
                            <i id="star1" class="star-rating"></i>
                            <i id="star2" class="star-rating"></i>
                            <i id="star3" class="star-rating"></i>
                            <i id="star4" class="star-rating"></i>
                            <i id="star5" class="star-rating"></i>
                        </h3>
                        <p class="p-2">(<spring:message code="user.average"
                                                        arguments="${score},${user.targetReviews.size()}"/>)

                        </p>
                    </c:otherwise>
                </c:choose>
                <div class="p-2">
                    <ul class="list-group">
                        <li class="list-group-item"><b><spring:message code="admin.petCard.id"/></b> <c:out
                                value="${user.id}"/></li>
                        <li class="list-group-item"><b><spring:message code="user.email"/></b> <c:out
                                value="${user.mail}"/></li>
                    </ul>
                    <hr>
                    <div class="p-2">
                        <h2><spring:message code="pets"/></h2>
                        <c:if test="${not empty userPets}">
                            <div class="m-2 ">
                                <c:if test="${maxPage ne 1}">
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                                  baseURL="/admin/user/${id}"/>
                                </c:if>
                            </div>
                        </c:if>
                        <div class="card-deck row">

                            <c:forEach var="pet" items="${userPets}">
                            <c:if test="${pet.status.value eq AVAILABLE}">
                            <div class="col-auto mb-3">
                                </c:if>
                                <c:if test="${pet.status.value ne AVAILABLE }">
                                <div class="col-auto mb-3 resolved">
                                    </c:if>
                                    <t:animalCard pet="${pet}" level="admin" loggedUser="${loggedUser}"/>

                                </div>
                                </c:forEach>
                                <c:if test="${empty userPets}">
                                    <div class="col-auto">
                                        <spring:message code="noPetsFound"/>
                                    </div>
                                </c:if>

                            </div>
                            <c:if test="${not empty userPets}">
                                <div class="m-2 ">
                                    <c:if test="${maxPage ne 1}">
                                        <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                                      baseURL="/admin/user/${id}"/>
                                    </c:if>
                                </div>
                            </c:if>

                        </div>
                    </div>
                    <hr>
                    <c:if test="${user.newPets.size() > 0}">
                        <div class="p-2" id="adopted">
                            <h2><b><spring:message code="userPets.adopted"/></b>
                                <spring:message code="showingOutOf" arguments="${adoptedLimit},${user.newPets.size()}"/>
                            </h2>
                            <div class="card-deck row">
                                <c:forEach var="pet" items="${user.newPets}" end="${adoptedLimit-1}">
                                    <div class="col-auto mb-3">
                                        <t:animalCard pet="${pet}" level="user" loggedUser="${loggedUser}"/>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <c:if test="${user.newPets.size() > 4 and showAllAdopted eq 'false'}">
                            <form method="get" class="text-center"
                                  action="${pageContext.request.contextPath}/user/${user.id}#adopted">
                                <input type="hidden" name="showAllAdopted" value="true">
                                <c:if test="${not empty param.page}">
                                    <input type="hidden" name="page" value="${param.page}">
                                </c:if>
                                <c:if test="${not empty param.showAllReviews}">
                                    <input type="hidden" name="showAllReviews" value="${param.showAllReviews}">
                                </c:if>
                                <button class="btn btn-primary btn-lg mt-2" type="submit"><spring:message
                                        code="showAll"/></button>

                            </form>
                        </c:if>
                        <c:if test="${showAllAdopted eq 'true'}">
                            <form method="get" class="text-center"
                                  action="${pageContext.request.contextPath}/user/${user.id}#adopted">
                                <input type="hidden" name="showAllAdopted" value="false">
                                <c:if test="${not empty param.page}">
                                    <input type="hidden" name="page" value="${param.page}">
                                </c:if>
                                <c:if test="${not empty param.showAllReviews}">
                                    <input type="hidden" name="showAllReviews" value="${param.showAllReviews}">
                                </c:if>
                                <button class="btn btn-primary btn-lg mt-2" type="submit"><spring:message
                                        code="showLess"/></button>
                            </form>
                        </c:if>
                    </c:if>

                    <hr>
                    <c:if test="${user.averageScore != -1}">
                        <div id="ratings" class="p-2">
                            <h2><b><spring:message code="user.reviews"/></b>
                                <spring:message code="showingOutOf"
                                                arguments="${limit}, ${user.targetReviews.size()}"/>
                            </h2>

                            <div class="row">
                                <div class="col-lg-2 ml-2 ">
                                    <h5 class="text-left"><b><spring:message code="user"/></b></h5>
                                </div>
                                <div class="col-lg-2">
                                    <h5 class="text-left"><b><spring:message code="user.score"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-left mr-4"><b><spring:message
                                            code="uploadPetForm.description"/></b></h5>
                                </div>
                            </div>
                            <hr class="m-0">
                            <c:forEach var="review" items="${user.targetReviews}" begin="0" end="${limit-1}">
                                <div class="row ml-0 mr-0 bg-white">
                                    <div class="col-lg-2">
                                        <a href="${pageContext.request.contextPath}/user/${review.owner.id}">
                                                ${review.owner.username}
                                        </a>
                                    </div>
                                    <div class="col-lg-2">
                                        <c:if test="${review.score == 1}">
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                        </c:if>
                                        <c:if test="${review.score == 2}">
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                        </c:if>
                                        <c:if test="${review.score == 3}">
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                        </c:if>
                                        <c:if test="${review.score == 4}">
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="far fa-star star-rating"></i>
                                        </c:if>
                                        <c:if test="${review.score == 5}">
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                            <i class="fas fa-star star-rating"></i>
                                        </c:if>
                                    </div>
                                    <div class="col">
                                            ${review.description}
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${user.targetReviews.size() > 5 and showAllReviews eq 'false'}">
                                <form method="get" class="text-center"
                                      action="${pageContext.request.contextPath}/user/${user.id}#ratings">
                                    <input type="hidden" name="showAllReviews" value="true">
                                    <c:if test="${not empty param.page}">
                                        <input type="hidden" name="page" value="${param.page}">
                                    </c:if>
                                    <button class="btn btn-primary btn-lg mt-2" type="submit"><spring:message
                                            code="showAll"/></button>
                                </form>
                            </c:if>
                            <c:if test="${showAllReviews eq 'true'}">
                                <form method="get" class="text-center"
                                      action="${pageContext.request.contextPath}/user/${user.id}#ratings">
                                    <input type="hidden" name="showAllReviews" value="false">
                                    <c:if test="${not empty param.page}">
                                        <input type="hidden" name="page" value="${param.page}">
                                    </c:if>
                                    <button class="btn btn-primary btn-lg mt-2" type="submit"><spring:message
                                            code="showLess"/></button>
                                </form>
                            </c:if>
                            <hr class="m-0">
                        </div>
                    </c:if>

                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                    </div>
                </div>
            </div>
        </div>
        <script>let userScore =<c:out value="${user.averageScore}"/></script>
        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
        <script src="<c:url value="/resources/js/user_rating.js"/>"></script>

    </jsp:body>
</t:adminLayout>