<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="requestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp" >
                    <jsp:param name="destination" value="requests"/>
                </jsp:include>
                <div class="col ">
                    <div class="shadow p-3 bg-white rounded">
                        <h2>Pets you requested:</h2>
                        <c:if test="${empty requests_list}">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <c:choose>
                                    <c:when test="${(empty param.status || param.status eq 'any')
                                        && (empty param.searchCriteria || param.searchCriteria eq 'any')
                               }">
                                        <c:url var="homeUrl" value="/"/>
                                        <spring:message code="request.emptyRequests"/>
                                        <a href="${homeUrl}"><spring:message code="request.goToHome"/></a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/requests"><spring:message code="removeFilters"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:if>
                        <c:forEach var="req" items="${requests_list}">
                            <c:if test="${req.status.id eq 1}">
                                <div class="row bg-light p-1">
                                    <div class=" col-sm-11">
                                        <spring:message code="request.showedInterest" arguments="${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning">    ${req.creationDate}</small>
                                    </div>
                                    <div class="col-sm-1 ">
                                        <form method="POST" class="m-0" action="<c:url value="/requests-cancel/${req.id}"/>">
                                            <button  type="submit" name="newStatus" value="cancel" class="btn btn-danger"><spring:message code="cancel"/></button>
                                        </form>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.id eq 2}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-sm-11">
                                        <spring:message code="request.wasAccepted" arguments="${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning">    ${req.creationDate}</small>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${req.status.id eq 3}">
                                <div class="row p-1 bg-light resolved">
                                    <div class=" col-sm-11">
                                        <spring:message code="request.wasRejected" arguments="${pageContext.request.contextPath}/pet/${req.petId},${req.petName}"/>
                                        <small class="text-warning">    ${req.creationDate}</small>
                                    </div>
                                </div>
                            </c:if>
                        </c:forEach>

                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>