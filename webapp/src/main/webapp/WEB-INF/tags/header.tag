<%@tag description="Header" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="loggedUser" required="true" type="ar.edu.itba.paw.models.User"%>



<nav class="navbar navbar-expand-lg header">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">
        <img src="<c:url value="/resources/images/logo.png"/>" alt="logo" height="70" width="70"/>
    </a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">

            <li class="nav-item active">
                <h1><a class="nav-link" href="${pageContext.request.contextPath}/" id="home-link">PET SOCIETY</a></h1>
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/contact"><spring:message code="header.addPet"/></a></h4>
            </li>

        <c:if test="${not empty loggedUser}">
            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/requests"><spring:message code="header.requests"/></a></h4>
            </li>
<<<<<<< HEAD:webapp/src/main/webapp/WEB-INF/jsp/parts/header.jsp
            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/interests"><spring:message code="header.interests"/></a></h4>
=======

            <li class="nav-item notify-container">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/interests"><spring:message code="header.interests"/></a></h4>
                <c:if test="${!(pendingRequests eq 0)}"><span class="notify-bubble">${pendingRequests}</span></c:if>
>>>>>>> 137ba9515fe7c83409aa51225965281750d02d8e:webapp/src/main/webapp/WEB-INF/tags/header.tag
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/user/${loggedUser.id}"><spring:message code="header.myProfile"/></a></h4>
            </li>
        </c:if>
        </ul>

        <form class="form-inline my-2 my-lg-0" method="GET" action="${pageContext.request.contextPath}/">
            <input id="search-value" name="find" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="search"/>" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit" ><spring:message code="search"/></button>
        </form>

    </div>

    <div class="p-3">
        <c:choose>
            <c:when test="${empty loggedUser}">
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0 pl-3">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/register">Sign Up</a> </li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login">Log In</a> </li>
                </ul>
            </c:when>
            <c:otherwise>
                <c:out value="${loggedUser.username}"/> <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:otherwise>
        </c:choose>
    </div>

</nav>