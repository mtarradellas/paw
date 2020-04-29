<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message var="titleTxt" code="uploadPetForm.title"/>

<t:basicLayout title="${titleTxt}">
    <div class="row ">
        <div class=" col-md-10 offset-md-1">

            <div class="bg-light shadow p-3">
                <div class="p-2">
                    <form:form modelAttribute="uploadPetForm" action="${pageContext.request.contextPath}/register" method="post" enctype="application/x-www-form-urlencoded">
                        <h1>${titleTxt}</h1>

                        <div class="form-row p-1">

                            <div class="col">
                                <spring:bind path="petName">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.petName" var="petNameTxt"/>
                                        <form:label path="petName" for="petName">${petNameTxt}: </form:label>
                                        <form:input placeholder="${petNameTxt}" type="text" id="petName" path="petName" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="petName" element="div" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>

                                <spring:bind path="description">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.description" var="descriptionTxt"/>
                                        <form:label path="description" for="description">${descriptionTxt}: </form:label>
                                        <form:input placeholder="${descriptionTxt}" type="text" id="description" path="description" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="description" element="div" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>
                            </div>
                            <div class="col">
                                <spring:bind path="location">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.location" var="locationTxt"/>
                                        <form:label path="location" for="location">${locationTxt}: </form:label>
                                        <form:input placeholder="${locationTxt}" type="text" id="location" path="location" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="location" element="div" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="price">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.price" var="priceTxt"/>
                                        <form:label path="price" for="price">${priceTxt}: </form:label>
                                        <form:input placeholder="${priceTxt}" type="text" id="price" path="price" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="price" element="div" cssClass="invalid-feedback"/>
                                    </div>
                                </spring:bind>
                            </div>
                    </div>
                    <div class="form-row p-1">
                        <div class="col">
                            <spring:bind path="speciesName">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.speciesName" var="speciesNameTxt"/>
                                    <form:label path="speciesName" for="speciesName">${speciesNameTxt}: </form:label>
                                    <form:select id="speciesName" path="speciesName" cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                        <form:option value="empty"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                        <c:forEach var="species" items="${species_list}">
                                            <form:option value="${species.id}">${species.name}</form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="speciesName" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>
                            <spring:bind path="breedName">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.breedName" var="breedNameTxt"/>
                                    <form:label path="breedName" for="petName">${breedNameTxt}: </form:label>
                                    <form:select id="breedName" path="breedName" cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                        <form:option value="empty"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                        <c:forEach var="breed" items="${breeds_list}">
                                            <form:option cssClass="species-${breed.speciesId}" value="${breed.id}">${breed.name}</form:option>
                                        </c:forEach>
                                    </form:select>
                                    <form:errors path="breedName" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>
                            <spring:bind path="birthDate">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.birthDate" var="birthDateTxt"/>
                                    <form:label path="birthDate" for="birthDate">${birthDateTxt}: </form:label>
                                    <form:input type="date" id="birthDate" path="birthDate" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                    <form:errors path="birthDate" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>
                        </div>
                        <div class="col">
                            <spring:bind path="vaccinated">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.vaccinated" var="vaccinatedTxt"/>
                                    <form:label path="vaccinated" for="petName">${vaccinatedTxt}: </form:label>
                                    <form:select id="vaccinated" path="vaccinated" cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                        <form:option value="${true}"><spring:message code="boolean.Yes"/></form:option>
                                        <form:option value="${false}"><spring:message code="boolean.No"/></form:option>
                                    </form:select>
                                    <form:errors path="vaccinated" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>

                            <spring:bind path="gender">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.gender" var="genderTxt"/>
                                    <form:label path="gender" for="petName">${genderTxt}: </form:label>
                                    <form:select id="gender" path="gender" cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                        <form:option value="male"><spring:message code="uploadPetForm.male"/></form:option>
                                        <form:option value="female"><spring:message code="uploadPetForm.female"/></form:option>
                                    </form:select>
                                    <form:errors path="gender" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>
                        </div>
                    </div>
                    <div class="form-row p-1">
                        <div class="col">
                            <spring:bind path="photo">
                                <div class="form-group">
                                    <spring:message code="uploadPetForm.photo" var="photoTxt"/>
                                    <spring:message code="uploadPetForm.uploadPhoto" var="uploadPhotoTxt"/>
                                    <form:label path="photo" for="photo">${photoTxt}: </form:label>
                                    <form:input accept="image/*" type="file" id="photo" path="photo" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                    <form:errors path="photo" element="div" cssClass="invalid-feedback"/>
                                </div>
                            </spring:bind>
                        </div>
                    </div>

                    <div class="p-2 row">
                        <spring:message code="uploadPetForm.submit" var="submitText"/>
                        <input type="submit" class="btn btn-primary" value="${submitText}"/>
                    </div>
                    </form:form>

                </div>

            </div>
        </div>
    </div>

    <script src="<c:url value="/resources/js/upload_pet.js"/>"></script>
</t:basicLayout>
