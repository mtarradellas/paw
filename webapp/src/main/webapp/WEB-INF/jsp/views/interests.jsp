<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="interestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <jsp class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp" >
                    <jsp:param name="destination" value="interests"/>
                </jsp:include>
                <div class="col ">
                    <div class="shadow p-3 bg-white rounded">
                        <h2>Users interested in your pets:</h2>
                            <c:if test="${empty requests }">
                                <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                </div>
                            </c:if>
                            <c:forEach var="req" items="${requests}">

                                <div class="row bg-light p-1">
                                    <div class=" col-sm-10">
                                        <spring:message code="request.isInterested" arguments="${pageContext.request.contextPath}/user/${req.ownerId},${req.ownerUsername},${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning">    ${req.creationDate}</small>
                                    </div>
                                    <div class="col-sm-2 ">
                                        <button type="button" class="btn btn-success">Accept</button>
                                        <button type="button" class="btn btn-danger">Reject</button>
                                    </div>
                                </div>
                            </c:forEach>


<%--                        <div class="row bg-light p-1 resolved">--%>
<%--                            <div class=" col-sm-10">--%>
<%--                                <a href="${pageContext.request.contextPath}/">El Lenia</a> was interested in <a href="${pageContext.request.contextPath}/">Franco</a>--%>
<%--                                <small class="text-warning">  22.01.2020, 00:02</small>--%>
<%--                            </div>--%>
<%--                            <div class="col-sm-2 ">--%>
<%--                                <p>Accepted</p>--%>
<%--                            </div>--%>
<%--                        </div>--%>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>