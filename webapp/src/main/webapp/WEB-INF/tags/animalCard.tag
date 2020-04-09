<%@tag description="Animal card" pageEncoding="UTF-8"%>
<%@attribute name="pet" required="true" type="ar.edu.itba.paw.models.Pet"%>

<div>
    <div>
        <img src="https://hips.hearstapps.com/ghk.h-cdn.co/assets/17/30/2560x1280/landscape-1500925839-golden-retriever-puppy.jpg?resize=1200:*"
             width="200" height="200"/>
    </div>
    <div>
        <p>${pet.name}</p>
        <p>${pet.birthDate}</p>
        <p>${pet.species}</p>
        <p>${pet.breed}</p>
        <p>${pet.gender}</p>
        <p>${pet.vaccinated}</p>
        <p>${pet.uploadDate}</p>
        <p>${pet.price}</p>
        <p>${pet.location}</p>

    </div>
</div>