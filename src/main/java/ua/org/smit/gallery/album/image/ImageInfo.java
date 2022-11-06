package ua.org.smit.gallery.album.image;

import java.sql.Timestamp;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import ua.org.smit.gallery.album.AlbumInfo;
import ua.org.smit.gallery.hibarnate.AlbumInfoDAO;

@Entity
public class ImageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int albumId;

    @Column(unique = true)
    private int alias; // file name (number) without extension

    private Timestamp created;

    private int hits;

    private int likes;

    private int timeCounter;

    private int width;

    private int height;

    private String originalFileName; // without extension

    @Transient
    private AlbumInfo albumInfo;

    @Transient
    private final AlbumInfoDAO albumInfoDAO = new AlbumInfoDAO(AlbumInfo.class);

    public ImageInfo() {
    }

    public ImageInfo(int albumId, int alias, Timestamp created) {
        this.albumId = albumId;
        this.alias = alias;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getAlias() {
        return alias;
    }

    public void setAlias(int alias) {
        this.alias = alias;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(int timeCounter) {
        this.timeCounter = timeCounter;
    }

    public Optional<AlbumInfo> getAlbumInfo() {
        return Optional.ofNullable(albumInfo);
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }

    public void bindAlbumInfo() {
        this.albumInfo = albumInfoDAO.findOne(albumId);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTimeViewsMins() {
        int seconds = timeCounter * 5;
        return (seconds / 60);
    }

    public int getHitsInthousands() {
        return hits / 1000;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public void setResolution(Resolution resolution) {
        this.width = resolution.getWidth();
        this.height = resolution.getHeight();
    }

    public void timeCounterPlusOne() {
        timeCounter++;
    }

    public void addHitPlusOne() {
        hits++;
    }

}
