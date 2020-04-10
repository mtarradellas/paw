<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:basicLayout>
    <jsp:body>
        <div class="container-fluid home-container">
            <div class="row">

                <div class="col-md-2 search-tools">
                    <div class="card shadow p-3">
                        <div class="card-header">
                            <h5 class="card-title">Opciones de busqueda</h5>
                        </div>
                        <div class="card-body">
                            <h6 class="card-subtitle mb-2 text-muted">Filtrar</h6>
                            <div class="form-group">
                                <label for="filtrar-especie">Especie</label>
                                <select class="form-control" id="filtrar-especie">
                                    <option>Cualquiera</option>
                                    <option>Perro</option>
                                    <option>Gato</option>
                                    <option>Mono</option>
                                </select>

                                <label for="filtrar-raza">Raza</label>
                                <select class="form-control" id="filtrar-raza">
                                    <option>Cualquiera</option>
                                    <option>Golden</option>
                                    <option>Collie</option>
                                    <option>Caniche</option>
                                </select>

                                <label for="filtrar-sexo">Sexo</label>
                                <select class="form-control" id="filtrar-sexo">
                                    <option>Cualquiera</option>
                                    <option>Macho</option>
                                    <option>Hembra</option>
                                </select>
                            </div>
                            <h6 class="card-subtitle mb-2 text-muted">Ordenar</h6>
                            <label for="search-criteria">Criterio</label>
                            <select class="form-control" id="search-criteria">
                                <option>Cualquiera</option>
                                <option>Especie</option>
                                <option>Sexo</option>
                                <option>Precio</option>
                                <option>Fecha de subida</option>
                            </select>
                            <label for="search-order">Orden</label>
                            <select class="form-control" id="search-order">
                                <option>Ascendente</option>
                                <option>Descendente</option>
                            </select>
                        </div>
                        <div class="card-footer">
                            <button type="button" class="btn btn-primary">Buscar</button>
                        </div>
                    </div>
                </div>
                <div class="col">
                    <div class="shadow p-3 bg-white rounded">
                        <div class="card-deck row">

                            <c:forEach var="pet" items="${home_pet_list}">
                                <div class="col-auto mb-3">

                                    <t:animalCard pet="${pet}"/>

                                </div>
                            </c:forEach>

                        </div>
                    </div>
                </div>


            </div>

        </div>

    </jsp:body>
</t:basicLayout>