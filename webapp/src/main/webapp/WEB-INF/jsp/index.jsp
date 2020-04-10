<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout>
    <jsp:body>
        <div class="shadow-lg p-3 mb-5 bg-white rounded">
            <div class="card-deck">

                <c:forEach var="pet" items="${home_pet_list}">
                    <div class="col-auto mb-3">

                        <t:animalCard pet="${pet}"/>

                    </div>
                </c:forEach>

            </div>
        </div>
    </jsp:body>
</t:basicLayout>
