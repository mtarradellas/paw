<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8" %>

<%@attribute name="breedList" required="true" type="ar.edu.itba.paw.models.Breed[]" %>
<%@attribute name="speciesList" required="true" type="ar.edu.itba.paw.models.Species[]" %>
<%@attribute name="departmentList" required="true" type="ar.edu.itba.paw.models.Department[]" %>
<%@attribute name="provinceList" required="true" type="ar.edu.itba.paw.models.Province[]" %>


<div class="col-md-2 search-tools">
    <form class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/">

        <div class="card-body">
            <div class="form-group">
                <label for="filter-species"><spring:message code="pet.species"/></label>
                <select data-child="filter-breed" name="species" class="selector-parent form-control" id="filter-species">
                        <option value="-1"><spring:message code="filter.any"/></option>
                        <c:forEach items="${speciesList}" var="speciesValue">
                            <c:set var="speciesId">${speciesValue.id}</c:set>
                            <option value="${speciesValue.id}"
                                    <c:if test="${(not empty param.species)  && (speciesId eq param.species)}">
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
                    <option class="species-any" value="-1"><spring:message code="filter.any"/></option>

                    <c:forEach items="${breedList}" var="breed">
                        <c:set var="breedId">${breed.id}</c:set>
                        <c:set var="speciesId">${breed.species.id}</c:set>
                        <option data-dependency="${breed.species.id}" value="${breed.id}"
                                <c:if test="${(not empty param.breed) && (breedId eq param.breed)}">selected</c:if>
                        >
                                ${breed.name}
                        </option>
                    </c:forEach>

                </select>

                <label for="filter-price"><spring:message code="pet.price"/></label>
                <select  name="priceRange" class=" form-control" id="filter-price">
                    <option value="-1"><spring:message code="filter.any"/></option>
                    <option value="0"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '0')}">selected</c:if>
                    ><spring:message code="pet.forAdoption"/></option>
                    <option value="1"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '1')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="1"/>-<spring:message code="argPrice" arguments="5000"/>
                    </option>
                    <option value="2"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '2')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="5000"/>-<spring:message code="argPrice" arguments="10000"/>
                    </option>
                    <option value="3"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '3')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="10000"/>-<spring:message code="argPrice" arguments="15000"/>
                    </option>
                    <option value="4"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '4')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="15000"/>-<spring:message code="argPrice" arguments="20000"/>
                    </option>
                    <option value="5"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '5')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="20000"/>-<spring:message code="argPrice" arguments="25000"/>
                    </option>
                    <option value="6"
                            <c:if test="${(not empty param.priceRange) && (param.priceRange eq '6')}">selected</c:if>
                    ><spring:message code="argPrice" arguments="25000"/>+
                    </option>
                </select>


                <label for="filter-gender"><spring:message code="pet.sex"/></label>
                <select name="gender" class="form-control" id="filter-gender">
                    <option value="-1"><spring:message code="filter.any"/></option>
                    <option value="male"
                            <c:if test="${(not empty param.gender) && (param.gender eq 'male')}">selected</c:if>
                    ><spring:message code="pet.male"/></option>
                    <option value="female"
                            <c:if test="${(not empty param.gender) && (param.gender eq 'female')}">selected</c:if>
                    ><spring:message code="pet.female"/></option>
                </select>

                <label for="filter-province"><spring:message code="searchTool.province"/></label>
                <select data-child="filter-department" name="province" class="selector-parent form-control" id="filter-province">
                    <option value="-1"><spring:message code="filter.any"/></option>
                    <c:forEach items="${provinceList}" var="province">
                        <c:set var="speciesId">${province.id}</c:set>
                        <option value="${province.id}"
                                <c:if test="${(not empty param.province)  && (speciesId eq param.province)}">
                                    selected
                                </c:if>
                        >
                                ${province.name}
                        </option>
                    </c:forEach>
                </select>

                <label for="filter-department"><spring:message code="searchTool.department"/></label>
                <select name="department" class="form-control" id="filter-department">
                    <option value="-1"><spring:message code="filter.any"/></option>

                    <c:forEach items="${departmentList}" var="department">
                        <c:set var="departmentId">${department.id}</c:set>
                        <option data-dependency="${department.province.id}" value="${department.id}"
                                <c:if test="${(not empty param.department) && (departmentId eq param.department)}">selected</c:if>
                        >
                                ${department.name}
                        </option>
                    </c:forEach>

                </select>
            </div>
            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
            <select data-child="search-order" name="searchCriteria" class="selector-parent form-control" id="search-criteria">
                <option value="-1"><spring:message code="filter.any"/></option>
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
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/"><spring:message code="filter.clear"/></a>
        </div>
    </form>
</div>