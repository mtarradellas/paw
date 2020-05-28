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
    public SpeciesList speciesList(String locale) {
        List<Species> speciesList = speciesDao.speciesList(PAGE, PAGE_SIZE);
        speciesList.forEach(s -> s.setName(locale));
        speciesList.sort(Species::compareTo);
        return new SpeciesList(speciesList);
    }

    @Override
    public SpeciesList speciesList() {
        return new SpeciesList(speciesDao.speciesList(PAGE, PAGE_SIZE));
    }

    @Override
    public BreedList breedList(String locale) {
        List<Breed> breedList = speciesDao.breedList(PAGE, PAGE_SIZE);
        breedList.forEach(b -> b.setName(locale));
        breedList.sort(Breed::compareTo);
        return new BreedList(breedList);
    }

    @Override
    public BreedList breedList() {
        return new BreedList(speciesDao.breedList(PAGE, PAGE_SIZE));
    }

    @Override
    public Optional<Species> findSpeciesByName(String locale, String name) {
        Optional<Species> opSpecies = speciesDao.findSpeciesByName(locale, name);
        opSpecies.ifPresent(species -> species.setName(locale));
        return opSpecies;
    }

    @Override
    public Optional<Breed> findBreedByName(String locale, String name) {
        Optional<Breed> opBreed = speciesDao.findBreedByName(locale, name);
        opBreed.ifPresent(breed -> breed.setName(locale));
        return opBreed;
    }

    @Override
    public Optional<Species> findSpeciesById(String locale, long id) {
        Optional<Species> opSpecies = speciesDao.findSpeciesById(id);
        opSpecies.ifPresent(species -> species.setName(locale));
        return opSpecies;
    }

    @Override
    public Optional<Breed> findBreedById(String locale, long id) {
        Optional<Breed> opBreed = speciesDao.findBreedById(id);
        opBreed.ifPresent(b -> b.setName(locale));
        return opBreed;
    }
}
