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
                        <div class="row bg-light p-1">
                            <div class=" col-sm-11">
                                You showed interest in <a href="${pageContext.request.contextPath}/">Callie</a>
                                <small class="text-warning">  27.11.2014, 12:00</small>
                            </div>
                            <div class="col-sm-1 ">
                                <button type="button" class="btn btn-danger">Cancel</button>
                            </div>
                        </div>
                        <div class="row p-1 bg-light resolved">
                            <div class=" col-sm-11">
                                Your request for <a href="${pageContext.request.contextPath}/">Fido</a> was accepted by <a href="${pageContext.request.contextPath}/">John Johnson</a>!
                                <small class="text-warning">  12.02.2020, 12:00</small>
                            </div>
                        </div>
                        <div class="row bg-light p-1 resolved">
                            <div class=" col-sm-11">
                                Your request for <a href="${pageContext.request.contextPath}/">Bobby</a> was rejected by <a href="${pageContext.request.contextPath}/">Jack Jackson</a>!
                                <small class="text-warning">  27.11.2014, 12:00</small>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>