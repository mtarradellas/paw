<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="adminTitle" var="adminTitle"/>
<t:adminLayout title="${adminTitle}" item="pets">
    <jsp:body>
        <div id="body" class="page-content">

            <div class="col">
                <div class="shadow p-3 bg-white rounded">
                    <h2><b><spring:message code="welcomeMessage"/> </b></h2>
                    <p><spring:message code="mainPageDescription1"/></p>
                    <div>
                        <div class="col p-2">
                            <a href="${pageContext.request.contextPath}/admi/pets" class="btn btn-secondary btn-lg btn-block btn-secondary " role="button" aria-pressed="true">
                                <spring:message code="adminHeader.listPets"/>
                            </a>
                        </div>
                        <div class="col p-2">
                            <a href="${pageContext.request.contextPath}/admi/users" class="btn btn-secondary btn-lg btn-block btn-secondary" role="button" aria-pressed="true">
                                <spring:message code="adminHeader.listUsers"/>
                            </a>
                        </div>
                        <div class="col p-2">
                            <a href="${pageContext.request.contextPath}/admi/requests" class="btn btn-secondary btn-lg btn-block btn-secondary" role="button" aria-pressed="true">
                                <spring:message code="adminHeader.listRequests"/>
                            </a>
                        </div>


                    </div>
                    <br><br>
                    <p><spring:message code="mainPageDescription2"/></p>
                    <div >
                        <div class="col p-2">
                            <a type="button" class="btn btn-info btn-lg btn-block"
                               href="${pageContext.request.contextPath}/admi/upload-pet">
                                <i class="fas fa-plus mr-2"></i>
                                <spring:message code="addPet"/>
                            </a>
                        </div>
                        <div class="col p-2">
                            <a type="button" class="btn btn-info btn-lg btn-block"
                               href="${pageContext.request.contextPath}/admi/upload-user">
                                <i class="fas fa-plus mr-2"></i>
                                <spring:message code="addUser"/>
                            </a>
                        </div>
                        <div class="col p-2">
                            <a type="button" class="btn btn-info btn-lg btn-block"
                               href="${pageContext.request.contextPath}/admi/upload-request">
                                <i class="fas fa-plus mr-2"></i>
                                <spring:message code="addRequest"/>
                            </a>
                        </div>


                    </div>


                </div>
            </div>
        </div>


    </jsp:body>
</t:adminLayout>