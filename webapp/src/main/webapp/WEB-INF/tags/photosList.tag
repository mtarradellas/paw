<%@tag description="Image carousel" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="ids" required="true" type="java.lang.String[]"%>

<div>
    <c:forEach items="${ids}" varStatus="status" var="id">
        <a href="#" class="pet-photo-link">
            <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg"
                         alt="Photo ${status.index}" class="img-thumbnail pet-photo">
        </a>
    </c:forEach>
</div>
