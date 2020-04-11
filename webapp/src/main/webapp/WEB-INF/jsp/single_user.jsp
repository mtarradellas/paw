<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
<head>
    <title>Pet Society</title>
</head>
<body>
<h1>User</h1>
<h3>${single_user_example.username}</h3>
<p><c:out value="${single_user_example.id}" /></p>
<p><c:out value="${single_user_example.mail}" /></p>
<p><c:out value="${single_user_example.phone}" /></p>
</body>
</html>