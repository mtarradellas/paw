<%@tag description="Basic layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>

<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/parts/header.jsp" />

<div id="body">
    <jsp:doBody/>
</div>

<jsp:include page="/WEB-INF/jsp/parts/footer.jsp" />

</body>
</html>