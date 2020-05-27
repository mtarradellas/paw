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
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SpeciesServiceImpl implements SpeciesService {

    static final int PAGE = 1;
    static final int PAGE_SIZE = 200;

    @Autowired
    private SpeciesDao speciesDao;


    @Override
    public SpeciesList speciesList() {
        return new SpeciesList(speciesDao.speciesList(PAGE, PAGE_SIZE));
    }

    @Override
    public BreedList breedsList() {
        return new BreedList(speciesDao.breedsList(PAGE, PAGE_SIZE));
    }

    @Override
    public Optional<Species> findSpeciesByName(String locale, String name) {
        return speciesDao.findSpeciesByName(locale, name);
    }

    @Override
    public Optional<Breed> findBreedByName(String locale, String name) {
        return speciesDao.findBreedByName(locale, name);
    }

    @Override
    public Optional<Species> findSpeciesById(long id) {
        return speciesDao.findSpeciesById(id);
    }

    @Override
    public Optional<Breed> findBreedById(long id) {
        return speciesDao.findBreedById(id);
    }
}
