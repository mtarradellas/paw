<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message code="addPet" var="addPetTitle"/>
<t:adminLayout title="${addPetTitle}" item="pets">
    <jsp:body>
        <div class="row">
            <div class=" col-md-10 offset-md-1">

                <div class="bg-light shadow p-3">
                    <div class="p-2">
                        <form:form modelAttribute="adminUploadPetForm" action="${pageContext.request.contextPath}/admin/upload-pet" method="post" enctype="multipart/form-data">
                            <h1>${addPetTitle}</h1>

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
                                    <spring:bind path="price">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.price" var="priceTxt"/>
                                            <form:label path="price" for="price">${priceTxt}: </form:label>
                                            <form:input placeholder="${priceTxt}" type="number" id="price" path="price" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <form:errors path="price" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>
                                </div>
                            </div>
                            <div class="form-row p-1">
                                <div class="col">
                                    <spring:bind path="province">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.province" var="provinceTxt"/>

                                            <form:label path="province" for="province">${provinceTxt}: </form:label>
                                            <form:select id="province" data-child="department" path="province" cssClass="selector-parent custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="province" items="${province_list}">
                                                    <form:option value="${province.id}">${province.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <form:errors path="province" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>
                                </div>
                                <div class="col">
                                    <spring:bind path="department">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.department" var="departmentTxt"/>

                                            <form:label path="department" for="department">${departmentTxt}: </form:label>
                                            <form:select id="department" path="department" cssClass="custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="department" items="${department_list}">
                                                    <form:option data-dependency="${department.province.id}" value="${department.id}">${department.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <form:errors path="department" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>
                                </div>
                            </div>
                            <div class="form-row p-1">
                                <div class="col">
                                    <spring:bind path="speciesId">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.speciesName" var="speciesIdTxt"/>
                                            <form:label path="speciesId" for="speciesId">${speciesIdTxt}: </form:label>
                                            <form:select id="speciesId" data-child="breedId" path="speciesId" cssClass="selector-parent custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="species" items="${species_list}">
                                                    <form:option value="${species.id}">${species.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <form:errors path="speciesId" element="div" cssClass="invalid-feedback"/>
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
                                    <spring:bind path="owner">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.owner" var="ownerText"/>
                                            <form:label path="owner" for="owner">${ownerText}: </form:label>
                                            <form:select id="owner" path="owner" cssClass="form-control ${status.error ? 'is-invalid' : ''}">
                                                <form:options items="${users_list}" itemValue="id" itemLabel="username" />
                                            </form:select>
                                            <form:errors path="owner" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>
                                </div>
                                <div class="col">

                                    <spring:bind path="breedId">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.breedName" var="breedIdTxt"/>
                                            <form:label path="breedId" for="breedId">${breedIdTxt}: </form:label>
                                            <form:select id="breedId" path="breedId" cssClass="custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="breed" items="${breeds_list}">
                                                    <form:option data-dependency="${breed.species.id}" value="${breed.id}">${breed.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <form:errors path="breedId" element="div" cssClass="invalid-feedback"/>
                                        </div>
                                    </spring:bind>
                                    <spring:bind path="vaccinated">
                                        <div class="form-group">
                                            <spring:message code="uploadPetForm.vaccinated" var="vaccinatedTxt"/>
                                            <form:label path="vaccinated" for="vaccinated">${vaccinatedTxt}: </form:label>
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
                                            <form:label path="gender" for="gender">${genderTxt}: </form:label>
                                            <form:select id="gender" path="gender" cssClass="custom-select ${status.error ? 'is-invalid' : ''}">
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
                                    <t:imageUpload/>
                                </div>
                            </div>

                            <div class="m-1 p-3 row">
                                <spring:message code="uploadPetForm.submit" var="submitText"/>
                                <input type="submit" class="btn btn-primary" value="${submitText}"/>
                            </div>
                        </form:form>

                        <div class="p-3">
                            <a href="${pageContext.request.contextPath}/admin/pets"><spring:message code="goBackLists"/></a>
                        </div>
                    </div>

                </div>
            </div>
        </div>
        <script src="<c:url value="/resources/js/selector_dependency.js"/>"></script>
    </jsp:body>
</t:adminLayout>