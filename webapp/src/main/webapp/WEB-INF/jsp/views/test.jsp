<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<%--<c:forEach items="${request_list}" var="r">--%>
<h1><c:out value="${contact.email}"/></h1>
<h1><c:out value="${contact.username}"/></h1>

<%--</c:forEach>--%>
</body>
</html>
