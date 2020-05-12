<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message var="titleTxt" code="editPetForm.title"/>

<t:basicLayout title="${titleTxt}">
    <div class="row">
        <div class=" col-md-10 offset-md-1">

            <div class="bg-light shadow p-3">
                <div class="p-2">
                    <form:form modelAttribute="editPetForm" action="${pageContext.request.contextPath}/edit-pet/${id}" method="post" enctype="multipart/form-data">
                        <h1>${titleTxt}</h1>

                        <div class="form-row p-1">

                            <div class="col">
                                <spring:bind path="petName">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.petName" var="petNameTxt"/>
                                        <form:label path="petName" for="petName">${petNameTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.petName}">
                                            <form:input placeholder="${petNameTxt}" type="text" id="petName" path="petName" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="petName" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>

                                <spring:bind path="description">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.description" var="descriptionTxt"/>
                                        <form:label path="description" for="description">${descriptionTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.description}">
                                            <form:input placeholder="${descriptionTxt}" type="text" id="description" path="description" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="description" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>
                            <div class="col">
                                <spring:bind path="location">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.location" var="locationTxt"/>
                                        <form:label path="location" for="location">${locationTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.location}">
                                            <form:input placeholder="${locationTxt}" type="text" id="location" path="location" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="location" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="price">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.price" var="priceTxt"/>
                                        <form:label path="price" for="price">${priceTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.price}">
                                            <form:input placeholder="${priceTxt}" type="number" id="price" path="price" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="price" element="div" cssClass="text-error"/>
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
                                        <div class="input-modifiable-div" data-current="${pet.species.id}">
                                            <form:select id="speciesId" path="speciesId" cssClass="input-modifiable custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="species" items="${species_list}">
                                                    <form:option value="${species.id}">${species.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="speciesId" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="breedId">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.breedName" var="breedIdTxt"/>
                                        <form:label path="breedId" for="breedId">${breedIdTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.breed.id}">
                                            <form:select id="breedId" path="breedId" cssClass="input-modifiable custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="-1"><spring:message code="uploadPetForm.emptySelect"/></form:option>
                                                <c:forEach var="breed" items="${breeds_list}">
                                                    <form:option cssClass="species-${breed.species.id}" value="${breed.id}">${breed.name}</form:option>
                                                </c:forEach>
                                            </form:select>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="breedId" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                                <spring:bind path="birthDate">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.birthDate" var="birthDateTxt"/>
                                        <form:label path="birthDate" for="birthDate">${birthDateTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.birthDate}">
                                            <form:input type="date" id="birthDate" path="birthDate" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="birthDate" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>
                            <div class="col">
                                <spring:bind path="vaccinated">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.vaccinated" var="vaccinatedTxt"/>
                                        <form:label path="vaccinated" for="vaccinated">${vaccinatedTxt}: </form:label>
                                        <c:set var="vaccinatedString">${pet.vaccinated}</c:set>
                                        <div class="input-modifiable-div" data-current="${vaccinatedString}">
                                            <form:select id="vaccinated" path="vaccinated" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="${true}"><spring:message code="boolean.Yes"/></form:option>
                                                <form:option value="${false}"><spring:message code="boolean.No"/></form:option>
                                            </form:select>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>

                                        <form:errors path="vaccinated" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>

                                <spring:bind path="gender">
                                    <div class="form-group">
                                        <spring:message code="uploadPetForm.gender" var="genderTxt"/>
                                        <form:label path="gender" for="gender">${genderTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${pet.gender}">
                                            <form:select id="gender" path="gender" cssClass="input-modifiable custom-select ${status.error ? 'is-invalid' : ''}">
                                                <form:option value="male"><spring:message code="uploadPetForm.male"/></form:option>
                                                <form:option value="female"><spring:message code="uploadPetForm.female"/></form:option>
                                            </form:select>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="gender" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>
                        </div>
                        <div class="form-row p-1">
                            <div class="col">
                                <spring:message code="editPetForm.deletePhotos"/>
                                <div>

                                    <c:forEach items="${pet.images}" var="imageId">
                                        <form:checkbox path="imagesIdToDelete" cssClass="image-checkbox" value="${imageId}" id="delete-image-checkbox-${imageId}"/>
                                        <div class="delete-image-select" id="${imageId}">
                                            <img src="<c:url value="/img/${imageId}"/>" alt="" class="pet-photo"/>
                                        </div>
                                    </c:forEach>

                                </div>
                                <form:errors path="imagesIdToDelete" element="div" cssClass="text-error"
                                             cssStyle="${status.error ? 'display: block' : ''}"/>
                            </div>
                        </div>
                        <div class="form-row p-1">
                            <div class="col">
                                <t:imageUpload/>
                            </div>
                        </div>

                        <div class="m-1 p-3 row">
                            <spring:message code="editPetForm.updatePet" var="submitText"/>
                            <input type="submit" class="btn btn-primary" value="${submitText}"/>
                        </div>
                    </form:form>

                </div>
                <div class="p-3">
                    <a href="${pageContext.request.contextPath}/"><spring:message code="backToHome"/></a>
                </div>
            </div>
        </div>
    </div>

    <script src="<c:url value="/resources/js/edit_pet_view.js"/>"></script>
</t:basicLayout>
