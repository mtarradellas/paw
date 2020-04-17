<%@tag description="Centered layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

        <div id="body" class="page-content">
            <div class="container-fluid h-100">
                <div class="row justify-content-center h-100">
                    <div class="col-8 justify-content-center align-middle h-100">
                        <div class="shadow p-3 bg-white rounded">
                            <jsp:doBody/>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="/WEB-INF/jsp/parts/footer.jsp" />

        <script src="<c:url value="/resources/bootstrap-4.3.1/js/bootstrap.js"/>"></script>
        <script crossorigin="anonymous" src="<c:url value="https://kit.fontawesome.com/865baf5b70.js"/>"></script>
        <script src="<c:url value="/resources/js/header.js"/>"></script>
    </body>

</html>