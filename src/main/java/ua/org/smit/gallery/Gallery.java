package ua.org.smit.gallery;

import ua.org.smit.gallery.album.Album;
import java.util.List;
import ua.org.smit.gallery.album.AlbumInfo;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.tag.TagsService;
import ua.org.smit.gallery.user.photomodel.Photomodel;
import ua.org.smit.gallery.user.photomodel.PhotomodelInfo;

public interface Gallery {

    Album getAlbum(String alias);

    List<Album> getAllAlbums();

    List<Album> getAlbumsByAliases(List<String> aliases);

    List<Album> getAlbumsByIds(List<Integer> ids);

    List<Album> getLastAlbums(int limit);

    List<AlbumInfo> getAlbumInfoByIds(List<Integer> albumsIds);

    List<String> getAllAlbumAliases();

    List<Integer> getAlbumsIds();

    List<PhotomodelInfo> getAllPhotomodelsInfos();

    List<ImageInfo> getImages(List<Integer> ids);

    List<ImageInfo> getImagesByAliases(List<Integer> aliases);

    Photomodel getPhotomodel(String alias);

    void updateImagesResolution();

    void createAlbum(String title, String alias);

    void addSecondsViewTime(int imageAlias);

    void addHitImage(int imageAlias);

    void addHitAlbum(String albumAlias);

    void deleteImage(String albumAlias, int imageAlias);

    void deleteAlbum(String albumAlias);

    TagsService getTagsService();

    List<AlbumInfo> getAllAlbumsInfos();

    List<ImageInfo> getAllImagesInfos();

}
