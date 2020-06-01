<%@tag description="Image carousel" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="images" required="true" type="java.util.List<ar.edu.itba.paw.models.ImageDTO>"%>

<div>
    <c:forEach items="${images}" varStatus="status" var="image">
        <a href="#" class="pet-photo-link">
            <img src="${pageContext.request.contextPath}/img/${image.id}" alt="Photo ${status.index}" class="img-thumbnail pet-photo">
        </a>
    </c:forEach>
</div>
