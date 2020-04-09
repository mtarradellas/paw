<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1>Pet Society</h1>
<ul>
    <c:forEach var="user" items="${users_list}">
        <li>
            <ul>
                <li><c:out value="${user.username}" /></li>
            </ul>
        </li>
    </c:forEach>
</ul>
</body>
</html>