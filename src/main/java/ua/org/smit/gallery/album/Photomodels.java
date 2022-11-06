package ua.org.smit.gallery.album;

import java.util.List;
import java.util.Optional;
import ua.org.smit.gallery.hibarnate.PhotomodelsDAO;
import ua.org.smit.gallery.user.photomodel.PhotomodelInfo;

public class Photomodels {

    private final int albumId;

    private final PhotomodelsDAO dao = new PhotomodelsDAO(PhotomodelInfo.class);

    public Photomodels(int albumId) {
        this.albumId = albumId;
    }

    public List<PhotomodelInfo> getList() {
        return dao.findByAlbumId(albumId);
    }

}
