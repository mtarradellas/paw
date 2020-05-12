<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message code="indexTitle" var="titleVar"/>
<t:basicLayout title="${titleVar}">
    <jsp:body>
        <div class="container-fluid">
            <div class="row">

                <t:search-tools-pet breeds_list="${breeds_list}" species_list="${species_list}"/>

                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="row">
                            <div class="col">
                                <h2><b><spring:message code="indexMessage"/></b> <spring:message code="showingResults"
                                                                                                 arguments="${pets_list_size}"/>
                                </h2>
                            </div>
                            <div class="col-md-1 align-self-end">
                                <button type="button" class="btn btn-primary btn-circle float-right "
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

                        <c:if test="${empty home_pet_list }">
                            <div class="p-3 card-color title-style"><spring:message code="noItemsFound"/>
                                <a href="${pageContext.request.contextPath}/"><spring:message code="showFirst"/></a>
                            </div>

                        </c:if>
                        <div class="card-deck row ml-5">
                            <c:forEach var="pet" items="${home_pet_list}">
                                <c:if test="${pet.status.id eq 1}">

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

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
