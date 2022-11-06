package ua.org.smit.gallery.album.image;

import java.util.List;
import ua.org.smit.gallery.hibarnate.ImageInfoDAO;

public class Images {

    private int albumId;

    private final ImageInfoDAO dao = new ImageInfoDAO(ImageInfo.class);

    public Images(int albumId) {
        this.albumId = albumId;
    }

    public ImageInfo getByAlias(int alias) {
        return dao.getImage(albumId, alias);
    }

    public List<ImageInfo> getAllByAlbum() {
        return dao.getImagesByAlbum(albumId);
    }

    public void update(ImageInfo image) {
        this.dao.update(image);
    }

    public ImageInfo create(ImageInfo imageInfo) {
        return this.dao.create(imageInfo);
    }

    public int getAlbumId() {
        return albumId;
    }

    public List<ImageInfo> getAllWithoutAlbum() {
        return dao.findAll();
    }

    public void delete(int imageAlias) {
        ImageInfo imgInfo = dao.getImage(albumId, imageAlias);
        dao.delete(imgInfo);
    }

}
