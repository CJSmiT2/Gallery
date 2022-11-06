package ua.org.smit.gallery.user.photomodel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import ua.org.smit.gallery.hibarnate.HibernateUtil;
import ua.org.smit.gallery.hibarnate.PhotomodelsDAO;

public class PhotomodelTest {

    private static final Logger log = LogManager.getLogger(PhotomodelTest.class);

    @BeforeAll
    public static void initHibernate() {
        HibernateUtil.buildSessionFactory();
    }

    @Test
    public void writeMetaDataTest() {
        PhotomodelsDAO dao = new PhotomodelsDAO(PhotomodelInfo.class);

        int albumId = 99;

        PhotomodelInfo photomodel = new PhotomodelInfo();
        photomodel.setAlbums(Arrays.asList(albumId));
        photomodel.setNickName("nickName");
        photomodel.setAlias("test_alias");

        dao.create(photomodel);

        List<PhotomodelInfo> result = dao.findByAlbumId(albumId);
        assertFalse(result.isEmpty());
        assertEquals("test_alias", result.get(0).getAlias());
    }

    @Test
    public void tryFindPhotomodelByAlias() {
        PhotomodelsDAO dao = new PhotomodelsDAO(PhotomodelInfo.class);

        String alias = "test_alias_2";

        PhotomodelInfo photomodel = new PhotomodelInfo();
        photomodel.setNickName("nickName");
        photomodel.setAlias(alias);

        dao.create(photomodel);

        Optional<PhotomodelInfo> result = dao.findByAlias(alias);
        assertTrue(result.isPresent());
    }

    @AfterAll
    public static void closeHibernateSession() {
        HibernateUtil.close();
    }

}
