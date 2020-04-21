<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<c:forEach items="${species_list}" var="s">
<h1><c:out value="${s.id}"/></h1>
<h1><c:out value="${s.name}"/></h1>
</c:forEach>

</body>
</html>
