<%--
  Created by IntelliJ IDEA.
  User: lkarpovich
  Date: 12/4/20
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Test PET W IMAGE</title>
</head>
<body>
<h1><c:out value="${test_pet_image.petName}" /> </h1>
<ul>
    <c:forEach var="img" items="${test_pet_image.images}">
        <li>
            <ul>
                <li><img src="${img.url}" alt="test"/></li>

            </ul>
        </li>
    </c:forEach>
</ul>
</body>
</html>
