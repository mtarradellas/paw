<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<c:forEach items="${pet_list}" var="pet">
<h1><c:out value="${pet.id}"/></h1>
<h1><c:out value="${pet.petName}"/></h1>
    <c:forEach items="${pet.images}" var="i">
        <h1><c:out value="${i}"/></h1>
    </c:forEach>
    <h1><c:out value="---------------"/></h1>
</c:forEach>

</body>
</html>
