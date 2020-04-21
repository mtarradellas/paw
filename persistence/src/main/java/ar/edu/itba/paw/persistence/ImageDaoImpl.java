package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
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

    @Override
    public Stream<Image> findByPetId(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE petId = ? ", new Object[] {id}, IMAGE_MAPPER)
                .stream();
    }
}
