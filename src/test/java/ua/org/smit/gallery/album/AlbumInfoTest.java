package ua.org.smit.gallery.album;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeAll;
import ua.org.smit.gallery.hibarnate.AlbumInfoDAO;
import ua.org.smit.gallery.hibarnate.HibernateUtil;

public class AlbumInfoTest {

    private static final Logger log = LogManager.getLogger(AlbumInfoTest.class);

    @BeforeAll
    public static void initHibernate() {
        HibernateUtil.buildSessionFactory();
    }

    @Test
    public void writeMetaDataTest() {
        AlbumInfoDAO dao = new AlbumInfoDAO(AlbumInfo.class);

        AlbumInfo metaData = new AlbumInfo();
        metaData.setTitle("Test class");

        dao.create(metaData);

        assertFalse(dao.findAll().isEmpty());
    }

    @Test
    public void foundByAlias() {
        AlbumInfoDAO dao = new AlbumInfoDAO(AlbumInfo.class);

        String alias = "test_alias";
        AlbumInfo metaData = new AlbumInfo();
        metaData.setTitle("Test class");
        metaData.setAlias(alias);

        dao.create(metaData);

        Optional<AlbumInfo> result = dao.findByAlias(alias);
        assertEquals(alias, result.get().getAlias());
    }

    @AfterAll
    public static void closeHibernateSession() {
        HibernateUtil.close();
    }

}
