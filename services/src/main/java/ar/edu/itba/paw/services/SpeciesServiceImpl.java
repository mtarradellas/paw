package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.SpeciesDao;
import ar.edu.itba.paw.interfaces.SpeciesService;
import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Species;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SpeciesServiceImpl implements SpeciesService {

    static final int PAGE = 1;
    static final int PAGE_SIZE = 200;

    @Autowired
    private SpeciesDao speciesDao;


    @Override
    public List<Species> speciesList(String locale) {
        List<Species> speciesList = speciesDao.speciesList(PAGE, PAGE_SIZE);
        setSpeciesLocale(locale, speciesList);
        return speciesList;
    }

    @Override
    public List<Species> speciesList() {
        return speciesDao.speciesList(PAGE, PAGE_SIZE);
    }

    @Override
    public List<Breed> breedList(String locale) {
        List<Breed> breedList = speciesDao.breedList(PAGE, PAGE_SIZE);
        setBreedLocale(locale, breedList);
        return breedList;
    }

    @Override
    public List<Breed> breedList() {
        return speciesDao.breedList(PAGE, PAGE_SIZE);
    }

    @Override
    public Optional<Species> findSpeciesByName(String locale, String name) {
        Optional<Species> opSpecies = speciesDao.findSpeciesByName(locale, name);
        opSpecies.ifPresent(s -> setSpeciesLocale(locale, s));
        return opSpecies;
    }

    @Override
    public Optional<Breed> findBreedByName(String locale, String name) {
        Optional<Breed> opBreed = speciesDao.findBreedByName(locale, name);
        opBreed.ifPresent(b -> setBreedLocale(locale, b));
        return opBreed;
    }

    @Override
    public Optional<Species> findSpeciesById(String locale, long id) {
        Optional<Species> opSpecies = speciesDao.findSpeciesById(id);
        opSpecies.ifPresent(s -> setSpeciesLocale(locale, s));
        return opSpecies;
    }

    @Override
    public Optional<Breed> findBreedById(String locale, long id) {
        Optional<Breed> opBreed = speciesDao.findBreedById(id);
        opBreed.ifPresent(b -> setBreedLocale(locale, b));
        return opBreed;
    }

    @Override
    public Optional<Species> findSpeciesById(long id) {
        return speciesDao.findSpeciesById(id);
    }

    @Override
    public Optional<Breed> findBreedById(long id) {
        return speciesDao.findBreedById(id);
    }

    @Override
    public void setSpeciesLocale(String locale, Species species) {
        species.setLocale(locale);
    }

    @Override
    public void setBreedLocale(String locale, Breed breed) {
        breed.setLocale(locale);
    }

    @Override
    public void setSpeciesLocale(String locale, List<Species> speciesList) {
        speciesList.forEach(s -> {
                    s.setLocale(locale);
                    setBreedLocale(locale, s.getBreedList());
                }
        );
        speciesList.sort(Species::compareTo);
    }

    @Override
    public void setBreedLocale(String locale, List<Breed> breedList) {
        breedList.forEach(b -> b.setLocale(locale));
        breedList.sort(Breed::compareTo);
    }
}
