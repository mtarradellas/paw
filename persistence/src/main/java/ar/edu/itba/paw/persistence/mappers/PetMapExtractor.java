package ar.edu.itba.paw.persistence.mappers;

import ar.edu.itba.paw.models.*;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PetMapExtractor implements ResultSetExtractor<Map<Pet, List<Long>>> {
    @Override
    public Map<Pet, List<Long>> extractData(ResultSet rs) throws SQLException {
        Map<Pet, List<Long>> imageIdMap = new LinkedHashMap<>();
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
                    rs.getLong("ownerId"),
                    new Status(rs.getInt("statusId"), rs.getString("statusName"))
            );
            Long imageId = new Long(
                    rs.getLong("imagesId")
            );
            List<Long> images = imageIdMap.get(pet);
            if (images == null) {
                List<Long> newImages = new ArrayList<>();
                newImages.add(imageId);
                imageIdMap.put(pet, newImages);
            } else {
                images.add(imageId);
            }
        }
        return imageIdMap;
    }
}
