package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.BreedList;
import ar.edu.itba.paw.models.Species;
import ar.edu.itba.paw.models.SpeciesList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class SpeciesServiceImpl implements SpeciesService {

    @Autowired
    private SpeciesDao speciesDao;


    @Override
    public SpeciesList speciesList(String locale) {
        return new SpeciesList(speciesDao.speciesList(locale));

    }

    @Override
    public BreedList breedsList(String locale) {
        return new BreedList(speciesDao.breedsList(locale));
    }
}
