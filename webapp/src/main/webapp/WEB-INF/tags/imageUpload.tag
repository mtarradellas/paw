<%@tag description="Image upload component" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<div class="image-upload">
    <spring:bind path="photos">
        <spring:message code="uploadPetForm.uploadPhoto" var="uploadPhotoTxt"/>
        <form:label path="photos">${uploadPhotoTxt}:</form:label>
        <div class="m-1">
            <form:input multiple="multiple" path="photos" accept="image/*" type="file"
                        class="photos-input ${status.error ? 'is-invalid' : ''}"
            />
        </div>

        <div class="image-preview-container">

        </div>
        <form:errors path="photos" element="div" cssClass="invalid-feedback"
                     cssStyle="${status.error ? 'display: block' : ''}"/>
    </spring:bind>

    <script src="<c:url value="/resources/js/image_upload.js"/>"></script>

</div>