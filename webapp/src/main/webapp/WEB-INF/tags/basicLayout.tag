<%@tag description="Basic layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="title" required="true" type="java.lang.String"%>

<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/bootstrap-4.3.1/css/bootstrap.css"/>"/>
        <link rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
        <title>
            <c:out value="${title}"/>
        </title>
    </head>
    <body>
        <script src="<c:url value="/resources/jquery/jquery-3.4.1.slim.min.js"/>"></script>

        <jsp:include page="/WEB-INF/jsp/parts/header.jsp" />

        <div class="page-content">
            <div class="p-4">
                <h1><c:out value="${title}"/></h1>
            </div>

            <div id="body">
                <jsp:doBody/>
            </div>
        </div>

        <jsp:include page="/WEB-INF/jsp/parts/footer.jsp" />

        <script src="<c:url value="/resources/bootstrap-4.3.1/js/bootstrap.js"/>"></script>
        <script crossorigin="anonymous" src="<c:url value="https://kit.fontawesome.com/865baf5b70.js"/>"></script>


    </body>
</html>