<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout>
    <jsp:body>
        <ul>
            <c:forEach var="pet" items="${home_pet_list}">
                <li>
                    <ul>
                        <t:animalCard pet="${pet}"/>
                    </ul>
                </li>
            </c:forEach>
        </ul>
    </jsp:body>
</t:basicLayout>