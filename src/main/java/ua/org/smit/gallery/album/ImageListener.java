package ua.org.smit.gallery.album;

import java.util.List;
import java.util.Optional;
import ua.org.smit.gallery.album.image.ImageInfo;
import ua.org.smit.gallery.album.image.Images;

public class ImageListener {

    private final Images images;

    private Optional<List<ImageInfo>> selectedImages = Optional.empty();

    ImageListener(Images images) {
        this.images = images;
    }

    public Optional<ImageInfo> getPrevious(ImageInfo currImage) {
        for (int i = 0; i < getImages().size(); i++) {
            if (getImages().get(i).getId() == currImage.getId()) {
                if (i == 0) {
                    return Optional.empty();
                } else {
                    return Optional.of(getImages().get(i - 1));
                }

            }
        }
        return Optional.empty();
    }

    public Optional<ImageInfo> getNext(ImageInfo currImage) {
        for (int i = 0; i < getImages().size(); i++) {
            if (getImages().get(i).getId() == currImage.getId()) {
                if (i == (getImages().size() - 1)) {
                    return Optional.empty();
                } else {
                    return Optional.of(getImages().get(i + 1));
                }
            }
        }
        return Optional.empty();
    }

    private List<ImageInfo> getImages() {
        if (!selectedImages.isPresent()) {
            selectedImages = Optional.of(images.getAllByAlbum());
        }
        return selectedImages.get();
    }
}
