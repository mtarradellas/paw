<%@tag description="Admin layout" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="item" required="true" type="java.lang.String" %>

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
<script src="<c:url value="/resources/js/admin_control.js"/>"></script>

<nav class="navbar navbar-expand-lg admin-header">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/admin">
        <img src="<c:url value="/resources/images/logo.png"/>" alt="logo" height="70" width="70"/>
    </a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">

            <li class="nav-item active">
                <%--            TODO: cambiar el path del url--%>
                <h1><a class="nav-link" href="${pageContext.request.contextPath}/admin" id="home-link">PET SOCIETY Admin</a></h1>
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/admin/pets"><spring:message code="adminHeader.listPets"/></a></h4>
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/admin/users"><spring:message code="adminHeader.listUsers"/></a></h4>
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/admin/requests"><spring:message code="adminHeader.listRequests"/></a></h4>
            </li>


        </ul>

        <form class="form-inline my-2 my-lg-0" method="GET" action="${pageContext.request.contextPath}/admin/${item}">
            <input id="search-value" name="find" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="search"/>" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit" ><spring:message code="search"/></button>
        </form>

    </div>

    <div class="p-3">
        <c:out value="${loggedUser.username}"/> <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>

</nav>

<div id="body" class="page-content">
    <jsp:doBody/>
</div>

<script src="<c:url value="/resources/bootstrap-4.3.1/js/bootstrap.js"/>"></script>
<script crossorigin="anonymous" src="<c:url value="https://kit.fontawesome.com/865baf5b70.js"/>"></script>

</body>
</html>
