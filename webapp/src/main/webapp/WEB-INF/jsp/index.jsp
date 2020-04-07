<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<body>
<%-- Comentario --%>
<h1>PET SOCIETY</h1>
<ul>
    <c:forEach var="pet" items="${home_pet_list}">
        <li><c:out value="${pet.name}" /></li>
    </c:forEach>
</ul>
</body>
</html>