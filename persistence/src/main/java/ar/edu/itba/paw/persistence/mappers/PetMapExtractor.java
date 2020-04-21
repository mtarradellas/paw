package ar.edu.itba.paw.persistence.mappers;

import ar.edu.itba.paw.models.Breed;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pet;
import ar.edu.itba.paw.models.Species;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetMapExtractor implements ResultSetExtractor<Map<Pet, List<Image>>> {
    @Override
    public Map<Pet, List<Image>> extractData(ResultSet rs) throws SQLException {
        Map<Pet, List<Image>> imageMap = new HashMap<>();
        while (rs.next()) {
            Pet pet = new Pet(
                    rs.getLong("id"),
                    rs.getString("petName"),
                    new Species(rs.getLong("speciesId"), rs.getString("speciesName")),
                    new Breed(rs.getLong("breedId"), rs.getLong("breedSpeciesId"), rs.getString("breedName")),
                    rs.getString("location"),
                    rs.getBoolean("vaccinated"),
                    rs.getString("gender"),
                    rs.getString("description"),
                    rs.getDate("birthDate"),
                    rs.getDate("uploadDate"),
                    rs.getInt("price"),
                    rs.getLong("ownerId")
            );
            Image image = new Image(
                    rs.getInt("id"),
                    rs.getBytes("img"),
                    rs.getInt("petId")
            );
            List<Image> images = imageMap.get(pet);
            if (images == null) {
                List<Image> newImages = new ArrayList<>();
                newImages.add(image);
                imageMap.put(pet, newImages);
            } else {
                images.add(image);
            }
        }
        return imageMap;
    }
}
