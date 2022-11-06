package ua.org.smit.gallery.album;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import ua.org.smit.gallery.album.image.Images;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import ua.org.smit.common.cropresizeimage.ResizeImage;
import ua.org.smit.common.filesystem.FolderCms;
import ua.org.smit.gallery.album.image.ImageFile;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.hibarnate.AlbumInfoDAO;

public class Album {

    private static final Logger log = LogManager.getLogger(Album.class);

    private final AlbumInfo info;

    private final Images images;
    private final Photomodels photomodels;

    private final AlbumInfoDAO dao = new AlbumInfoDAO(AlbumInfo.class);

    private final FolderCms selfFolder;

    private final ImageListener listener;

    private final ResizeImage resizer = new ResizeImage();

    private final Merge merge;

    public Album(FolderCms galleryFolder, String alias) {
        log.debug("Init album. Alias = '{}'", alias);

        Optional<AlbumInfo> optional = dao.findByAlias(alias);
        if (!optional.isPresent()) {
            throw new RuntimeException("Cant find album by alias '" + alias + "'");
        }

        info = optional.get();
        images = new Images(info.getAlbumId());
        photomodels = new Photomodels(info.getAlbumId());

        this.selfFolder = new FolderCms(
                galleryFolder + File.separator + info.getAlias());

        listener = new ImageListener(images);
        this.merge = new Merge(selfFolder, images, dao);
    }

    public QualityFolder getByQuality(Quality quality) {
        return new QualityFolder(selfFolder, quality);
    }

    public AlbumInfo getInfo() {
        return info;
    }

    public Images getImages() {
        return images;
    }

    public Photomodels getPhotomodels() {
        return photomodels;
    }

    public ImageListener getListener() {
        return listener;
    }

    public void updateImagesResolution() {
        for (ImageInfo imageInfo : this.getImages().getAllByAlbum()) {
            try {
                ImageFile file = getByQuality(Quality.ORIGINAL)
                        .getByAlias(imageInfo.getAlias());
                BufferedImage orig = ImageIO.read(file);
                BufferedImage res = resizer.resize(orig, 1920, 1080);
                imageInfo.setWidth(res.getWidth());
                imageInfo.setHeight(res.getHeight());
                this.getImages().update(imageInfo);

                log.info("Updated image "
                        + "alias = '" + imageInfo.getAlias() + "', "
                        + "[" + res.getWidth() + "*" + res.getHeight() + "]");
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Album.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Merge getMerge() {
        return merge;
    }

    public void setCover(int imageAlias) {
        ImageInfo imageInfo = images.getByAlias(imageAlias);
        info.setPosterImageId(imageInfo.getAlias());
        dao.update(info);
    }

    public void deleteImage(int imageAlias) {
        images.delete(imageAlias);
        for (Quality quality : Quality.values()) {
            this.getByQuality(quality).deleteImageByAlias(imageAlias);
        }
    }

    public void delete() {
        dao.delete(info);
        selfFolder.deleteSelfRecursive();
    }

}
