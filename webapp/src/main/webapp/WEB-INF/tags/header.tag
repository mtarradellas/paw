<%@tag description="Header" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="loggedUser" required="true" type="ar.edu.itba.paw.models.User"%>



<nav class="custom-navbar">
    <ul>
        <li class="nav-left">
            <a href="${pageContext.request.contextPath}/">
                <img src="<c:url value="/resources/images/logo.png"/>" alt="logo" height="70" width="70"/>
            </a>
        </li>

        <li class="nav-left">
            <h1><a href="${pageContext.request.contextPath}/" id="home-link">PET SOCIETY</a></h1>
        </li>

        <li>
            <h4><a href="${pageContext.request.contextPath}/upload-pet"><spring:message code="header.addPet"/></a></h4>
        </li>

        <c:if test="${not empty loggedUser}">
            <li>
                <h4><a href="${pageContext.request.contextPath}/requests"><spring:message code="header.requests"/></a></h4>
            </li>

            <li>
                <h4><a href="${pageContext.request.contextPath}/interests"><spring:message code="header.interests"/></a></h4>
            </li>

            <li>
                <h4><a href="${pageContext.request.contextPath}/user/${loggedUser.id}"><spring:message code="header.myProfile"/></a></h4>
            </li>
        </c:if>


        <li class="nav-right">
            <form class="navbar-form" method="GET" action="${pageContext.request.contextPath}/">
                <div class="input-group">
                    <input id="search-value" name="find" class="form-control" type="search"
                           placeholder="<spring:message code="search"/>" aria-label="Search">
                    <button class="btn btn-outline-success ml-1" type="submit" ><spring:message code="search"/></button>
                </div>
            </form>
        </li>

        <c:choose>
            <c:when test="${empty loggedUser}">
                <li class="nav-right">
                    <a href="${pageContext.request.contextPath}/register"><spring:message code="signup"/></a>
                </li>
                <li class="nav-right">
                    <a href="${pageContext.request.contextPath}/login"><spring:message code="login"/></a>
                </li>
            </c:when>
            <c:otherwise>
                <li class="nav-right">
                    <span><c:out value="${loggedUser.username}"/></span>
                </li>
                <li class="nav-right">
                    <a href="${pageContext.request.contextPath}/logout"><spring:message code="logout"/></a>
                </li>
            </c:otherwise>
        </c:choose>
    </ul>
</nav>