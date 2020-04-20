<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indexTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">

                <jsp:include page="/WEB-INF/jsp/parts/search-tools-pet.jsp" />

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
                    </div>
                </div>

            </div>

        </div>

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
