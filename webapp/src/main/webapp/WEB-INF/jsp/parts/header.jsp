<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="pendingRequests" scope="application" value="4"/>

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
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/requests">Requests</a></h4>
            </li>
            <li class="nav-item notify-container">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/interests">Interests</a></h4>
                <c:if test="${!(pendingRequests eq 0)}"><span class="notify-bubble">${pendingRequests}</span></c:if>

            </li>
            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/">My Profile</a></h4>
            </li>
        </ul>

        <form class="form-inline my-2 my-lg-0" method="GET" action="${pageContext.request.contextPath}/">
            <input id="search-value" name="find" class="form-control mr-sm-2" type="search" placeholder="<spring:message code="search"/>" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit" ><spring:message code="search"/></button>
        </form>
    </div>
</nav>