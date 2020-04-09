<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1>Pet Society</h1>
<ul>
    <c:forEach var="pet" items="${home_pet_list}">
        <li>
            <ul>
                <li><c:out value="${pet.name}" /></li>
                <li><c:out value="${pet.species}" /></li>
                <li><c:out value="${pet.breed}" /></li>
                <li><c:out value="${pet.gender}" /></li>
                <li><c:out value="${pet.location}" /></li>
                <li><c:out value="${pet.price}" /></li>
            </ul>
        </li>
    </c:forEach>
</ul>
</body>
</html>