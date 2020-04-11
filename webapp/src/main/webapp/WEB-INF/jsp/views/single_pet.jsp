<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout>
    <div class="modal fade" id="image-modal" tabindex="-1" role="dialog" aria-labelledby="full-image" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <img src="" alt="Full sized image"/>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid page-content">
        <div class="row justify-content-center">

            <div class="col-10">
                <div class="p-4">
                    <h1>Informacion sobre la mascota</h1>
                </div>
                <div class="shadow p-3">
                    <div class="p-2">
                        <h2>Fotos</h2>
                        <t:photosList ids="${ids}"/>
                    </div>
                    <div class="p-2">
                        <h2>Datos</h2>
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
                <div class="p-4">
                    <a href="${pageContext.request.contextPath}/">Volver al inicio</a>
                </div>
            </div>
        </div>
    </div>
    <script src="<c:url value="/resources/js/pet_view.js"/>"></script>
</t:basicLayout>