package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
public class SpeciesServiceImpl implements SpeciesService {

    @Autowired
    private SpeciesDao speciesDao;


    @Override
    public Stream<Species> speciesList(String language) {
        return speciesDao.speciesList(language);
    }

    @Override
    public Stream<Breed> breedsList(String language) {
        return speciesDao.breedsList(language);
    }
}
