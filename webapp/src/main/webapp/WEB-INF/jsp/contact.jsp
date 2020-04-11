<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:basicLayout>
    <jsp:body>
        <div class="shadow p-3 mb-5 rounded card-color">
            <h1 class="title-style"><spring:message code="contact.contactUs"/></h1>
        </div>
        <h6 class="title-style"><b><spring:message code="contact.subtitle"/></b></h6>
        <div class="row mt-1">
            <div class="col-md-12">
                <div class="card card-color">
                    <div class="card-body">
                        <spring:message code="contact.step1"/>
                    </div>
                </div>
                <div class="card card-color">
                    <div class="card-body">
                        <spring:message code="contact.step2"/>
                        <ul>
                            <li><spring:message code="contact.name"/></li>
                            <li><spring:message code="contact.species"/></li>
                            <li><spring:message code="contact.breed"/></li>
                            <li><spring:message code="contact.age"/></li>
                            <li><spring:message code="contact.sex"/></li>
                            <li><spring:message code="contact.status"/></li>
                            <li><spring:message code="contact.vaccined"/></li>
                            <li><spring:message code="contact.photo"/></li>
                            <li><spring:message code="contact.location"/></li>
                            <li><spring:message code="contact.ownerName"/></li>
                        </ul>
                    </div>
                </div>
                <div class="card card-color">
                    <div class="card-body">
                        <spring:message code="contact.step3"/>
                    </div>
                </div>
            </div>
        </div>
        </div>

    </jsp:body>
</t:basicLayout>