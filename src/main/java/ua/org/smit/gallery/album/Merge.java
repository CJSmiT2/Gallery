package ua.org.smit.gallery.album;

import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import ua.org.smit.common.filesystem.FileCms;
import ua.org.smit.common.filesystem.FolderCms;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.album.image.Images;
import ua.org.smit.gallery.album.image.Resolution;
import ua.org.smit.gallery.hibarnate.AlbumInfoDAO;

public class Merge {

    private final FolderCms selfFolder;
    private final Images images;
    private final AlbumInfoDAO albumInfoDAO;

    Merge(FolderCms selfFolder, Images images, AlbumInfoDAO albumInfoDAO) {
        this.selfFolder = selfFolder;
        this.images = images;
        this.albumInfoDAO = albumInfoDAO;
    }

    public void mergeFiles(List<File> files, Quality quality) {
        for (FileCms file : FileCms.convert(files)) {
            ImageInfo imgInfo;

            if (quality == Quality.RESIZED) {
                imgInfo = createNew(file.getNameWithoutExtension());
                imgInfo = images.create(imgInfo);
            } else {
                imgInfo = getByOriginalFileName(file.getNameWithoutExtension());
            }

            String imageAliasName = imgInfo.getAlias() + "." + file.getExtension();
            file = file.rename(imageAliasName);
            getByQuality(quality).put(file);

            AlbumInfo albumInfo = albumInfoDAO.findOne(images.getAlbumId());
            if (quality == Quality.RESIZED) {
                albumInfo.addImagesCountPlusOne();
            }
            albumInfo.setUpdateIsNow();
            albumInfoDAO.update(albumInfo);
        }
    }

    private boolean isFileNameUniqe(String name) {
        for (ImageInfo info : this.images.getAllByAlbum()) {
            if (info.getOriginalFileName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    private ImageInfo createNew(String fileName) {
        String nameForBd = fileName;
        if (fileName.contains("_resolution-")) {
            nameForBd = fileName.split("_resolution-")[0];
        }

        ImageInfo info = new ImageInfo();
        info.setAlbumId(images.getAlbumId());
        info.setAlias(createNewAlias());
        info.setCreated(new Timestamp(System.currentTimeMillis()));
        info.setOriginalFileName(nameForBd);
        info.setResolution(getResolution(fileName));
        return info;
    }

    private int createNewAlias() {
        int alias = 1;

        for (ImageInfo info : images.getAllWithoutAlbum()) {
            if (info.getAlias() > alias) {
                alias = info.getAlias();
            }
        }

        return ++alias;
    }

    private QualityFolder getByQuality(Quality quality) {
        return new QualityFolder(selfFolder, quality);
    }

    private ImageInfo getByOriginalFileName(String name) {
        for (ImageInfo info : images.getAllByAlbum()) {
            if (info.getOriginalFileName().equals(name)) {
                return info;
            }
        }
        throw new RuntimeException("Cant find 'ImageInfo' by originalFileName '" + name + "' in album!");
    }

    private Resolution getResolution(String name) {
        if (!name.contains("resolution-")) {
            throw new IllegalArgumentException("Wrong file name: '" + name + "'");
        }

        String[] resolution = name.split("resolution-");
        String[] splited = resolution[1].split("x");
        int width = Integer.valueOf(splited[0]);
        int height = Integer.valueOf(splited[1]);

        return new Resolution(width, height);
    }
}
