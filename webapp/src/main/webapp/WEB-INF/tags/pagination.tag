<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>

<%@attribute name="currentPage" required="true" type="java.lang.String"%>
<%@attribute name="maxPage" required="true" type="java.lang.String"%>
<%@attribute name="baseURL" required="true" type="java.lang.String"%>

<nav aria-label="..." class="text-center">
    <ul class="pagination">
        <c:url value="${baseURL}" var="prev">
            <c:param name="page" value="${currentPage-1}"/>
            <c:if test="${not empty param.species}">
                <c:param name="species" value="${param.species}"/>
            </c:if>
            <c:if test="${not empty param.breed}">
                <c:param name="breed" value="${param.breed}"/>
            </c:if>
            <c:if test="${not empty param.gender}">
                <c:param name="gender" value="${param.gender}"/>
            </c:if>
            <c:if test="${not empty param.priceRange}">
                <c:param name="priceRange" value="${param.priceRange}"/>
            </c:if>

            <c:if test="${not empty param.searchCriteria}">
                <c:param name="searchCriteria" value="${param.searchCriteria}"/>
            </c:if>
            <c:if test="${not empty param.searchOrder}">
                <c:param name="searchOrder" value="${param.searchOrder}"/>
            </c:if>
            <c:if test="${not empty param.find}">
                <c:param name="find" value="${param.find}"/>
            </c:if>
            <c:if test="${not empty param.status}">
                <c:param name="status" value="${param.status}"/>
            </c:if>
            <c:if test="${not empty param.province}">
                <c:param name="province" value="${param.province}"/>
            </c:if>
            <c:if test="${not empty param.department}">
                <c:param name="department" value="${param.department}"/>
            </c:if>
            <c:if test="${not empty param.showAllReviews}">
                <c:param name="showAllReviews" value="${param.showAllReviews}"/>
            </c:if>
        </c:url>
        <c:if test="${currentPage > 1}">
            <li class="page-item"><a class="page-link" href="<c:out value="${prev}" />" ><spring:message code="prev"/> </a></li>
        </c:if>

        <c:forEach begin="1" end="${maxPage}" step="1" varStatus="i">
            <c:choose>
                <c:when test="${currentPage == i.index}">
                    <li class="page-item active"><a class="page-link">${i.index}</a></li>
                </c:when>
                <c:otherwise>
                    <c:url value="${baseURL}" var="url">
                        <c:param name="page" value="${i.index}"/>
                        <c:if test="${not empty param.species}">
                            <c:param name="species" value="${param.species}"/>
                        </c:if>
                        <c:if test="${not empty param.breed}">
                            <c:param name="breed" value="${param.breed}"/>
                        </c:if>
                        <c:if test="${not empty param.gender}">
                            <c:param name="gender" value="${param.gender}"/>
                        </c:if>
                        <c:if test="${not empty param.priceRange}">
                            <c:param name="priceRange" value="${param.priceRange}"/>
                        </c:if>
                        <c:if test="${not empty param.searchCriteria}">
                            <c:param name="searchCriteria" value="${param.searchCriteria}"/>
                        </c:if>
                        <c:if test="${not empty param.searchOrder}">
                            <c:param name="searchOrder" value="${param.searchOrder}"/>
                        </c:if>
                        <c:if test="${not empty param.find}">
                            <c:param name="find" value="${param.find}"/>
                        </c:if>
                        <c:if test="${not empty param.status}">
                            <c:param name="status" value="${param.status}"/>
                        </c:if>
                        <c:if test="${not empty param.reqstatus}">
                            <c:param name="reqstatus" value="${param.reqstatus}"/>
                        </c:if>
                        <c:if test="${not empty param.province}">
                            <c:param name="province" value="${param.province}"/>
                        </c:if>
                        <c:if test="${not empty param.department}">
                            <c:param name="department" value="${param.department}"/>
                        </c:if>
                        <c:if test="${not empty param.showAllReviews}">
                            <c:param name="showAllReviews" value="${param.showAllReviews}"/>
                        </c:if>
                    </c:url>
                    <li class="page-item"><a class="page-link" href='<c:out value="${url}" />'>${i.index}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:url value="${baseURL}" var="next">

            <c:param name="page" value="${currentPage + 1}"/>
            <c:if test="${not empty param.species}">
                <c:param name="species" value="${param.species}"/>
            </c:if>
            <c:if test="${not empty param.breed}">
                <c:param name="breed" value="${param.breed}"/>
            </c:if>
            <c:if test="${not empty param.gender}">
                <c:param name="gender" value="${param.gender}"/>
            </c:if>
            <c:if test="${not empty param.priceRange}">
                <c:param name="priceRange" value="${param.priceRange}"/>
            </c:if>
            <c:if test="${not empty param.searchCriteria}">
                <c:param name="searchCriteria" value="${param.searchCriteria}"/>
            </c:if>
            <c:if test="${not empty param.searchOrder}">
                <c:param name="searchOrder" value="${param.searchOrder}"/>
            </c:if>
            <c:if test="${not empty param.find}">
                <c:param name="find" value="${param.find}"/>
            </c:if>
            <c:if test="${not empty param.status}">
                <c:param name="status" value="${param.status}"/>
            </c:if>
            <c:if test="${not empty param.reqstatus}">
                <c:param name="reqstatus" value="${param.reqstatus}"/>
            </c:if>
            <c:if test="${not empty param.province}">
                <c:param name="province" value="${param.province}"/>
            </c:if>
            <c:if test="${not empty param.department}">
                <c:param name="department" value="${param.department}"/>
            </c:if>
            <c:if test="${not empty param.showAllReviews}">
                <c:param name="showAllReviews" value="${param.showAllReviews}"/>
            </c:if>
        </c:url>
        <c:if test="${currentPage + 1 <= maxPage}">
            <li class="page-item"><a class="page-link" href="<c:out value="${next}" />" ><spring:message code="next"/></a></li>
        </c:if>
    </ul>


</nav>
