<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:basicLayout title="LENIA TEST">
    <jsp:body>
        <div class="row">
            <div class="col-md-10 offset-md-1">
                <div class="shadow p-3 bg-white rounded">
                    <div class="shadow p-3 mb-5 rounded card-color">
                        <h1 class="title-style">~TEST PAGE~</h1>
                    </div>
                    <div class="row mt-1">
                        <div class="col-md-12">
                            <div class="card">
                                    <%--                            TEST                --%>

<%--                                <h1><c:out value="${amount}"/></h1>--%>

                                <c:forEach items="${filteredRe}" var="request">
                                    <div class="card-body">
                                            ${request.id}
                                    </div>
                                    <div class="card-body">
                                            ${request.target}
                                    </div>
                                    <div class="card-body">
                                            ${request.user}
                                    </div>
                                    <div class="card-body">
                                            ${request.pet}
                                    </div>
                                </c:forEach>

                            </div>
                        </div>
                    </div>
                    <div class="p-4">
                        <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:basicLayout>