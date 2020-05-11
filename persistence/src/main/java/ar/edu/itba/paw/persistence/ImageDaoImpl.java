package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Stream;

@Repository
public class ImageDaoImpl implements ImageDao {
    private static final String IMAGES_TABLE = "images";

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public ImageDaoImpl(final DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(IMAGES_TABLE)
                .usingGeneratedKeyColumns("id");
    }
    private static final RowMapper<Image> IMAGE_MAPPER = (rs, rowNum) -> new Image(
            rs.getInt("id"),
            rs.getBytes("img"),
            rs.getInt("petId")
    );
    private static final RowMapper<byte[]> IMAGE_DATA_MAPPER = (rs, rowNum) -> rs.getBytes("img");

    @Override
    public Stream<Image> findByPetId(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE petId = ? ", new Object[] {id}, IMAGE_MAPPER)
                .stream();
    }

    @Override
    public Integer quantityByPetId(long id) {
        return jdbcTemplate.queryForObject("SELECT count(*) FROM images WHERE petId = ? ", new Object[] {id}, Integer.class);
    }

    @Override
    public Optional<byte[]> getDataById(long id) {
        return jdbcTemplate.query("SELECT img FROM images WHERE id = ? ", new Object[] {id}, IMAGE_DATA_MAPPER)
                .stream().findFirst();
    }

    @Override
    public Optional<Image> create(long petId, byte[] bytes) {
        final Map<String, Object> values = new HashMap<>();
        values.put("img", bytes);
        values.put("petId", petId);
        Number key;

        key = jdbcInsert.executeAndReturnKey(values);
        return Optional.of(new Image(key.intValue(), bytes, petId));
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM images WHERE id = ? ", new Object[]{id}) ;
    }

    @Override
    public void delete(List<Integer> ids) {
        List<String> images = new ArrayList<>();
        for (Integer id:ids) {
            images.add(id.toString());
        }
        String imagesToDelete = String.join(",", images);
        jdbcTemplate.update("DELETE FROM images WHERE id IN (" + imagesToDelete +") ") ;
    }

}
