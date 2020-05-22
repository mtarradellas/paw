<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="col-md-2 search-tools">
    <form class="card shadow p-3" method="get" action="${pageContext.request.contextPath}/${param.destination}">

        <div class="card-body">
            <div class="form-group">
                <label for="filter-status"><spring:message code="request.status"/></label>
                <select name="status" class="form-control" id="filter-status">
                    <option value="any"
                            <c:if test="${(not empty param.status) && (param.status eq 'any')}">
                                selected
                            </c:if>
                    ><spring:message code="filter.any"/></option>
                    <option value="accepted"
                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('accepted' eq param.status)}">
                                selected
                            </c:if>
                    ><spring:message code="request.accepted"/></option>
                    <option value="rejected"
                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('rejected' eq param.status)}">
                                selected
                            </c:if>
                    ><spring:message code="request.rejected"/></option>
                    <option value="pending"
                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('pending' eq param.status)}">
                                selected
                            </c:if>
                    ><spring:message code="request.pending"/></option>
                    <option value="canceled"
                            <c:if test="${(not empty param.status) && (param.status ne 'any') && ('canceled' eq param.status)}">
                                selected
                            </c:if>
                    ><spring:message code="request.canceled"/></option>
                </select>
            </div>
            <label for="search-criteria"><spring:message code="filter.criteria"/></label>
            <select name="searchCriteria" class="form-control" id="search-criteria">
                <option value="any"><spring:message code="filter.any"/></option>
                <option value="date"
                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'date')}">selected</c:if>
                ><spring:message code="request.date"/></option>
                <option value="petName"
                        <c:if test="${(not empty param.searchCriteria) && (param.searchCriteria eq 'petName')}">selected</c:if>
                ><spring:message code="request.petName"/></option>
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