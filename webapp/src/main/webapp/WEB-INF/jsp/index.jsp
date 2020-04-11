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
                                <label for="filter-specie">Especie</label>
                                <select class="form-control" id="filter-specie">
                                    <option value="any">Cualquiera</option>
                                    <option value="dog">Perro</option>
                                    <option value="cat">Gato</option>
                                </select>

                                <label for="filter-breed">Raza</label>
                                <select class="form-control disabled" id="filter-breed" disabled>
                                    <option class="specie-any" value="any">Cualquiera</option>
                                    <option class="specie-dog" value="golden">Golden</option>
                                    <option class="specie-dog" value="collie">Collie</option>
                                    <option class="specie-cat" value="siamese">Siamese</option>
                                </select>

                                <label for="filter-gender">Sexo</label>
                                <select class="form-control" id="filter-gender">
                                    <option value="any">Cualquiera</option>
                                    <option value="male">Macho</option>
                                    <option value="female">Hembra</option>
                                </select>
                            </div>
                            <h6 class="card-subtitle mb-2 text-muted">Ordenar</h6>
                            <label for="search-criteria">Criterio</label>
                            <select class="form-control" id="search-criteria">
                                <option value="any">Cualquiera</option>
                                <option value="specie">Especie</option>
                                <option value="gender">Sexo</option>
                                <option value="price">Precio</option>
                                <option value="upload-date">Fecha de subida</option>
                            </select>
                            <label for="search-order">Orden</label>
                            <select class="form-control" id="search-order" disabled>
                                <option value="asc">Ascendente</option>
                                <option value="desc">Descendente</option>
                            </select>
                        </div>
                        <div class="card-footer" id="search-tools-submit">
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

        <script src="<c:url value="/resources/js/index.js"/>"></script>

    </jsp:body>
</t:basicLayout>
