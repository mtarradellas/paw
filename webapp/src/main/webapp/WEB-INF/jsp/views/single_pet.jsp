<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout>
    <div class="container-fluid page-content">
        <div class="row justify-content-center">
            <div class="col-10 shadow p-3">
                <t:imageCarousel ids="${ids}"/>
                <h2>Datos de la mascota</h2>
                <ul class="list-group">
                    <li class="list-group-item">Nombre: ${pet.petName}</li>
                    <li class="list-group-item">Fecha de nacimiento: ${pet.birthDate}</li>
                    <li class="list-group-item">Especie: ${pet.species}</li>
                    <li class="list-group-item">Raza: ${pet.breed}</li>
                    <li class="list-group-item">Sexo: ${pet.gender}</li>
                    <li class="list-group-item">Vacunado: ${pet.vaccinated}</li>
                    <li class="list-group-item">Precio: ${pet.price}</li>
                    <li class="list-group-item">Ubicacion: ${pet.location}</li>
                    <li class="list-group-item">Descripcion: ${pet.description}</li>
                </ul>
            </div>
        </div>
    </div>
</t:basicLayout>