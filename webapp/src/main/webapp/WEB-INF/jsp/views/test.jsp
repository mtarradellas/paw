<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<%--<c:forEach items="${users}" var="user">--%>
<h1><c:out value="${pet.id}"/></h1>
<h1><c:out value="${pet.petName}"/></h1>
<h1><c:out value="${pet.status.name}"/></h1>
<h1><c:out value="${pet.province.name}"/></h1>
<h1><c:out value="${pet.department.name}"/></h1>
<%--<h1><c:out value="${user.status.name}"/></h1>--%>


<%--</c:forEach>--%>
</body>
</html>
