<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="title"/>
<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

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
                <div class="p-2">
                    <hr>
                    <c:if test="${loggedUser.id eq user.id}">
                        <ul class="list-group">
                            <li class="list-group-item"><b><spring:message code="user.email"/></b> <c:out
                                    value="${user.mail}"/></li>
                        </ul>
                    </c:if>
                    <c:if test="${loggedUser.id ne user.id}">
                        <h5 class="text-center"><b><spring:message code="otherUserProfile"/></b></h5>
                    </c:if>
                    <div class="p-2">

                        <h2><b><spring:message code="userPets"/></b>
                            <spring:message code="userPets.faded"/></h2>
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


                            <c:if test="${pet.status.id eq 1}">
                            <div class="col-auto mb-3">
                                </c:if>
                                <c:if test="${pet.status.id ne 1 }">
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
                    </div>
                    <div class="p-4">
                        <b><a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a></b>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
        <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>