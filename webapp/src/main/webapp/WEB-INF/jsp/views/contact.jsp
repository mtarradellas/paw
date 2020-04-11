<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout title="Contact">
    <jsp:body>

        <div class="shadow p-3 mb-5 rounded card-color">
            <h1 class="title-style">Como contactarse con nosotros:</h1>
        </div>
        <h6 class="title-style"><b>Para poder contactarse con nosotros debera tener una direccion de email valido y hacer lo siguiente:</b></h6>
        <div class="row mt-1">
            <div class="col-md-12">
                <div class="card card-color">
                    <div class="card-body">
                        1. Abra su correo electronico (ATENCION! posibles interesados en la mascora se contactaran a esta direccion)
                    </div>
                </div>
                <div class="card card-color">
                    <div class="card-body">
                        2. Envie un mail a petsociety.contact@gmail.com con el siguiente contenido:
                        <ul>
                            <li>Nombre (mascota)</li>
                            <li>Especie (Perro o gato)</li>
                            <li>Raza(si es callejero o no sabe, aclarelo)</li>
                            <li>Edad (mascota)</li>
                            <li>Si esta en adopcion o a la venta (si ese es el caso el precio)</li>
                            <li>Vacunado/No Vacunado</li>
                            <li>Foto/s de la mascota</li>
                            <li>Nombre del dueño</li>
                            <li>Ubicacion del animal (Ciudad dentro de la Republica Argentina)</li>
                        </ul>
                    </div>
                </div>
                <div class="card card-color">
                    <div class="card-body">
                        3. Listo! En cuanto podamos subiremos su mascota al sitio y aparecera en el inicio!
                    </div>
                </div>
            </div>
        </div>
        <div class="p-4">
            <a href="${pageContext.request.contextPath}/">Volver al inicio</a>
        </div>
    </jsp:body>
</t:basicLayout>