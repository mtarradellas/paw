<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<nav class="navbar navbar-expand-lg header">
    <a class="navbar-brand" href="/">
        <img src="<c:url value="/resources/images/logo.png"/>" alt="logo" height="70" width="70"/>
    </a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <h1><a href="/" id="home-link">PET SOCIETY</a></h1>
            </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
            <input class="form-control mr-sm-2" type="search" placeholder="<spring:message code="header.search"/>" aria-label="Search">
            <button class="btn btn-outline-success my-2 my-sm-0" type="submit"><spring:message code="header.search"/></button>
        </form>
    </div>
</nav>