<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="are-you-sure" pageEncoding="UTF-8"%>
<%@attribute name="title" required="true" type="java.lang.String"%>
<%@attribute name="body" required="true" type="java.lang.String"%>


<div id="are-you-sure-modal" class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">${title}</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p>${body}</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-danger"><spring:message code="areYouSure.accept"/></button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="areYouSure.decline"/></button>
            </div>
        </div>
    </div>
</div>