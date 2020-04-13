<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="dogBreeds" value="<%=ar.edu.itba.paw.models.constants.DogBreeds.values()%>"/>
<c:set var="catBreeds" value="<%=ar.edu.itba.paw.models.constants.CatBreeds.values()%>"/>


<div class="col-md-2 search-tools">
    <div class="card shadow p-3">
        <div class="card-header">
            <h5 class="card-title"><spring:message code="filter.options"/></h5>
        </div>
        <div class="card-body">
            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter"/></h6>
            <div class="form-group">
                <label for="filter-specie"><spring:message code="pet.species"/></label>
                <select class="form-control" id="filter-specie">
                    <option value="any"><spring:message code="filter.any"/></option>
                    <option value="dog"><spring:message code="pet.dog"/></option>
                    <option value="cat"><spring:message code="pet.cat"/></option>
                </select>

                <label for="filter-breed"><spring:message code="pet.breed"/></label>
                <select class="form-control disabled" id="filter-breed" disabled>
                    <option class="specie-any" value="any"><spring:message code="filter.any"/></option>

                    <c:forEach items="${dogBreeds}" var="breed" varStatus="i">
                        <option class="specie-dog" value="${breed.id}"><spring:message code="dog.${breed.name}"/></option>
                    </c:forEach>

                    <c:forEach items="${catBreeds}" var="breed" varStatus="i">
                        <option class="specie-cat" value="${breed.id}"><spring:message code="cat.${breed.name}"/></option>
                    </c:forEach>

                </select>

                <label for="filter-gender"><spring:message code="pet.sex"/></label>
                <select class="form-control" id="filter-gender">
                    <option value="any"><spring:message code="filter.any"/></option>
                    <option value="male"><spring:message code="pet.male"/></option>
                    <option value="female"><spring:message code="pet.female"/></option>
                </select>
            </div>
            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter.orderBy"/></h6>
            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
            <select class="form-control" id="search-criteria">
                <option value="any"><spring:message code="filter.any"/></option>
                <option value="specie"><spring:message code="pet.species"/></option>
                <option value="gender"><spring:message code="pet.sex"/></option>
                <option value="price"><spring:message code="pet.price"/></option>
                <option value="upload-date"><spring:message code="pet.date"/></option>
            </select>
            <label for="search-order"><spring:message code="filter.order"/></label>
            <select class="form-control" id="search-order" disabled>
                <option value="asc"><spring:message code="filter.ascending"/></option>
                <option value="desc"><spring:message code="filter.descending"/></option>
            </select>
        </div>
        <div class="card-footer" id="search-tools-submit">
            <button type="button" class="btn btn-primary"><spring:message code="search"/></button>
        </div>
    </div>
</div>