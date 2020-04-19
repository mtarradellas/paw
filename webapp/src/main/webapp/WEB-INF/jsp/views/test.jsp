<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>test</title>
</head>
<body>
<h1><c:out value="${pet.id}"/></h1>
<h1><c:out value="${pet.petName}"/></h1>
<h1><c:out value="${pet.location}"/></h1>
<h1><c:out value="${pet.species.en_US}"/></h1>
<h1><c:out value="${pet.breed.es_AR}"/></h1>
</body>
</html>
