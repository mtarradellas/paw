<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<c:forEach items="${request_list}" var="r">
<h1><c:out value="${r.id}"/></h1>
<h1><c:out value="${r.ownerId}"/></h1>
<h1><c:out value="${r.status.name}"/></h1>
</c:forEach>

</body>
</html>
