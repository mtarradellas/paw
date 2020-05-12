<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>


<spring:message var="titleTxt" code="editUserForm.title"/>

<t:adminLayout title="${titleTxt}" item="users">
    <div class="row">
        <div class=" col-md-10 offset-md-1">

            <div class="bg-light shadow p-3">
                <div class="p-2">
                    <h1>${titleTxt}</h1>
                    <form:form modelAttribute="editUserForm" action="${pageContext.request.contextPath}/admin/user/${id}/edit" method="post" enctype="multipart/form-data">

                        <h3><spring:message code="editUserForm.basicForm"/></h3>
                        <div class="form-row p-1">
                            <div class="col">
                                <spring:bind path="username">
                                    <div class="form-group">
                                        <spring:message code="editUserForm.username" var="usernameTxt"/>
                                        <form:label path="username" for="username">${usernameTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${user.username}">
                                            <form:input placeholder="${usernameTxt}" type="text" id="petName" path="username" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <c:if test="${duplicatedUsername eq true}">
                                            <p class="text-error"><spring:message code="duplicatedUsername"/> </p>
                                        </c:if>
                                        <form:errors path="username" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>
                            <div class="col">
                                <spring:bind path="phone">
                                    <div class="form-group">
                                        <spring:message code="editUserForm.phone" var="phoneTxt"/>
                                        <form:label path="phone" for="phone">${phoneTxt}: </form:label>
                                        <div class="input-modifiable-div" data-current="${user.phone}">
                                            <form:input placeholder="${phoneTxt}" type="number" id="phone" path="phone" cssClass="input-modifiable form-control ${status.error ? 'is-invalid' : ''}"/>
                                            <a class="revert-input-anchor">
                                                <svg class="bi bi-arrow-counterclockwise" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">
                                                    <path fill-rule="evenodd" d="M12.83 6.706a5 5 0 00-7.103-3.16.5.5 0 11-.454-.892A6 6 0 112.545 5.5a.5.5 0 11.91.417 5 5 0 109.375.789z" clip-rule="evenodd"/>
                                                    <path fill-rule="evenodd" d="M7.854.146a.5.5 0 00-.708 0l-2.5 2.5a.5.5 0 000 .708l2.5 2.5a.5.5 0 10.708-.708L5.707 3 7.854.854a.5.5 0 000-.708z" clip-rule="evenodd"/>
                                                </svg>
                                            </a>
                                        </div>
                                        <form:errors path="phone" element="div" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>


                        </div>

                        <div class="p-1">
                            <spring:message code="editUserForm.updateUser" var="submitText"/>
                            <input type="submit" class="btn btn-primary" value="${submitText}" name="update-basic-info"/>
                        </div>

                        <hr>

                    </form:form>
                    <form:form modelAttribute="editUserForm" action="${pageContext.request.contextPath}/admin/user/${id}/edit" method="post" enctype="multipart/form-data">

                        <h3><spring:message code="editUserForm.passwordForm"/></h3>

                        <div class="form-row p-1">
                            <div class="col">
                                <spring:bind path="currentPassword">
                                    <div class="form-group">
                                        <spring:message code="editUserForm.currentPassword" var="passwordTxt"/>
                                        <form:label path="currentPassword" for="currentPassword">${passwordTxt}: </form:label>
                                        <form:input type="password" placeholder="${passwordTxt}" id="currentPassword" path="currentPassword" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="currentPassword" element="div" cssClass="invalid-feedback"/>
                                        <c:if test="${not empty current_password_fail}">
                                            <p class="text-error"><spring:message code="editUserForm.passwordIsIncorrect"/></p>
                                        </c:if>
                                    </div>
                                </spring:bind>
                            </div>
                        </div>
                        <div class="form-row p-1">

                            <div class="col">
                                <spring:bind path="newPassword">
                                    <div class="form-group">
                                        <spring:message code="editUserForm.newPassword" var="passwordTxt"/>
                                        <form:label path="newPassword" for="password">${passwordTxt}: </form:label>
                                        <form:input type="password" placeholder="${passwordTxt}" id="password" path="newPassword" cssClass="form-control ${status.error ? 'is-invalid' : ''}"/>
                                        <form:errors path="newPassword" element="div" cssClass="invalid-feedback"/>
                                        <form:errors element="p" cssClass="text-error"/>
                                    </div>
                                </spring:bind>
                            </div>

                            <div class="col">
                                <spring:bind path="*">
                                    <div class="form-group">
                                        <spring:message code="editUserForm.repeatNewPassword" var="repeatPasswordTxt"/>
                                        <form:label path="repeatNewPassword" for="repeatPassword">${repeatPasswordTxt}: </form:label>
                                        <form:input placeholder="${repeatPasswordTxt}" type="password" id="repeatPassword" cssClass="form-control ${status.error || (not empty classError) ? 'is-invalid' : ''}"
                                                    path="repeatNewPassword"/>
                                    </div>
                                </spring:bind>
                            </div>
                        </div>

                        <div class="p-1">
                            <spring:message code="editUserForm.updatePassword" var="submitText"/>
                            <input type="submit" class="btn btn-primary" value="${submitText}" name="update-password"/>
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
</t:adminLayout>
