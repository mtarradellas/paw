<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indexTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">

                <t:search-tools-pet breeds_list="${breeds_list}" species_list="${species_list}"/>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <c:if test="${empty home_pet_list }">
                        <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                            <a href="${pageContext.request.contextPath}/"><spring:message code="showAll"/></a>
                        </div>

                        </c:if>
                        <div class="card-deck row">
                            <c:forEach var="pet" items="${home_pet_list}">
                                <div class="col-auto mb-3">

                                    <t:animalCard pet="${pet}"/>

                                </div>
                            </c:forEach>
                        </div>
                        <div class="text-center">
                            <c:url value="/" var="prev">
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
                                <c:if test="${not empty param.searchCriteria}">
                                    <c:param name="searchCriteria" value="${param.searchCriteria}"/>
                                </c:if>
                                <c:if test="${not empty param.searchOrder}">
                                    <c:param name="searchOrder" value="${param.searchOrder}"/>
                                </c:if>
                                <c:if test="${not empty param.find}">
                                    <c:param name="find" value="${param.find}"/>
                                </c:if>
                            </c:url>
                            <c:if test="${currentPage > 1}">
                                <a href="<c:out value="${prev}" />" class="pn prev">< prev</a>
                            </c:if>

                            <c:forEach begin="1" end="${maxPage}" step="1" varStatus="i">
                                <c:choose>
                                    <c:when test="${currentPage == i.index}">
                                        <span>${i.index}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:url value="/" var="url">
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
                                            <c:if test="${not empty param.searchCriteria}">
                                                <c:param name="searchCriteria" value="${param.searchCriteria}"/>
                                            </c:if>
                                            <c:if test="${not empty param.searchOrder}">
                                                <c:param name="searchOrder" value="${param.searchOrder}"/>
                                            </c:if>
                                            <c:if test="${not empty param.find}">
                                                <c:param name="find" value="${param.find}"/>
                                            </c:if>
                                        </c:url>
                                        <a href='<c:out value="${url}" />'>${i.index}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:url value="/" var="next">

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
                                <c:if test="${not empty param.searchCriteria}">
                                    <c:param name="searchCriteria" value="${param.searchCriteria}"/>
                                </c:if>
                                <c:if test="${not empty param.searchOrder}">
                                    <c:param name="searchOrder" value="${param.searchOrder}"/>
                                </c:if>
                                <c:if test="${not empty param.find}">
                                    <c:param name="find" value="${param.find}"/>
                                </c:if>
                            </c:url>
                            <c:if test="${currentPage + 1 <= maxPage}">
                                <a href='<c:out value="${next}" />' class="pn next">next ></a>
                            </c:if>
                        </div>

                    </div>
                </div>


            </div>

        </div>

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
