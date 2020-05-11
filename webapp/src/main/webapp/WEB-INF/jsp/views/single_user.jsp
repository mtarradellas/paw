<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="userTitle" var="title"/>

<t:basicLayout title="${title}">
    <span id="confirmMessage" hidden>
        <spring:message code='confirmMessage' javaScriptEscape='true'/>
    </span>
    <div class="row ">
        <div class=" col-md-10 offset-md-1">
            <div class="bg-light shadow ">
                <div class="p-2 bg-dark">
                    <div class="row text-whitesmoke">
                        <h1 class="ml-4"><c:out value="${user.username}"/></h1>
                        <c:if test="${(user.id eq loggedUser.id)}">
                            <h1 class="mt-2 ml-4">
                                <form method="POST" class="m-0" action="<c:url value="/user/${id}/remove" />">
                                    <button type="submit" onclick="confirmDelete(event)" name="action" class="btn btn-danger">
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
                            <li class="list-group-item"><b><spring:message code="user.email"/></b> <c:out value="${user.mail}"/></li>
                            <li class="list-group-item"><b><spring:message code="user.phone"/></b> <c:out value="${user.phone}"/></li>
                        </ul>
                    </c:if>
                    <c:if test="${loggedUser.id ne user.id}">
                        <spring:message code="otherUserProfile"/>
                    </c:if>
                    <hr>
                    <div class="p-2">
                        <h2><spring:message code="userPets"/></h2>
                        <c:if test="${not empty userPets}">
                            <div class="m-2 ">
                                <c:if test="${maxPage ne 1}">
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/user/${id}"/>
                                </c:if>
                            </div>
                        </c:if>
                        <div class="card-deck row">

                            <c:forEach var="pet" items="${userPets}">
                                <div class="col-auto mb-3">

                                    <t:animalCard pet="${pet}"/>

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
                                    <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="/user/${id}"/>
                                </c:if>
                            </div>
                        </c:if>

                    </div>
                    <hr>

                </div>

                <div class="p-4">
                    <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                </div>
            </div>
        </div>
    </div>
    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>