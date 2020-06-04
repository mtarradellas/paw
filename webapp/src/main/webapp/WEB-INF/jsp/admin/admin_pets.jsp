<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="areYouSure.delete" var="sureBody"/>
<spring:message code="areYouSure.title" var="sureTitle"/>

<spring:message code="adminTitle.pet" var="petTitle"/>
<t:adminLayout title="${petTitle}" item="pets">
    <jsp:body>
        <c:if test="${wrongSearch}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <spring:message code="wrongSearch"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <t:are-you-sure title="${sureTitle}" body="${sureBody}"/>

        <div class="container-fluid">
            <div class="row">
                    <%--                Filter Tools --%>

                <div class="col-md-2 search-tools">
                    <form class="card shadow p-3" method="get"
                          action="${pageContext.request.contextPath}/admin/pets">

                        <div class="card-body">
                            <div class="form-group">
                                <label for="filter-species"><spring:message code="pet.species"/></label>
                                <select name="species" class="form-control" id="filter-species">
                                    <option value="any"><spring:message code="filter.any"/></option>
                                    <c:forEach items="${speciesList}" var="speciesValue">
                                        <c:set var="speciesId">${speciesValue.id}</c:set>
                                        <option value="${speciesValue.id}"
                                                <c:if test="${(not empty param.species) && (param.species ne 'any') && (speciesId eq param.species)}">
                                                    selected
                                                </c:if>
                                        >
                                                ${speciesValue.name}
                                        </option>
                                    </c:forEach>
                                </select>

                                <label for="filter-breed"><spring:message code="pet.breed"/></label>
                                <select name="breed" class="form-control" id="filter-breed"
                                        <c:if test="${(empty param.species) || (param.species eq 'any')}">
                                            disabled
                                        </c:if>
                                >
                                    <option class="species-any" value="any"><spring:message code="filter.any"/></option>

                                    <c:forEach items="${breedList}" var="breed">
                                        <c:set var="breedId">${breed.id}</c:set>
                                        <c:set var="speciesId">${breed.species.id}</c:set>
                                        <option class="species-${breed.species.id}" value="${breed.id}"
                                                <c:if test="${(not empty param.species) && (param.species ne 'any') && (speciesId ne param.species)}">style="display: none;"</c:if>
                                                <c:if test="${(not empty param.breed) && (param.breed ne 'any') && (breedId eq param.breed)}">selected</c:if>
                                        >
                                                ${breed.name}
                                        </option>
                                    </c:forEach>

                                </select>

                                <label for="filter-gender"><spring:message code="pet.sex"/></label>
                                <select name="gender" class="form-control" id="filter-gender">
                                    <option value="any"><spring:message code="filter.any"/></option>
                                    <option value="male"
                                            <c:if test="${(not empty param.gender) && (param.gender eq 'male')}">selected</c:if>
                                    ><spring:message code="pet.male"/></option>
                                    <option value="female"
                                            <c:if test="${(not empty param.gender) && (param.gender eq 'female')}">selected</c:if>
                                    ><spring:message code="pet.female"/></option>
                                </select>
                                <label for="filter-status"><spring:message code="status"/></label>
                                <select name="status" class="form-control" id="filter-status">
                                    <option value="any"><spring:message code="filter.any"/></option>
                                    <option value="deleted"
                                            <c:if test="${(not empty param.status) && (param.status eq 'deleted')}">selected</c:if>
                                    ><spring:message code="status.deleted"/></option>
                                    <option value="exists"
                                            <c:if test="${(not empty param.status) && (param.status eq 'exists')}">selected</c:if>
                                    ><spring:message code="status.exists"/></option>
                                </select>
                            </div>
                            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
                            <select name="searchCriteria" class="form-control" id="search-criteria">
                                <option value="any"><spring:message code="filter.any"/></option>
                                <option value="species"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'species')}">selected</c:if>
                                ><spring:message code="pet.species"/></option>
                                <option value="gender"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'gender')}">selected</c:if>
                                ><spring:message code="pet.sex"/></option>
                                <option value="price"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'price')}">selected</c:if>
                                ><spring:message code="pet.price"/></option>
                                <option value="upload-date"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'upload-date')}">selected</c:if>
                                ><spring:message code="pet.date"/></option>
                                <option value="breed"
                                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'breed')}">selected</c:if>
                                ><spring:message code="pet.breed"/></option>
                            </select>
                            <label for="search-order"><spring:message code="filter.order"/></label>
                            <select name="searchOrder" class="form-control" id="search-order"
                                    <c:if test="${(empty param.searchCriteria) || (param.searchCriteria eq 'any')}">
                                        disabled
                                    </c:if>
                            >
                                <option value="asc"
                                        <c:if test="${(not empty param.searchOrder) && (param.searchOrder eq 'asc')}">selected</c:if>
                                ><spring:message code="filter.ascending"/></option>
                                <option value="desc"
                                        <c:if test="${(not empty param.searchOrder) && (param.searchOrder eq 'desc')}">selected</c:if>
                                ><spring:message code="filter.descending"/></option>
                            </select>
                        </div>
                        <div class="card-footer" id="search-tools-submit">
                            <button type="submit" class="btn btn-primary"><spring:message code="filter"/></button>
                        </div>
                    </form>
                </div>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <div class="col">
                                <c:if test="${empty petList }">
                                    <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                        <a href="${pageContext.request.contextPath}/admin/pets"><spring:message
                                                code="showFirst"/></a>
                                    </div>
                                </c:if>

                                <c:if test="${not empty petList}">
                                    <div>
                                        <h2><spring:message code="admin.petsListing"/>
                                            <a type="button" class="btn btn-success"
                                               href="${pageContext.request.contextPath}/admin/upload-pet"
                                            ><i class="fas fa-plus mr-2"></i><spring:message code="addPet"/></a>
                                        </h2>
                                    </div>
                                </c:if>
                            </div>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right mb-1"
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>

                        </div>


                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                              baseURL="${'/admin/pets/'}"/>
                            </c:if>
                        </div>
                        <div>
                            <c:if test="${not empty petList}">
                                <div class="row">
                                    <div class="col-lg-7">
                                        <h5 class="text-left ml-4"><b><spring:message code="pet"/></b></h5>
                                    </div>
                                    <div class="col">
                                        <h5 class="text-center mr-4"><b><spring:message code="actions"/></b></h5>
                                    </div>
                                </div>
                            </c:if>
                            <ul class="list-group list-group-flush ">
                                <c:forEach var="pet" items="${petList}">
                                    <%--                                    Falta agregar que si el status es deleted lo muestra mas oscuro y con un boton distinto--%>
                                    <li     <c:if test="${(pet.status.value eq 1)}">
                                        class="list-group-item"
                                    </c:if>
                                            <c:if test="${(pet.status.value eq 2) or (pet.status.value eq 3)}">
                                                class="list-group-item resolved"
                                            </c:if>
                                    >
                                        <div class="row ">
                                            <div class="col-lg-7">
                                                <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${pet.id}"/>">
                                                    <img src="<c:out value="${pageContext.request.contextPath}/img/${pet.images[0].id}"/>"
                                                         alt="Pet image" height="70" width="70">
                                                    <c:if test="${not empty pet.petName}">
                                                        <c:out value="${pet.petName}"/>
                                                    </c:if>
                                                    <c:if test="${empty pet.petName}">
                                                        <spring:message code="pet.unnamed"/>
                                                    </c:if>
                                                </a>
                                            </div>
                                            <div class="col text-center pt-3 ml-3">
                                                <c:if test="${pet.status.value eq 1}">
                                                    <form method="POST" class="m-0"
                                                          action="<c:url value="/admin/pet/${pet.id}/remove"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${pet.user.id}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitOwner"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${pet.id}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitPet"/></a>
                                                        <a href="<c:url value="/admin/pet/${pet.id}/edit-pet"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="edit"/></a>
                                                        <button type="submit" class="btn btn-danger are-you-sure">
                                                            <spring:message code="petCard.remove"/></button>
                                                    </form>
                                                </c:if>
                                                <c:if test="${(pet.status.value eq 2) or (pet.status.value eq 3)}">
                                                    <form method="POST" class="m-0"
                                                          action="<c:url value="/admin/pet/${pet.id}/recover"/>">
                                                        <a href="${pageContext.request.contextPath}/admin/user/<c:out value="${pet.user.id}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitOwner"/></a>
                                                        <a href="${pageContext.request.contextPath}/admin/pet/<c:out value="${pet.id}"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="visitPet"/></a>
                                                        <a href="<c:url value="/admin/pet/${pet.id}/edit-pet"/>"
                                                           type="button" class="btn btn-secondary"><spring:message
                                                                code="edit"/></a>
                                                        <button type="submit"
                                                                class="btn btn-success"><spring:message
                                                                code="petCard.recover"/></button>
                                                    </form>
                                                </c:if>

                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                        <div class="m-2">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}"
                                              baseURL="${'/admin/pets/'}"/>
                            </c:if>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="help" tabindex="-1" role="dialog" aria-labelledby="helpTitle"
                     aria-hidden="true">
                    <div class="modal-dialog" role="document">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h3 class="modal-title" id="helpTitle"><spring:message code="help.title"/></h3>
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div class="modal-body">
                                <h4><b><spring:message code="guide.role"/></b></h4>
                                <p><spring:message code="guide.role.description"/></p>
                                <h4><b><spring:message code="guide.color"/></b></h4>
                                <p><spring:message code="guide.color.description"/></p>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/are_you_sure.js"/>"></script>
        <script src="<c:url value="/resources/js/index.js"/>"></script>
    </jsp:body>
</t:adminLayout>