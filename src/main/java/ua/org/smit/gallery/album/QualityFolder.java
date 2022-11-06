package ua.org.smit.gallery.album;

import java.io.File;
import ua.org.smit.common.filesystem.FolderCms;
import ua.org.smit.gallery.album.image.ImageFile;

public class QualityFolder extends FolderCms {

    public QualityFolder(FolderCms folder, Quality quality) {
        super(folder + File.separator + quality.name());
    }

    public ImageFile getByAlias(int imageAlias) { // imageAlias - file name (number) without extension
        File file = null;

        for (ImageFile.Extension ext : ImageFile.Extension.values()) {
            file = new File(this + File.separator + imageAlias + "." + ext);
            if (file.exists()) {
                break;
            }
        }

        return new ImageFile(file, imageAlias);
    }

    void deleteImageByAlias(int imageAlias) {
        getByAlias(imageAlias).delete();
    }

}
