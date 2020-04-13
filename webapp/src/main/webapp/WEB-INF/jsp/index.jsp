<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:basicLayout title="Buscar mascotas">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">

                <jsp:include page="/WEB-INF/jsp/parts/search-tools.jsp" />

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
