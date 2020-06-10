<%@tag description="Image carousel" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="images" required="true" type="java.util.List<ar.edu.itba.paw.models.ImageDTO>"%>

<div class="pet-image-preview-section">
    <c:forEach items="${images}" varStatus="status" var="image">
        <a href="#" class="pet-photo-link pet-image-preview">
            <img src="${pageContext.request.contextPath}/img/${image.id}" alt="Photo ${status.index}"
                 class="img-thumbnail">
        </a>
    </c:forEach>
</div>
