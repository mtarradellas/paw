<%@tag description="Header" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@attribute name="loggedUser" required="true" type="ar.edu.itba.paw.models.User"%>



<nav class="navbar navbar-expand-lg header">
    <a class="navbar-brand" href="${pageContext.request.contextPath}/">
        <img src="<c:url value="/resources/images/logo.png"/>" alt="logo" height="70" width="70"/>
    </a>

    <div class="navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto mt-2 mt-lg-0">

            <li class="nav-item active">
                <h1><a class="nav-link" href="${pageContext.request.contextPath}/" id="home-link">PET SOCIETY</a></h1>
            </li>

            <li class="nav-item">
                <h4><a class="nav-link" href="${pageContext.request.contextPath}/upload-pet"><spring:message code="header.addPet"/></a></h4>
            </li>

            <c:if test="${not empty loggedUser}">
                <li class="nav-item">
                    <h4><a class="nav-link" href="${pageContext.request.contextPath}/requests"><spring:message code="header.requests"/></a></h4>
                </li>

                <li class="nav-item">
                    <h4><a class="nav-link" href="${pageContext.request.contextPath}/interests"><spring:message code="header.interests"/></a></h4>
                </li>

                <li class="nav-item">
                    <h4><a class="nav-link" href="${pageContext.request.contextPath}/user/${loggedUser.id}"><spring:message code="header.myProfile"/></a></h4>
                </li>
            </c:if>
        </ul>

        <form class="form-inline pt-1 my-2 my-lg-0 ui-widget" method="GET" action="${pageContext.request.contextPath}/">
            <input id="search-value" name="find" class="form-control mr-sm-2" path="search" type="search"
                   placeholder="<spring:message code="search"/>" aria-label="Search"
            <c:if test="${not empty param.find}">
                value="${param.find}"
            </c:if>
            >
            <c:if test="${param.status ne null}">
                <input type="hidden" name="status" value="${param.status}">
            </c:if>
            <c:if test="${param.searchOrder ne null}">
                <input type="hidden" name="searchOrder" value="${param.searchOrder}">
            </c:if>
            <c:if test="${param.searchCriteria ne null}">
                <input type="hidden" name="searchCriteria" value="${param.searchCriteria}">
            </c:if>
            <c:if test="${param.priceRange ne null}">
                <input type="hidden" name="priceRange" value="${param.priceRange}">
            </c:if>
            <c:if test="${param.gender ne null}">
                <input type="hidden" name="gender" value="${param.gender}">
            </c:if>
            <c:if test="${param.breed ne null}">
                <input type="hidden" name="breed" value="${param.breed}">
            </c:if>
            <c:if test="${param.species ne null}">
                <input type="hidden" name="species" value="${param.species}">
            </c:if>
            <c:if test="${param.province ne null}">
                <input type="hidden" name="province" value="${param.province}">
            </c:if>
            <c:if test="${param.department ne null}">
                <input type="hidden" name="department" value="${param.department}">
            </c:if>

            <button class="btn btn-outline-success my-2 my-sm-0" type="submit" ><spring:message code="search"/></button>
        </form>

    </div>

    <div class="p-3">
        <c:choose>
            <c:when test="${empty loggedUser}">
                <ul class="navbar-nav mr-auto mt-2 mt-lg-0 pl-3">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/register"><spring:message code="signup"/></a> </li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/login"><spring:message code="login"/></a> </li>
                </ul>
            </c:when>
            <c:otherwise>
                <c:out value="${loggedUser.username}"/> <a href="${pageContext.request.contextPath}/logout"><spring:message code="logout"/></a>
            </c:otherwise>
        </c:choose>
    </div>

    <script>
        $(document).ready(function() {
            $(function() {
                $("#search-value").autocomplete({
                    source: function(request, response) {
                        $.ajax({
                            url: '${pageContext.request.contextPath}/search',
                            type: "GET",
                            data: { term: request.term },

                            dataType: "json",

                            success: function(data) {
                                response(data);
                            }
                        });
                    }
                });
            });
        });
    </script>

</nav>