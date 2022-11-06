package ua.org.smit.gallery.album.image;

import java.io.File;
import ua.org.smit.common.filesystem.FileCms;

public class ImageFile extends FileCms {

    private final int id;

    public ImageFile(File file, int id) {
        super(file);

        this.id = id;
    }

    public int getId() {
        return id;
    }

    public enum Extension {
        JPEG, JPG, jpeg, jpg, webp;
    }

}
