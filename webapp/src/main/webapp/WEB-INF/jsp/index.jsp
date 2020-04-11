<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:basicLayout>
    <jsp:body>
        <div class="container-fluid home-container">
            <div class="row">

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
                                    <option class="specie-dog" value="golden"><spring:message code="dog.golden"/></option>
                                    <option class="specie-dog" value="collie"><spring:message code="dog.collie"/></option>
                                    <option class="specie-cat" value="siamese"><spring:message code="cat.siamese"/></option>
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
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="card-deck row">

                            <c:forEach var="pet" items="${home_pet_list}">
                                <div class="col-auto mb-3">

                                    <t:animalCard pet="${pet}"/>

                                </div>
                            </c:forEach>

                        </div>
                    </div>
                </div>

            </div>

        </div>

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
