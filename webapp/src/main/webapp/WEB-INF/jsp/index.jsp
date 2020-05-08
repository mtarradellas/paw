<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indexTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">

                <t:search-tools-pet model="${searchTools}" breeds_list="${breeds_list}" species_list="${species_list}"/>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/'}" />
                            </c:if>
                        </div>

                        <c:if test="${empty home_pet_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/"><spring:message code="showFirst"/></a>
                            </div>
                        </c:if>

                        <div class="card-deck row">
                            <c:forEach var="pet" items="${home_pet_list}">
                                <div class="col-auto mb-3">

                                    <t:animalCard pet="${pet}"/>

                                </div>
                            </c:forEach>
                        </div>
                        <div class="m-2">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/'}" />
                            </c:if>
                        </div>
                    </div>
                </div>


            </div>

        </div>

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
