<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1><spring:message code="users"/></h1>
<ul>
    <c:forEach var="user" items="${users_list}">
        <li>
            <ul>
                <li><c:out value="${user.id}" /></li>
                <li><c:out value="${user.username}" /></li>
                <li><c:out value="${user.mail}" /></li>
                <li><c:out value="${user.phone}" /></li>
            </ul>
        </li>
    </c:forEach>
</ul>
</body>
</html>