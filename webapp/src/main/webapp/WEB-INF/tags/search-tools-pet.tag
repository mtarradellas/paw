<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@attribute name="model" required="true" type="ar.edu.itba.paw.webapp.form.SearchTools" %>

<%@attribute name="breeds_list" required="true" type="ar.edu.itba.paw.models.Breed[]" %>
<%@attribute name="species_list" required="true" type="ar.edu.itba.paw.models.Species[]" %>

<div class="col-md-2 search-tools">
    <form:form modelAttribute="searchTools" class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/">
        <div class="card-header">
            <h5 class="card-title"><spring:message code="filter.options"/></h5>
        </div>
        <div class="card-body">
            <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter"/></h6>
            <div class="form-group">

                <form:label path="species"><spring:message code="pet.species"/></form:label>
                <form:select path="species" cssClass="form-control">
                    <form:options items="${species_list}" itemLabel="name" itemValue="id"/>
                </form:select>

                <%-- TODO: setear cuando deberiá estar disabled y cuales de las options habilitadas --%>
                <form:label path="breed"><spring:message code="pet.breed"/></form:label>
                <form:select path="breed" cssClass="form-control">
                    <form:options items="${breeds_list}" itemLabel="name" itemValue="id"/>
                </form:select>

                <label for="price-filter"><spring:message code="pet.price"/></label>
                <div class="row" id="price-filter">
                    <div class="col pr-0">
                        <spring:message var="minPriceTxt" code="searchTool.minPrice"/>
                        <form:input placeholder="${minPriceTxt}" cssClass="form-control" path="minPrice" type="number"/>
                    </div>
                    <div class="col pl-0">
                        <spring:message var="maxPriceTxt" code="searchTool.maxPrice"/>
                        <form:input placeholder="${minPriceTxt}" cssClass="form-control ml-1" path="maxPrice" type="number"/>
                    </div>
                </div>

                <form:label path="filterGender"><spring:message code="pet.sex"/></form:label>
                <form:select path="filterGender">
                    <form:option value="${null}"><spring:message code="filter.any"/></form:option>
                    <form:option value="male"><spring:message code="pet.male"/></form:option>
                    <form:option value="female"><spring:message code="pet.female"/></form:option>
                </form:select>

            </div>
            <div class="form-group">
                <h6 class="card-subtitle mb-2 text-muted"><spring:message code="filter.orderBy"/></h6>

                <form:label path="searchCriteria"><spring:message code="filter.criteria"/></form:label>
                <form:select path="searchCriteria" cssClass="form-control">
                    <form:option value="${null}"><spring:message code="filter.any"/></form:option>
                    <form:option value="species"><spring:message code="pet.species"/></form:option>
                    <form:option value="sex"><spring:message code="pet.sex"/></form:option>
                    <form:option value="price"><spring:message code="pet.price"/></form:option>
                    <form:option value="upload-date"><spring:message code="pet.date"/></form:option>
                    <form:option value="breed"><spring:message code="pet.breed"/></form:option>
                </form:select>

                <%-- TODO: disable if search criteria is set as any --%>
                <form:label path="searchOrder"><spring:message code="filter.order"/></form:label>
                <form:select path="searchOrder" cssClass="form-control">
                    <form:option value="asc"><spring:message code="filter.ascending"/></form:option>
                    <form:option value="desc"><spring:message code="filter.descending"/></form:option>
                </form:select>
            </div>
        </div>
        <div class="card-footer" id="search-tools-submit">
            <button type="submit" class="blue-button"><spring:message code="filter"/></button>
        </div>
    </form:form>
</div>