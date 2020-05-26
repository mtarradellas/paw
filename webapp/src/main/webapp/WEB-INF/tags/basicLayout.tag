<%@tag description="Basic layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>


<html>
    <head>
        <link rel="stylesheet" href="<c:url value="/resources/bootstrap-4.3.1/css/bootstrap.css"/>"/>
        <link rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>

        <link rel="apple-touch-icon" sizes="180x180" href="<c:url value="/apple-touch-icon.png"/>"/>
        <link rel="icon" type="image/png" sizes="32x32" href="<c:url value="/favicon-32x32.png"/>"/>
        <link rel="icon" type="image/png" sizes="16x16" href="<c:url value="/favicon-16x16.png"/>"/>
        <link rel="manifest" sizes="180x180" href="<c:url value="/site.webmanifest"/>"/>

        <title>
            <c:out value="${title}"/>
        </title>
    </head>
    <body>
        <script src="<c:url value="/resources/jquery/jquery-3.4.1.slim.min.js"/>"></script>

        <t:header loggedUser="${loggedUser}"/>

        <div id="body" class="page-content">
                <jsp:doBody/>
        </div>

        <jsp:include page="/WEB-INF/jsp/parts/footer.jsp" />

        <script src="<c:url value="/resources/bootstrap-4.3.1/js/bootstrap.js"/>"></script>
        <script crossorigin="anonymous" src="<c:url value="https://kit.fontawesome.com/865baf5b70.js"/>"></script>

    </body>
</html>