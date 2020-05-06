<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<spring:message code="adminTitle" var="adminTitle"/>
<t:adminLayout title="${adminTitle}">
<jsp:body>
        <div id="body" class="page-content">

            <div class="col">
                <div class="shadow p-3 bg-white rounded">
                    <div class="m-2 ">
                        <c:if test="${maxPage ne 1}">
                            <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/users/'}" />
                        </c:if>
                    </div>

                    <c:if test="${empty users_list }">
                        <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                            <a href="${pageContext.request.contextPath}/"><spring:message code="showFirst"/></a>
                        </div>

                    </c:if>
                    <div>
                        <h2><spring:message code="admin.usersListing"/></h2>
                    </div>
                    <div class="card-deck row">
                        <ul class="list-group">
                            <c:forEach var="user" items="${users_list}">
                                    <li class="list-group-item">${user.username}</li>
                            </c:forEach>
                        </ul>
                    </div>
                    <div class="m-2">
                        <c:if test="${maxPage ne 1}">
                            <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/admi/users'}" />
                        </c:if>
                    </div>
                </div>
            </div>

        </div>

    </jsp:body>
</t:adminLayout>