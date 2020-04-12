<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Image carousel" pageEncoding="UTF-8"%>
<%@attribute name="ids" required="true" type="java.lang.String[]"%>

<div id="imageCarousel" class="carousel slide" data-ride="carousel">
    <ol class="carousel-indicators">
        <li data-target="#imageCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#imageCarousel" data-slide-to="1"></li>
        <li data-target="#imageCarousel" data-slide-to="2"></li>
    </ol>
    <div class="carousel-inner">
        <c:forEach items="${ids}" varStatus="varStatus">
            <c:choose>
                <c:when test="${varStatus.index == 0}">
                    <div class="carousel-item active">
                </c:when>
                <c:otherwise>
                    <div class="carousel-item">
                </c:otherwise>
            </c:choose>
                <img class="d-block w-100" src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
                     alt="${ids[varStatus.index]}"
                >
            </div>
        </c:forEach>

    </div>
    <a class="carousel-control-prev" href="#imageCarousel" role="button" data-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="carousel-control-next" href="#imageCarousel" role="button" data-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>