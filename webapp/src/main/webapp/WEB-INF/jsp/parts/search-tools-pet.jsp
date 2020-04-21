<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="breeds" value="<%=ar.edu.itba.paw.models.constants.BreedTypes.values()%>"/>
<c:set var="species" value="<%=ar.edu.itba.paw.models.constants.SpeciesTypes.values()%>"/>


<div class="col-md-2 search-tools">
    <form class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/">
        <div class="card-header">
            <h5 class="card-title"><spring:message code="filter.options"/></h5>
        </div>
        <div class="card-body">
            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter"/></h6>
                <div class="form-group">
                    <label for="filter-species"><spring:message code="pet.species"/></label>
                    <select name="species" class="form-control" id="filter-species">
                        <option value="any"><spring:message code="filter.any"/></option>
                        <c:forEach items="${species}" var="speciesValue">
                            <option value="${speciesValue.name}"
                                <c:if test="${(not empty param.species) && (param.species eq speciesValue.name)}">
                                    selected
                                </c:if>
                            >
                                <spring:message code="pet.${speciesValue.name}"/>
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

                        <c:forEach items="${breeds}" var="breed">
                            <option class="species-${breed.speciesType.name}" value="${breed.name}"
                                    <c:if test="${(not empty param.species) && (param.species ne breed.speciesType.name)}">style="display: none;"</c:if>
                                    <c:if test="${(not empty param.breed) && (param.breed eq breed.name)}">selected</c:if>
                            >
                                <spring:message code="${breed.speciesType.name}.${breed.name}"/>
                            </option>
                        </c:forEach>

                    </select>

                    <label for="filter-gender"><spring:message code="pet.sex"/></label>
                    <select  name="gender" class="form-control" id="filter-gender">
                        <option value="any"><spring:message code="filter.any"/></option>
                        <option value="male"
                                <c:if test="${(not empty param.gender) && (param.gender eq 'male')}">selected</c:if>
                        ><spring:message code="pet.male"/></option>
                        <option value="female"
                                <c:if test="${(not empty param.gender) && (param.gender eq 'female')}">selected</c:if>
                        ><spring:message code="pet.female"/></option>
                    </select>
                </div>
                <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter.orderBy"/></h6>
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