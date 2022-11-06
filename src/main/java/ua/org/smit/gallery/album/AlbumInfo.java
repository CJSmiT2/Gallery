package ua.org.smit.gallery.album;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AlbumInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int albumId;

    @Column(unique = true)
    private String alias;

    private String title;

    private Timestamp created;

    private Timestamp updated;

    private int posterImageId; // need rename to imageAlias

    private int hits;

    private int likes;

    private int imagesCount;

    public AlbumInfo() {
    }

    AlbumInfo(String alias) {
        this.alias = alias;
    }

    public AlbumInfo(String title, String alias) {
        this.title = title;
        this.alias = alias;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

    public int getPosterImageId() {
        return posterImageId;
    }

    public void setPosterImageId(int posterImageId) {
        this.posterImageId = posterImageId;
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

    public int getImagesCount() {
        return imagesCount;
    }

    public void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    public int getHitsInThousends() {
        return hits / 1000;
    }

    public void createdNow() {
        this.created = new Timestamp(System.currentTimeMillis());
        this.updated = this.created;
    }

    public void addHitPlusOne() {
        this.hits++;
    }

    void addImagesCountPlusOne() {
        this.imagesCount++;
    }

    void setUpdateIsNow() {
        this.updated = new Timestamp(System.currentTimeMillis());
    }

}
