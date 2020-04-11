<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1><spring:message code="pet"/></h1>
<h3>${single_pet_example.petName}</h3>
<p><c:out value="${single_pet_example.id}" /></p>
<p><c:out value="${single_pet_example.species}" /></p>
<p><c:out value="${single_pet_example.breed}" /></p>
<p><c:out value="${single_pet_example.description}" /></p>
<p><c:out value="${single_pet_example.price}" /></p>
<p><c:out value="${single_pet_example.ownerId}" /></p>
<p><c:out value="${single_pet_example.birthDate}" /></p>
</body>
</html>
