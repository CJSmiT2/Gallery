package ua.org.smit.gallery.tag;

import ua.org.smit.gallery.tag.Tag;
import java.util.Arrays;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.hibarnate.HibernateUtil;
import ua.org.smit.gallery.hibarnate.ImageInfoDAO;
import ua.org.smit.gallery.hibarnate.TagDAO;

public class TagTest {


    @BeforeAll
    public static void initHibernate() {
        HibernateUtil.buildSessionFactory();
    }

    @Test
    public void tryCreateTag() {
        TagDAO dao = new TagDAO(Tag.class);
        String name = "test_tag_name";

        Tag tag = new Tag();
        tag.setName(name);
        dao.create(tag);

        assertTrue(dao.findByName(name).isPresent());

    }

    @Test
    public void tryAddImageToTag() {
        TagDAO dao = new TagDAO(Tag.class);
        ImageInfoDAO imageDao = new ImageInfoDAO(ImageInfo.class);

        ImageInfo image = new ImageInfo();
        image = imageDao.create(image);
        int imageId = image.getId();

        String name = "test_tag_name2";

        Tag tag = new Tag();
        tag.setName(name);
        tag.getImagesInfos().add(image);
        tag = dao.create(tag);

        Tag tag2 = dao.findByName(name).get();
        int imageId2 = tag2.getImagesInfos().iterator().next().getId();

        assertEquals(imageId, imageId2);

    }
    
    @Test
    public void tryUpdateImagesInTag(){
        ImageInfoDAO imageDao = new ImageInfoDAO(ImageInfo.class);
        TagDAO tagDAO = new TagDAO(Tag.class);

        ImageInfo image1 = new ImageInfo();
        image1.setAlias(1);
        image1 = imageDao.create(image1);
        ImageInfo image2 = new ImageInfo();
        image2.setAlias(2);
        image2 = imageDao.create(image2);
        
        String name = "test_tag_name3";
        Tag tag = new Tag();
        tag.setName(name);
        tagDAO.create(tag);
        
        tag = tagDAO.findByName(name).get();
        tag.addImageInfo(Arrays.asList(image1, image2));
        
        Tag tag2 = tagDAO.findByName(name).get();
        assertEquals(2, tag2.getImagesInfos().size());
    }

    @AfterAll
    public static void closeHibernateSession() {
        HibernateUtil.close();
    }
}
