<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1><spring:message code="user"/></h1>
<h3>${single_user_example.username}</h3>
<p><c:out value="${single_user_example.id}" /></p>
<p><c:out value="${single_user_example.mail}" /></p>
<p><c:out value="${single_user_example.phone}" /></p>
</body>
</html>