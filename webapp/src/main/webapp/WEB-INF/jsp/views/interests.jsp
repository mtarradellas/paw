<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="interestsTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">
                <jsp:include page="/WEB-INF/jsp/parts/search-tools-interests.jsp" />
                <div class="col ">
                    <div class="shadow p-3 bg-white rounded">
                        <h2>Users interested in your pets:</h2>
                        <div class="row bg-light p-1">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">Pedro Vedoya</a> is interested in <a href="${pageContext.request.contextPath}/">Callie</a>
                                <small class="text-warning">  27.11.2015, 15:00</small>
                            </div>
                            <div class="col-sm-2 ">
                                <button type="button" class="btn btn-success">Accept</button>
                                <button type="button" class="btn btn-danger">Reject</button>
                            </div>
                        </div>
                        <div class="row bg-light p-1">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">Manu Tarradellas</a> is interested in <a href="${pageContext.request.contextPath}/">Dying</a>
                                <small class="text-warning">  21.01.2010, 10:20</small>
                            </div>
                            <div class="col-sm-2 ">
                                <button type="button" class="btn btn-success">Accept</button>
                                <button type="button" class="btn btn-danger">Reject</button>
                            </div>
                        </div>
                        <div class="row bg-light p-1">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">Facu Astiz</a> is interested in <a href="${pageContext.request.contextPath}/">Barry the Bee</a>
                                <small class="text-warning">  02.11.2016, 20:34</small>
                            </div>
                            <div class="col-sm-2 ">
                                <button type="button" class="btn btn-success">Accept</button>
                                <button type="button" class="btn btn-danger">Reject</button>

                            </div>
                        </div>
                        <div class="row bg-light p-1">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">Lu Karpovich</a> is interested in <a href="${pageContext.request.contextPath}/">Basic Shit</a>
                                <small class="text-warning">  12.12.2019, 21:32</small>
                            </div>
                            <div class="col-sm-2 ">
                                <button type="button" class="btn btn-success">Accept</button>
                                <button type="button" class="btn btn-danger">Reject</button>
                            </div>
                        </div>
                        <div class="row bg-light p-1 resolved">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">El Lenia</a> was interested in <a href="${pageContext.request.contextPath}/">Franco</a>
                                <small class="text-warning">  22.01.2020, 00:02</small>
                            </div>
                            <div class="col-sm-2 ">
                                <p>Accepted</p>
                            </div>
                        </div>
                        <div class="row bg-light p-1 resolved">
                            <div class=" col-sm-10">
                                <a href="${pageContext.request.contextPath}/">Coronavirus</a> was interested in <a href="${pageContext.request.contextPath}/">China</a>
                                <small class="text-warning">  27.12.2019, 10:00</small>
                            </div>
                            <div class="col-sm-2 ">
                                <p>Rejected</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<c:url value="/resources/js/interests.js"/>"></script>
    </jsp:body>
</t:basicLayout>