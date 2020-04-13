package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.ImageDao;
import ar.edu.itba.paw.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Optional;

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
                .usingGeneratedKeyColumns("images_id");
    }
    private static final RowMapper<Image> IMAGE_MAPPER = (rs, rowNum) -> new Image(
            rs.getInt("image_id"),
            rs.getBytes("img")
    );

    @Override
    public Optional<Image> findById(Integer id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE image_id = 1", IMAGE_MAPPER)
                .stream().findFirst();
    }
}
