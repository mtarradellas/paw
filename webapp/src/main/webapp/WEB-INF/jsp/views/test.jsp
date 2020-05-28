<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<%--<h1><c:out value="${species_list.size()}"/></h1>--%>
<c:forEach items="${provinces_list}" var="province">
    <h1><c:out value="${province.id}"/></h1>
    <h1><c:out value="${province.name}"/></h1>
<%--    <h1><c:out value="${breed.name}"/></h1>--%>
<%--    <h1><c:out value="${breed.name}"/></h1>--%>
<%--    <h1><c:out value="${species.breedList.size()}"/></h1>--%>
</c:forEach>

<%--<h1><c:out value="${breed_list.size()}"/></h1>--%>
<%--<c:forEach items="${breed_list}" var="breed">--%>
<%--    <h1><c:out value="${breed.species.name}"/></h1>--%>
<%--</c:forEach>--%>
</body>
</html>
