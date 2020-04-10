<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<div class="card animal-list-card">
    <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
         class="card-img-top" alt="">
    <div class="card-body">
        <p class="card-text">
            Nombre: <c:out value="${pet.name}"/><br>
            Raza: <c:out value="${pet.breed}"/><br>
            Precio: $<c:out value="${pet.price}"/>
        </p>
    </div>
    <div class="card-body">
        <a href="#" class="card-link">Ir a la página</a>
    </div>
</div>