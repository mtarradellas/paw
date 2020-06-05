<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<spring:message code="userTitle" var="title"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<fmt:formatNumber type="number" maxFractionDigits="2" var="score" value="${user.averageScore}"/>

<t:basicLayout title="${title}">

    <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>
    <div class="container-fluid ">
        <div class=" col-md-10 offset-md-1">
            <div class="bg-light shadow ">
                <div class="p-2 bg-dark">
                    <div class="row text-whitesmoke">
                        <h1 class="ml-4"><c:out value="${user.username}"/></h1>
                        <c:if test="${(user.id eq loggedUser.id)}">
                            <h1 class="mt-2 ml-4">
                                <form method="POST" class="m-0" action="<c:url value="/user/${id}/remove" />">
                                    <button type="submit" name="action"
                                            class="btn btn-danger are-you-sure">
                                        <i class="fas fa-times mr-2"></i>
                                        <spring:message code="petCard.remove"/>
                                    </button>
                                </form>
                            </h1>
                        </c:if>
                    </div>
                </div>
                <hr>
                <c:choose>
                    <c:when test="${user.averageScore == -1}">
                        <h3 class="p-2">
                            <b><spring:message code="user.rating"/>:</b>
                            <spring:message code="user.noReviews"/>
                        </h3>
                        <c:if test="${canRate}">
                            <p class="p-2">
                                <a href="#ratings"><spring:message code="user.rate"/></a>
                            </p>
                        </c:if>
                        <c:if test="${canRate eq false}">
                            <small class="p-2">
                                <spring:message code="user.cantRate"/>
                            </small>
                        </c:if>
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
                        <c:if test="${canRate}">
                            <a href="#ratings"><spring:message code="user.rate"/></a>
                        </c:if>
                        <c:if test="${canRate eq false}">
                            <small class="p-2">
                                <spring:message code="user.cantRate"/>
                            </small>
                        </c:if>

                        </p>
                    </c:otherwise>
                </c:choose>

                <hr>
                <div class="p-2">

                    <c:if test="${loggedUser.id eq user.id}">
                        <ul class="list-group">
                            <li class="list-group-item"><b><spring:message code="user.email"/></b> <c:out
                                    value="${user.mail}"/></li>
                        </ul>
                        <a class="edit-anchor" href="<c:url value="/edit-user/${loggedUser.id}"/>">
                            <spring:message code="editUserForm.edit"/>
                            <svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16"
                                 fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"></path>
                                <path fill-rule="evenodd"
                                      d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z"
                                      clip-rule="evenodd"></path>
                            </svg>
                        </a>
                    </c:if>
                    <c:if test="${loggedUser.id ne user.id}">
                        <h5 class="text-center"><b><spring:message code="otherUserProfile"/></b></h5>
                    </c:if>
                    <hr>
                    <div class="p-2">

                        <h2><b><spring:message code="userPets"/></b> <spring:message code="totalResults"
                                                                                     arguments="${amount}"/>
                        </h2>
                        <c:if test="${not empty userPets}">
                            <div class="m-2 ">
                                <c:if test="${maxPage ne 1}">
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                                  baseURL="/user/${id}"/>
                                </c:if>
                            </div>
                        </c:if>
                        <div class="card-deck row">

                            <c:forEach var="pet" items="${userPets}">


                            <c:if test="${pet.status.value eq 1}">
                            <div class="col-auto mb-3">
                                </c:if>
                                <c:if test="${pet.status.value ne 1 }">
                                <div class="col-auto mb-3 resolved">
                                    </c:if>

                                    <t:animalCard pet="${pet}" level="user"/>

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
                                                      baseURL="/user/${id}"/>
                                    </c:if>
                                </div>
                            </c:if>

                        </div>
                        <hr>
                        <div id="ratings" class="p-2">
                            <h2><b><spring:message code="user.reviews"/></b>
                            </h2>
                            <hr>
                        </div>


                        <div class="p-4">
                            <b><a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a></b>
                        </div>
                    </div>
                </div>
            </div>
            <script>let userScore =<c:out value="${user.averageScore}"/></script>
            <script src="<c:url value="/resources/js/user_rating.js"/>"></script>
            <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
            <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>