<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<div class="card animal-list-card" style="width: 18rem;">
    <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
         class="card-img-top" alt="" width="200" height="200">
    <div class="card-body">
        <p class="card-text">
            Nombre: ${pet.name}<br>
            Raza: ${pet.breed}<br>
            Precio: $${pet.price}
        </p>
    </div>
    <div class="card-body">
        <a href="#" class="card-link">Ir a la página</a>
    </div>
</div>