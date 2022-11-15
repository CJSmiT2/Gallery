package ua.org.smit.gallery.main;

import ua.org.smit.gallery.tag.TagsService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ua.org.smit.gallery.album.Album;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import ua.org.smit.common.filesystem.FolderCms;
import ua.org.smit.gallery.album.AlbumInfo;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.hibarnate.AlbumInfoDAO;
import ua.org.smit.gallery.hibarnate.HibernateUtil;
import ua.org.smit.gallery.hibarnate.ImageInfoDAO;
import ua.org.smit.gallery.hibarnate.PhotomodelsDAO;
import ua.org.smit.gallery.user.photomodel.Photomodel;
import ua.org.smit.gallery.user.photomodel.PhotomodelInfo;
import ua.org.smit.gallery.Gallery;

@Service
public class GalleryService implements Gallery {

    static Logger log = Logger.getLogger(GalleryService.class);

    private final AlbumInfoDAO albumInfoDAO = new AlbumInfoDAO(AlbumInfo.class);
    private final PhotomodelsDAO photomodelsDAO = new PhotomodelsDAO(PhotomodelInfo.class);
    private final ImageInfoDAO imageInfoDAO = new ImageInfoDAO(ImageInfo.class);

    private final TagsService tagsService = new TagsService();

    private final FolderCms galleryFolder;
    
    private Optional<List<String>> aliases = Optional.empty();

    public GalleryService(FolderCms gallery, SessionFactory sessionFactory) {
        this.galleryFolder = gallery;
        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @Override
    public Album getAlbum(String alias) {
        log.info("Get album: " + alias);
        return new Album(galleryFolder, alias);
    }

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        for (AlbumInfo albumInfo : this.albumInfoDAO.findAll()) {
            albums.add(getAlbum(albumInfo.getAlias()));
        }
        return albums;
    }

    @Override
    public List<Album> getAlbumsByAliases(List<String> aliases) {
        List<Album> albums = new ArrayList<>();
        for (String alias : aliases) {
            albums.add(getAlbum(alias));
        }
        return albums;
    }

    @Override
    public List<Integer> getAlbumsIds() {
        return albumInfoDAO.getAllIds();
    }

    @Override
    public List<String> getAllAlbumAliases() {
        return albumInfoDAO.getAllAliases();
    }

    @Override
    public List<PhotomodelInfo> getAllPhotomodelsInfos() {
        return this.photomodelsDAO.getAllWithAlbumsIds();
    }

    @Override
    public List<ImageInfo> getImages(List<Integer> aliases) {
        List<ImageInfo> images = imageInfoDAO.getImagesByAliases(aliases);

        for (ImageInfo image : images) {
            image.bindAlbumInfo();
        }

        return images;
    }

    @Override
    public Photomodel getPhotomodel(String alias) {
        log.info("Get photomodel: '" + alias + "'");
        return new Photomodel(alias);
    }

    @Override
    public List<AlbumInfo> getAlbumInfoByIds(List<Integer> albumsIds) {
        return this.albumInfoDAO.getByIds(albumsIds);
    }

    public List<ImageInfo> getImagesByAliases(List<Integer> aliases) {
        return this.imageInfoDAO.getImagesByAliases(aliases);
    }

    @Override
    public void updateImagesResolution() {
        log.info("Start update images resoltion!");

        for (Album album : this.getAllAlbums()) {
            log.info("Update album: " + album.getInfo().getAlias());
            album.updateImagesResolution();
        }

        log.info("Complete update images resolution!");
    }

    @Override
    public void createAlbum(String title, String alias) {
        if (albumInfoDAO.findByAlias(alias).isPresent()) {
            throw new RuntimeException("Alias '" + alias + "' already exist!");
        }
        AlbumInfo albumInfo = new AlbumInfo(title, alias);
        albumInfo.createdNow();
        this.albumInfoDAO.create(albumInfo);
    }

    @Override
    public List<Album> getLastAlbums(int limit) {
        return this.getAlbumsByIds(albumInfoDAO.getLastIds(limit));
    }

    @Override
    public List<Album> getAlbumsByIds(List<Integer> ids) {
        List<Album> selected = new ArrayList<>();
        for (AlbumInfo albumInfo : getAlbumInfoByIds(ids)) {
            selected.add(this.getAlbum(albumInfo.getAlias()));
        }
        return selected;
    }

    @Override
    public void addSecondsViewTime(int imageAlias) {
        Optional<ImageInfo> optional = imageInfoDAO.findByAlias(imageAlias);
        if (optional.isPresent()) {
            ImageInfo imageInfo = optional.get();
            imageInfo.timeCounterPlusOne();
            imageInfoDAO.update(imageInfo);
        }
    }

    @Override
    public void addHitImage(int imageAlias) {
        Optional<ImageInfo> optional = imageInfoDAO.findByAlias(imageAlias);
        if (optional.isPresent()) {
            ImageInfo imageInfo = optional.get();
            imageInfo.addHitPlusOne();
            imageInfoDAO.update(imageInfo);
        }
    }

    @Override
    public void addHitAlbum(String albumAlias) {
        Optional<AlbumInfo> optional = this.albumInfoDAO.findByAlias(albumAlias);
        if (optional.isPresent()) {
            AlbumInfo albumInfo = optional.get();
            albumInfo.addHitPlusOne();
            albumInfoDAO.update(albumInfo);
        }
    }

    @Override
    public void deleteImage(String albumAlias, int imageAlias) {
        Album album = this.getAlbum(albumAlias);
        tagsService.removeFromTags(imageAlias);
        album.deleteImage(imageAlias);
        log.info("Image '" + imageAlias + "' has deleted "
                + "from album '" + albumAlias + "'");
    }

    @Override
    public TagsService getTagsService() {
        return tagsService;
    }

    @Override
    public void deleteAlbum(String albumAlias) {
        Album album = this.getAlbum(albumAlias);
        for (ImageInfo imgInfo : album.getImages().getAllByAlbum()) {
            this.deleteImage(albumAlias, imgInfo.getAlias());
        }
        album.delete();
    }

    @Override
    public List<AlbumInfo> getAllAlbumsInfos() {
        return this.albumInfoDAO.findAll();
    }

    @Override
    public List<ImageInfo> getAllImagesInfos() {
        return this.imageInfoDAO.findAll();
    }

    @Override
    public boolean isAliasExist(String alias) {
        if (!aliases.isPresent()){
            aliases = Optional.ofNullable(this.getAllAlbumAliases());
        }
        
        for (String aliasInDb : aliases.get()) {
            if (aliasInDb.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

}
