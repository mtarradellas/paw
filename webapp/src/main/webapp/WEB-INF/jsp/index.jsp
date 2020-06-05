<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indexTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <c:if test="${wrongSearch}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <spring:message code="wrongSearch"/>
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        <div class="container-fluid">
            <div class="row">

                <t:search-tools-pet breedList="${breedList}" speciesList="${speciesList}"
                                    provinceList="${provinceList}" departmentList="${departmentList}"/>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <div class="col">
                                <h2>
                                    <c:if test="${empty find}">
                                        <b><spring:message code="indexMessage"/></b>
                                    </c:if>
                                    <c:if test="${not empty find}">
                                        <b><spring:message code="searchFor" arguments="${find}"/> </b>
                                    </c:if>

                                    <spring:message code="totalResults" arguments="${amount}"/>
                                </h2>
                            </div>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right mb-1"
                                        data-toggle="modal" data-target="#help"><b>?</b></button>
                            </div>
                        </div>

                        <div class="modal fade" id="help" tabindex="-1" role="dialog" aria-labelledby="helpTitle"
                             aria-hidden="true">
                            <div class="modal-dialog" role="document">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h3 class="modal-title" id="helpTitle"><spring:message code="help.title"/></h3>
                                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                            <span aria-hidden="true">&times;</span>
                                        </button>
                                    </div>
                                    <div class="modal-body">
                                        <h2><spring:message code="help.firstSteps.title"/></h2>
                                        <p><spring:message code="help.firstSteps.text"/></p>
                                        <h2><spring:message code="help.filter.title"/></h2>
                                        <p><spring:message code="help.filter.text"/></p>
                                        <h2><spring:message code="help.addPet.title"/></h2>
                                        <p><spring:message code="help.addPet.text"/></p>
                                        <h2><spring:message code="help.profile.title"/></h2>
                                        <p><spring:message code="help.profile.text"/></p>
                                        <h2><spring:message code="help.requests.title"/></h2>
                                        <p><spring:message code="help.requests.text"/></p>
                                        <h2><spring:message code="help.recoverUser.title"/></h2>
                                        <p><spring:message code="help.recoverUser.text"/></p>
                                    </div>
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="m-2 ">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/'}"/>
                            </c:if>
                        </div>
                        <hr>

                        <c:if test="${empty homePetList }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/"><spring:message code="showFirst"/></a>
                            </div>

                        </c:if>
                        <div class="card-deck row ml-5">
                            <c:forEach var="pet" items="${homePetList}">
                                <c:if test="${pet.status.value eq 1}">

                                    <div class="col-auto mb-3">

                                        <t:animalCard pet="${pet}" level="user"/>

                                    </div>

                                </c:if>

                            </c:forEach>
                        </div>
                        <hr>

                        <div class="m-2">
                            <c:if test="${maxPage ne 1}">
                                <t:pagination currentPage="${currentPage}" maxPage="${maxPage}" baseURL="${'/'}"/>
                            </c:if>
                        </div>
                    </div>
                </div>


            </div>

        </div>

        <link rel="stylesheet" href="<c:url value="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css"/>"/>
        
        <script src="<c:url value="http://code.jquery.com/jquery-1.10.2.js"/>"> </script>
        <script src="<c:url value="http://code.jquery.com/ui/1.10.3/jquery-ui.js"/>"></script>
        <script src="<c:url value="/resources/js/selector_dependency.js"/>"></script>

    </jsp:body>
</t:basicLayout>
