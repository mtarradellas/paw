<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>


<!doctype html>
<html lang="en">
<head>
        <script src="<c:url value="/resources/open-layers-6.3.1/build/ol.js"/>"></script>
        <link rel="stylesheet" href="<c:url value="/resources/open-layers-6.3.1/css/ol.css"/>" type="text/css">
    <link rel="stylesheet" href="<c:url value="/resources/css/styles.css"/>"/>
    <title>Maps</title>
</head>
<body>
<h2>Where are you?</h2>
<div id="map" class="map"></div>
<script src="<c:url value="/resources/js/maps.js"/>"></script>
<form method="post">
    <input id="country" name="country" type="text" placeholder="Country goes here" aria-label="Country">
    <input id="state" name="state" type="text" placeholder="State goes here" aria-label="State">
    <input id="town-city" name="town-city" type="text" placeholder="Town-city goes here" aria-label="town-city">
    <input id="suburb" name="suburb" type="text" placeholder="Suburb goes here" aria-label="Suburb">
    <input id="road" name="road" type="text" placeholder="Road goes here" aria-label="Road">
</form>
</body>
</html>