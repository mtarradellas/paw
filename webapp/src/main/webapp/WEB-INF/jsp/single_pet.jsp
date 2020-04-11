<%--
  Created by IntelliJ IDEA.
  User: lkarpovich
  Date: 10/4/20
  Time: 10:49
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1>Pet</h1>
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
