<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="adminTitle.pet" var="petTitle"/>
<t:adminLayout title="${petTitle}">
    <jsp:body>
        <span id="confirmMessage" hidden>
            <spring:message code='confirmMessage' javaScriptEscape='true' />
        </span>
        <div class="container-fluid">
            <div class="row">
                <t:search-tools-pet breeds_list="${breeds_list}" species_list="${species_list}"/>
                <div class="col-lg-8">
                    <div class="shadow p-3 bg-white rounded">

                        <c:if test="${empty pets_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/admi/pets"><spring:message code="showFirst"/></a>
                            </div>
                        </c:if>

                        <c:if test="${not empty pets_list}">
                            <div>
                                <h2><spring:message code="admin.petsListing" /> <spring:message code="showingResults" arguments="${pets_list.size()}"/>
                                    <button type="button" class="btn btn-success"><i class="fas fa-plus mr-2"></i><spring:message code="addPet"/></button>
                                </h2>
                            </div>
                        </c:if>

                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/pets/'}" />
                            </c:if>
                        </div>
                        <div>
                            <div class="row">
                                <div class="col-lg-8">
                                   <h5 class="text-left ml-4"><b><spring:message code="pet"/></b></h5>
                                </div>
                                <div class="col">
                                    <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                </div>
                            </div>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="pet" items="${pets_list}">
<%--                                    Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li class="list-group-item ">
                                        <div class="row ">
                                            <div class="col-lg-8">
                                                <a href="${pageContext.request.contextPath}/admi/pet/{${pet.id}}">
                                                <img src="<c:out value="${pageContext.request.contextPath}/img/${pet.images[0]}"/>"
                                                     alt="Pet image" height="70" width="70"> ${pet.petName}
                                                </a>
                                            </div>
                                            <div class="col text-center pt-3 ml-3">
                                                <form method="POST" class="m-0" action="<c:url value="/admi/pet/${pet.id}/delete"/>">
                                                    <a href="${pageContext.request.contextPath}/admi/user/{${pet.ownerId}}" type="button" class="btn btn-secondary"><spring:message code="visitOwner"/></a>
                                                    <a href="${pageContext.request.contextPath}/admi/pet/{${pet.id}}" type="button" class="btn btn-secondary"><spring:message code="visitPet"/></a>
                                                    <a href="${pageContext.request.contextPath}/admi/pet/{${pet.id}}/edit" type="button" class="btn btn-secondary"><spring:message code="edit"/></a>
                                                    <button type="submit" onclick="confirmDelete(event)" class="btn btn-danger"><spring:message code="delete"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </li>
                            </c:forEach>
                            </ul>
                    </div>
                    <div class="m-2">
                        <c:if test="${maxPage ne 1}">
                            <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/pets/'}" />
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="col">
                <div class="shadow p-3 bg-white rounded">
                    <h4><b><spring:message code="guide.role"/></b></h4>
                    <p><spring:message code="guide.role.description"/></p>
                    <h4><b><spring:message code="guide.color"/></b></h4>
                    <p><spring:message code="guide.color.description"/></p>

                </div>
            </div>
        </div>
        </div>


    </jsp:body>
</t:adminLayout>