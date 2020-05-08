<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8" %>

<%@attribute name="breeds_list" required="true" type="ar.edu.itba.paw.models.Breed[]" %>
<%@attribute name="species_list" required="true" type="ar.edu.itba.paw.models.Species[]" %>

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
                        <c:forEach items="${species_list}" var="speciesValue">
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

                    <c:forEach items="${breeds_list}" var="breed">
                        <c:set var="breedId">${breed.id}</c:set>
                        <c:set var="speciesId">${breed.speciesId}</c:set>
                        <option class="species-${breed.speciesId}" value="${breed.id}"
                                <c:if test="${(not empty param.species) && (param.species ne 'any') && (speciesId ne param.species)}">style="display: none;"</c:if>
                                <c:if test="${(not empty param.breed) && (param.breed ne 'any') && (breedId eq param.breed)}">selected</c:if>
                        >
                                ${breed.name}
                        </option>
                    </c:forEach>

                </select>

                <label for="price-filter"><spring:message code="pet.price"/></label>
                <div class="row" id="price-filter">
                    <div class="col pr-0">
                        <spring:message var="minPriceTxt" code="searchTool.minPrice"/>
                        <input id="min-price" placeholder="${minPriceTxt}" class="form-control" name="minPrice" type="number"
                            value="${param.minPrice}"
                        >
                    </div>
                    <div class="col pl-0">
                        <spring:message var="maxPriceTxt" code="searchTool.maxPrice"/>
                        <input id="max-price" placeholder="${maxPriceTxt}" class="form-control ml-1" name="maxPrice" type="number"
                               value="${param.maxPrice}"
                        >
                    </div>
                </div>


                <label for="filter-price"><spring:message code="pet.sex"/></label>
                <select name="price" class="form-control" id="filter-price">
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
            <button type="submit" class="blue-button"><spring:message code="filter"/></button>
        </div>
    </form>
</div>